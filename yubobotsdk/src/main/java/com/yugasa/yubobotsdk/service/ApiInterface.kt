package com.yugasa.yubobotsdk.service


import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    //    api method with out auth header
    @POST("{api_link}")
    fun getWithoutHeaderApi(
        @Path(value = "api_link", encoded = true) api_link: String?,
        @Body requestBody: RequestBody?
    ): Call<ResponseBody>

    //    api method with auth header
    @POST("{api_link}")
    fun getWithHeaderApi(
        @Header("Authorization") header : String?,
        @Path(
            value = "api_link",
            encoded = true
        ) api_link: String?,
        @Body requestBody: RequestBody?
    ): Call<ResponseBody>

    //    api method with auth header
    //    @Headers({"Content-Type: multipart/form-data","Content-Type: text/plain"})
    @Multipart
    @POST("{api_link}")
    fun getLoginResponse(
        @Path(value = "api_link", encoded = true) api_link: String?,
        @PartMap params: Map<String, RequestBody>?
    ): Call<ResponseBody>

    @Multipart
    @POST("{api_link}")
    fun uploadWithSingleImage(
        @Path(value = "api_link", encoded = true) api_link: String,
        @Part photo: MultipartBody.Part,
        @Part("fileList") params1: RequestBody,
        @Part("fileList") params2: RequestBody,
    ): Call<ResponseBody>

    @Multipart
    @POST("{api_link}")
    fun uploadWithMultiImage(
        @Path(value = "api_link", encoded = true) api_link: String?,
        @Part filePhoto: List<MultipartBody.Part?>?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<ResponseBody>

    @Multipart
    @POST("{api_link}")
    fun uploadWithImageAndList(
        @Path(value = "api_link", encoded = true) api_link: String?,
        @Part filePhoto: List<MultipartBody.Part?>?,
        @PartMap listMap: Map<String?, RequestBody?>?,
        @Part("postTag[]") params: List<String?>?
    ): Call<ResponseBody>
}