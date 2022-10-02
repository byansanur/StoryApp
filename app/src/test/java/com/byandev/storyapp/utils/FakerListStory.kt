package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.ResponseAllStories
import com.byandev.storyapp.data.model.Story

object FakerListStory {

    fun generateListStory() : ResponseAllStories {
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val news = Story(
                "2022-02-22T22:22:22Z",
                "description $i",
                "story-fnanf",
                null,
                null,
                "story $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                )
            storyList.add(news)
        }
        return ResponseAllStories(
            error = false,
            listStory = storyList,
            message = "Success"
        )
    }
}