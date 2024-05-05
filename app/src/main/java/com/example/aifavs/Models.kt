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
data class ContentItem(
    val id: String,
    val url: String,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    @SerializedName("full_text") val fullText: String?,
    @SerializedName("ai_labels") val labels: List<String>?,
    @SerializedName("ai_summary") val summary: String?,
    @SerializedName("ai_highlights") val highlights: List<String>?,
    @SerializedName("ai_podcast_url") val podcastUrl: String?,
)

@Serializable
data class AddContentRequestBody(
    val url: String
)