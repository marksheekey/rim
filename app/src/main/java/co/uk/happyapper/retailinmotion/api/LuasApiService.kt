package co.uk.happyapper.retailinmotion

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface LuasApiService {
    @GET("get.ashx")
    fun getStopInfo(
        @Query("stop") stop: String? = null,
        @Query("action") action: String = "forecast",
        @Query("encrypt") encrypt: String = "false"
    ): Call<StopInfo>

    companion object {
        fun create(): LuasApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .baseUrl("http://luasforecasts.rpa.ie/xml/")
                .build()

            return retrofit.create(LuasApiService::class.java)
        }
    }
}
