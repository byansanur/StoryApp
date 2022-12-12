package com.byandev.storyapp.domain.usecase

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.byandev.storyapp.data.model.dto.Stories
import com.byandev.storyapp.repository.repo.StoryRepository
import kotlinx.coroutines.flow.single
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource constructor(
    private val storyRepository: StoryRepository,
    private val location: Int
) : PagingSource<Int, Stories>() {
    override fun getRefreshKey(state: PagingState<Int, Stories>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stories> {
        return try {
            val page = params.key ?: 1
            val response = storyRepository.getAllStory(
                page, 10, location
            ).single().data
            val nextKey = if (response!!.listStory.isNotEmpty()) page +1 else null
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