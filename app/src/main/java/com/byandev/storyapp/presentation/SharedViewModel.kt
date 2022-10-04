package com.byandev.storyapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.byandev.storyapp.data.model.*
import com.byandev.storyapp.services.ServicesRepository
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.handlingError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun registerUsers(request: Register) : LiveData<Resources<ResponseBase>> {
        val output = MutableLiveData<Resources<ResponseBase>>()
        compositeDisposable.add(
            servicesRepository.postRegister(request)
                .doOnSubscribe { output.postValue(Resources.Loading()) }
                .subscribe({
                    if (!it.error) output.postValue(Resources.Success(it))
                    else output.postValue(Resources.Error(it.message, null))
                }, {
                    output.postValue(Resources.Error(handlingError(it), null))
                })
        )
        return output
    }


    fun loginUsers(request: Login) : LiveData<Resources<ResponseLogin>> {
        val output = MutableLiveData<Resources<ResponseLogin>>()
        compositeDisposable.add(
            servicesRepository.postLogin(request)
                .doOnSubscribe { output.postValue(Resources.Loading()) }
                .subscribe({
                    if (!it.error) output.postValue(Resources.Success(it))
                    else output.postValue(Resources.Error(it.message, null))
                }, {
                    output.postValue(Resources.Error(handlingError(it), null))
                })
        )
        return output
    }

    fun getListStory(location: Int) : Flow<PagingData<Story>> = servicesRepository.getListStory(location)
        .map { data -> data.map { it } }
        .cachedIn(viewModelScope)

    fun getListStoryLocation() : LiveData<Resources<ResponseAllStories>> {
        val output = MutableLiveData<Resources<ResponseAllStories>>()
        compositeDisposable.add(
            servicesRepository.getStoryLocation()
                .doOnSubscribe { output.postValue(Resources.Loading()) }
                .subscribe({
                    output.postValue(Resources.Success(it))
                }, {
                    output.postValue(Resources.Error(handlingError(it), null))
                })
        )
        return output
    }

    fun postStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) : LiveData<Resources<ResponseBase>> {
        val output = MutableLiveData<Resources<ResponseBase>>()
        compositeDisposable.add(
            servicesRepository.postStories(
                description, photo, lat, lon
            ).doOnSubscribe { output.postValue(Resources.Loading()) }
                .subscribe({
                    if (!it.error) output.postValue(Resources.Success(it))
                    else output.postValue(Resources.Error(it.message, null))
                }, {
                    output.postValue(Resources.Error(handlingError(it), null))
                })
        )
        return output
    }
}