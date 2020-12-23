package co.uk.happyapper.retailinmotion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snakydesign.livedataextensions.emptyLiveData
import com.snakydesign.livedataextensions.switchMap


class MainViewModel(private val luasRepo: LuasRepo, private val clockService: ClockService) : ViewModel() {

    private val updateData: MutableLiveData<Station> = emptyLiveData()
    val loading: MutableLiveData<Boolean> = emptyLiveData()
    val info: MutableLiveData<UIData> = emptyLiveData()

    fun refresh() {
        if (clockService.getTimeFromMillis(clockService.getNow()) <= 1200) {
            updateData.postValue(Station.MARLBOROUGH)
        } else {
            updateData.postValue(Station.STILLORGAN)
        }
    }

    val data = updateData.switchMap { station ->
        luasRepo.getStopInfo(station.name)
    }

    fun gotData(stopInfo: StopInfo) {
        if (Station.valueOf(stopInfo.stop) == Station.MARLBOROUGH) {
            stopInfo.directions?.filter { it.name == "Outbound" }?.first().run {
                info.postValue(UIData("Marlborough", this?.tramDestinations))
            }
        } else {
            stopInfo.directions?.filter { it.name == "Inbound" }?.first().run {
                info.postValue(UIData("Stillorgan", this?.tramDestinations))
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