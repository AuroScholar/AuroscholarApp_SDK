package com.auroscholar.final_auroscholarapp_sdk

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface SDKRemoteApi
{


    @POST("auto_login")
    public fun getSDKData(@Body params:HashMap<String,String>): Call<SDKDataModel>



  companion object
  {
    operator fun invoke():SDKRemoteApi
    {
      return Retrofit.Builder()
        .baseUrl("https://staging.auroscholar.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SDKRemoteApi::class.java)
    }
  }
}