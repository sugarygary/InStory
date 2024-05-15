package com.sugarygary.instory.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoryResponse(

    @Json(name = "listStory")
    val listStory: List<Story>,

    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String
)

@Entity(tableName = "stories")
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
    val lon: Float? = null,

    @PrimaryKey
    @Json(name = "id")
    val id: String,

    @Json(name = "lat")
    val lat: Float? = null
)

