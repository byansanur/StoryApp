package com.byandev.storyapp.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.byandev.storyapp.data.model.ResponseAllStories
import com.byandev.storyapp.data.model.Story
import com.byandev.storyapp.services.ApiServices
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val apiServices: ApiServices,
    private val location: Int? = 0
) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1
            val response = apiServices.getAllStories(
                page, 10, location
            )
            val nextKey =
                if (response.listStory.isNotEmpty())
                    page +1
                else null
            LoadResult.Page(
                data = response.listStory,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (io: IOException) {
            LoadResult.Error(io)
        } catch (http: HttpException) {
            LoadResult.Error(http)
        }
    }
}