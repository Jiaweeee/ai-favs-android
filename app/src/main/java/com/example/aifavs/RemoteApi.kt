package com.example.aifavs

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {
    @GET("content/list/get")
    fun getContentList(
        @Query("category_id") categoryId: String? = null
    ): Observable<BaseResponse<List<ContentItem>>>

    @POST("content/add")
    fun addContent(@Body body: AddContentRequestBody): Observable<BaseResponse<String>>
}