package co.uk.happyapper.retailinmotion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.happyapper.retailinmotion.repo.Resource
import com.snakydesign.livedataextensions.emptyLiveData
import com.snakydesign.livedataextensions.map
import com.snakydesign.livedataextensions.switchMap


class MainViewModel(private val luasRepo: LuasRepo, private val clockService: ClockService) :
    ViewModel() {

    private val updateData: MutableLiveData<Station> = emptyLiveData()

    fun refresh() {
        if (clockService.getTimeFromMillis(clockService.getNow()) <= 1200) {
            updateData.postValue(Station.MARLBOROUGH)
        } else {
            updateData.postValue(Station.STILLORGAN)
        }
    }

    val data = updateData.switchMap { station ->
        luasRepo.getStopInfo(station.type).map {
            when (it.status) {
                Resource.Status.ERROR -> LuasUI.LuasError(message = it.apiError + "")
                Resource.Status.LOADING -> LuasUI.LuasLoading
                Resource.Status.SUCCESS -> it.data?.let { stopInfo ->
                    returnTrams(stopInfo)
                }
            }
        }
    }
}


private fun returnTrams(stopInfo: StopInfo): LuasUI {
    if (stopInfo.directions == null || stopInfo.directions?.size == 0) {
        return LuasUI.LuasError("No trams found")
    }

    val direction = if (Station.getStation(stopInfo.stop) == Station.MARLBOROUGH) {
        "Outbound"
    } else {
        "Inbound"
    }

    stopInfo.directions?.filter { it.name == direction }?.firstOrNull()?.let { trams ->
        return LuasUI.LuasData(
            UIData(
                stopInfo.stopName,
                stopInfo.message,
                trams.tramDestinations
            )
        )
    }
    return LuasUI.LuasError("No trams found")
}

enum class Station(val type: String) {
    MARLBOROUGH("MAR"),
    STILLORGAN("STI"),
    UNKNOWN("???");

    companion object {
        fun getStation(id: String): Station {
            return values().firstOrNull() { it.type == id } ?: UNKNOWN
        }
    }

}

class UIData(
    val station: String,
    val message: String?,
    val destinations: List<TramDestination>?
)

sealed class LuasUI {
    object LuasLoading : LuasUI()
    data class LuasError(val message: String) : LuasUI()
    data class LuasData(val data: UIData) : LuasUI()
}