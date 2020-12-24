package co.uk.happyapper.retailinmotion
import androidx.lifecycle.MutableLiveData
import co.uk.happyapper.retailinmotion.repo.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class NetworkCall<T>{
    lateinit var call:Call<T>
    fun makeCall(call:Call<T>): MutableLiveData<Resource<T>> {
        this.call = call
        val callBackKt = CallBackKt<T>()
        callBackKt.result.value = Resource.loading(null)
        this.call.clone().enqueue(callBackKt)
        return callBackKt.result
    }

    class CallBackKt<T>: Callback<T> {
        var result: MutableLiveData<Resource<T>> = MutableLiveData()

        override fun onFailure(call: Call<T>, t: Throwable) {
            result.value = Resource.error(t.message)
            t.printStackTrace()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if(response.isSuccessful)
                result.value = Resource.success(response.body())
            else{
                result.value = Resource.error(response.message())
            }
        }
    }

    fun cancel(){
        if(::call.isInitialized){
            call.cancel()
        }
    }
}