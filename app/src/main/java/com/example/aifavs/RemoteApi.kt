package com.example.aifavs

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {
    @POST("content/add")
    fun addContent(@Body body: AddContentRequestBody): Observable<BaseResponse<String>>

    @GET("content/list/get")
    fun getContentList(
        @Query("category_id") categoryId: String? = null,
        @Query("tag_id") tagId: String? = null
    ): Observable<BaseResponse<List<Collection>>>

    @GET("category/list/get")
    fun getCategories(): Observable<BaseResponse<List<CategoryOverview>>>

    @GET("tag/list/get")
    fun getTags(): Observable<BaseResponse<List<TagOverview>>>
}