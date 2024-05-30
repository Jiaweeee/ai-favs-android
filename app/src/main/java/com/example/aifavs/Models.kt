package com.example.aifavs

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int,
    val msg: String?,
    val data: T?
)

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
)

@Serializable
data class AddContentRequestBody(
    val url: String
)

@Serializable
data class ChatRequestBody(
    val input: String,
    @SerializedName("chat_history") val history: List<String>? = null
)