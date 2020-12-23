package co.uk.happyapper.retailinmotion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.happyapper.retailinmotion.repo.Resource
import com.snakydesign.livedataextensions.emptyLiveData
import com.snakydesign.livedataextensions.map
import com.snakydesign.livedataextensions.switchMap


class MainViewModel(private val luasRepo: LuasRepo, private val clockService: ClockService) : ViewModel() {

    val loading: MutableLiveData<Boolean> = emptyLiveData()
    val info: MutableLiveData<List<TramDestination>> = emptyLiveData()

    fun refresh(){
        if(clockService.getTimeFromMillis(clockService.getNow()) <= 1200){
            getData(Station.MARLBOROUGH)
        }else{
            getData(Station.STILLORGAN)
        }
    }

    private fun getData(station: Station) =
        luasRepo.getStopInfo(station.name).map{ status ->
            when(status.status){
                Resource.Status.LOADING -> loading.postValue(true)
                Resource.Status.ERROR -> { loading.postValue(false)}
                Resource.Status.SUCCESS -> {
                    loading.postValue(false)
                    status.data?.let{
                        if(station == Station.MARLBOROUGH){
                            status.data.directions?.filter { it.name == "Outbound" }?.first().run {
                                info.postValue(this?.tramDestinations)
                            }
                        }else{
                            status.data.directions?.filter { it.name == "Inbound" }?.first().run {
                                info.postValue(this?.tramDestinations)
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