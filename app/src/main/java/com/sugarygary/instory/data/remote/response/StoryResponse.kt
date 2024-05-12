package com.sugarygary.instory.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class StoryResponse(

    @Json(name = "listStory")
    val listStory: List<Story>? = null,

    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String
)

@JsonClass(generateAdapter = true)
data class Story(

    @Json(name = "photoUrl")
    val photoUrl: String,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "lon")
    val lon: Any? = null,

    @Json(name = "id")
    val id: String,

    @Json(name = "lat")
    val lat: Any? = null
)
