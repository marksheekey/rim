package co.uk.happyapper.retailinmotion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.happyapper.retailinmotion.repo.Resource
import com.snakydesign.livedataextensions.emptyLiveData
import com.snakydesign.livedataextensions.map
import com.snakydesign.livedataextensions.switchMap


class MainViewModel(private val luasRepo: LuasRepo, private val clockService: ClockService) : ViewModel() {

    private val updateData: MutableLiveData<Station> = emptyLiveData()

    fun refresh() {
        if (clockService.getTimeFromMillis(clockService.getNow()) <= 1200) {
            updateData.postValue(Station.MARLBOROUGH)
        } else {
            updateData.postValue(Station.STILLORGAN)
        }
    }

    val data = updateData.switchMap { station ->
        luasRepo.getStopInfo(station.name).map {
            when (it.status) {
                Resource.Status.ERROR -> LuasUI.LuasError(message = it.apiError + "")
                Resource.Status.LOADING -> LuasUI.LuasLoading
                Resource.Status.SUCCESS -> it.data?.let { stopInfo ->
                    if (Station.valueOf(stopInfo.stop) == Station.MARLBOROUGH) {
                        stopInfo.directions?.filter { it.name == "Outbound" }?.first().run {
                            LuasUI.LuasData(UIData(stopInfo.stopName, this?.tramDestinations))
                        }
                    } else {
                        stopInfo.directions?.filter { it.name == "Inbound" }?.first().run {
                            LuasUI.LuasData(UIData(stopInfo.stopName, this?.tramDestinations))
                        }
                    }
                }
            }
        }
    }
}

enum class Station(val type: String) {
    MARLBOROUGH("MAR"),
    STILLORGAN("STI")
}

class UIData(
    val station: String,
    val destinations: List<TramDestination>?
)

sealed class LuasUI {
    object LuasLoading : LuasUI()
    data class LuasError(val message: String) : LuasUI()
    data class LuasData(val data: UIData) : LuasUI()
}