package com.byandev.storyapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.byandev.storyapp.data.paging_source.StoryPagingSource
import com.byandev.storyapp.services.ApiServices
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiServices: ApiServices
) {

    fun getListStory() =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { StoryPagingSource(apiServices = apiServices) }
        ).flow
}