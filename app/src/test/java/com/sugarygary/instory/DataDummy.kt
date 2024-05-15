package com.sugarygary.instory

import com.sugarygary.instory.data.remote.response.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                id = "story-$i",
                name = "dummy-$i",
                description = "dummy description",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1715751928325_267e7d8d85be9c86e789.jpg",
                createdAt = "2024-05-15T00:00:00.000Z",
                lat = 100F,
                lon = -6F
            )
            items.add(story)
        }
        return items
    }
}