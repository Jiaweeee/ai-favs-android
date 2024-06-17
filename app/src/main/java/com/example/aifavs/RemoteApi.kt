package com.example.aifavs

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {
    @POST("/collection/add")
    fun addCollection(@Body body: AddContentRequestBody): Observable<BaseResponse<String>>

    @GET("/collection/list/get")
    fun getCollectionList(
        @Query("category_id") categoryId: String? = null,
        @Query("tag_id") tagId: String? = null
    ): Observable<BaseResponse<List<Collection>>>

    @GET("/collection/overview")
    fun getCollectionOverview(): Observable<BaseResponse<CollectionOverviewResponse>>

    @POST("/podcast/create")
    fun createPodcast(@Body body: CreatePodcastRequestBody): Observable<BaseResponse<Any>>

    @GET("/podcast/list/get")
    fun getPodcastList(): Observable<BaseResponse<List<PodcastInfo>>>
}