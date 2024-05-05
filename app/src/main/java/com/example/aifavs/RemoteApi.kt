package com.example.aifavs

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteApi {
    @GET("content/list/get")
    fun getContentList(): Observable<BaseResponse<List<ContentItem>>>

    @POST("content/add")
    fun addContent(@Body body: AddContentRequestBody): Observable<BaseResponse<String>>
}