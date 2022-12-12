package com.byandev.storyapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.byandev.storyapp.common.base.BaseViewModel
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.domain.usecase.PostStoryUseCase
import com.byandev.storyapp.domain.usecase.RegisteringNewUserUseCase
import com.byandev.storyapp.domain.usecase.StoryPagingSource
import com.byandev.storyapp.domain.usecase.UserLoginUseCase
import com.byandev.storyapp.repository.repo.StoryRepository
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PresentationViewModel constructor(
    private val registerUseCase: RegisteringNewUserUseCase,
    private val loginUseCase: UserLoginUseCase,
    private val storyRepository: StoryRepository,
    private val postStoryUseCase: PostStoryUseCase
) : BaseViewModel() {

    fun registerUsers(request: Register) = registerUseCase.invoke(request)

    fun loginUsers(request: Login) = loginUseCase.invoke(request)

    private fun getListStoryPaging(location: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = true,
            initialLoadSize = 5
        ),
        pagingSourceFactory = { StoryPagingSource(storyRepository, location) }
    ).flow

    fun getListStory(location: Int) = getListStoryPaging(location).map { data -> data.map { it } }.cachedIn(viewModelScope)

    fun postStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) = postStoryUseCase.invoke(description, photo, lat, lon)
}