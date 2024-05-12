package com.sugarygary.instory.data.remote.response

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class StoryDetailResponse(

    @Json(name = "story")
    val story: Story? = null,

    @Json(name = "error")
    val error: Boolean,

    @Json(name = "message")
    val message: String
)

