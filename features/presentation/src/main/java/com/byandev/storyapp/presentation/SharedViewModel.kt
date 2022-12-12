package com.byandev.storyapp.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.byandev.storyapp.common.base.BaseViewModel
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.dto.Stories
import com.byandev.storyapp.domain.usecase.PostStoryUseCase
import com.byandev.storyapp.domain.usecase.RegisteringNewUserUseCase
import com.byandev.storyapp.domain.usecase.StoryPagingSource
import com.byandev.storyapp.domain.usecase.UserLoginUseCase
import com.byandev.storyapp.presentation.auth.FragmentLoginDirections
import com.byandev.storyapp.presentation.intro.FragmentIntroPermissionDirections
import com.byandev.storyapp.repository.repo.StoryRepository
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SharedViewModel : BaseViewModel() {


    fun navigateToIntro() {
        navigate(FragmentHomeDirections.actionFragmentHomeToFragmentIntroPermission())
    }

    fun navigateToHomeFromIntro() {
        navigate(FragmentIntroPermissionDirections.actionFragmentIntroPermissionToFragmentHome())
    }

    fun navigateToHomeFromLogin() {
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentHome())
    }

    fun navigateToLogin() {
        navigate(FragmentHomeDirections.actionFragmentHomeToFragmentLogin())
    }

    fun navigateToRegister() {
        navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRegister())
    }

    fun navigateToDetail(detailStory: Stories) {
        navigate(FragmentHomeDirections.actionFragmentHomeToFragmentDetailStory(detailStories = detailStory))
    }

//    fun getListStory(location: Int) : Flow<PagingData<Story>> = servicesRepository.getListStory(location)
//        .map { data -> data.map { it } }
//        .cachedIn(viewModelScope)

//    fun getListStoryLocation() : LiveData<Resources<ResponseAllStories>> {
//        val output = MutableLiveData<Resources<ResponseAllStories>>()
//        compositeDisposable.add(
//            servicesRepository.getStoryLocation()
//                .doOnSubscribe { output.postValue(Resources.Loading()) }
//                .subscribe({
//                    output.postValue(Resources.Success(it))
//                }, {
//                    output.postValue(Resources.Error(handlingError(it), null))
//                })
//        )
//        return output
//    }
//
//    fun postStories(
//        description: RequestBody,
//        photo: MultipartBody.Part,
//        lat: RequestBody?,
//        lon: RequestBody?
//    ) : LiveData<Resources<ResponseBase>> {
//        val output = MutableLiveData<Resources<ResponseBase>>()
//        compositeDisposable.add(
//            servicesRepository.postStories(
//                description, photo, lat, lon
//            ).doOnSubscribe { output.postValue(Resources.Loading()) }
//                .subscribe({
//                    if (!it.error) output.postValue(Resources.Success(it))
//                    else output.postValue(Resources.Error(it.message, null))
//                }, {
//                    output.postValue(Resources.Error(handlingError(it), null))
//                })
//        )
//        return output
//    }
}