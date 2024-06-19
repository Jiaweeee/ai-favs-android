package com.example.aifavs

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int,
    val msg: String?,
    val data: T?
) {
    fun isSuccess() = code == 200
}

@Serializable
data class Tag(
    val id: String,
    val name: String,
)

@Serializable
data class TagOverview(
    val id: String,
    val name: String,
    @SerializedName("collection_count") val collectionCount: Int = 0,
)

@Serializable
data class CategoryOverview(
    val id: String,
    val name: String,
    val description: String,
    @SerializedName("collection_count") val collectionCount: Int = 0,
)

@Serializable
data class CollectionOverviewResponse(
    val categories: List<CategoryOverview>?,
    val tags: List<TagOverview>?
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val description: String
)

@Serializable
data class Collection(
    val id: String,
    val url: String,
    val title: String?,
    val description: String?,
    @SerializedName("thumbnail_url")val thumbnail: String?,
    val category: Category?,
    val tags: List<Tag>?,
    val summary: String?,
    val highlights: List<String>?,
    @SerializedName("podcast_id") val podcastId: String?,
)

@Serializable
data class AddContentRequestBody(
    val url: String
)

@Serializable
data class CreatePodcastRequestBody(
    @SerializedName("collection_id") val collectionId: String
)

@Serializable
data class PodcastInfo(
    val title: String,
    val status: Int,
    @SerializedName("file_path") val filePath: String? = null,
    val transcript: String? = null,
    @SerializedName("collection_id") val collectionId: String,
    @SerializedName("collection_url") val collectionUrl: String,
) {
    fun audioUrl() = "${BuildConfig.BASE_URL}$filePath"
}