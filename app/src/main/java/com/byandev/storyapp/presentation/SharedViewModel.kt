package com.byandev.storyapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.byandev.storyapp.data.DataRepository
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import com.byandev.storyapp.di.UtilsConnect
import com.byandev.storyapp.services.ServicesRepository
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.handlingError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository,
    private val utilsConnect: UtilsConnect,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun registerUsers(request: Register) : LiveData<Resources<ResponseBase>> {
        val output = MutableLiveData<Resources<ResponseBase>>()
        if (utilsConnect.isConnectedToInternet()) {
            compositeDisposable.add(
                servicesRepository.postRegister(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { output.postValue(Resources.Loading()) }
                    .subscribe({
                        if (!it.error) output.postValue(Resources.Success(it))
                        else output.postValue(Resources.Error(it.message, null))
                    }, {
                        output.postValue(Resources.Error(handlingError(it), null))
                    })
            )
        } else {
            output.postValue(Resources.Error("No internet connection"))
        }
        return output
    }


    fun loginUsers(request: Login) : LiveData<Resources<ResponseLogin>> {
        val output = MutableLiveData<Resources<ResponseLogin>>()
        if (utilsConnect.isConnectedToInternet()) {
            compositeDisposable.add(
                servicesRepository.postLogin(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { output.postValue(Resources.Loading()) }
                    .subscribe({
                        if (!it.error) output.postValue(Resources.Success(it))
                        else output.postValue(Resources.Error(it.message, null))
                    }, {
                        output.postValue(Resources.Error(handlingError(it), null))
                    })
            )
        } else {
            output.postValue(Resources.Error("No internet connection"))
        }
        return output
    }

    fun getListStory() = dataRepository.getListStory()
        .map { data -> data.map { it } }
        .cachedIn(viewModelScope)
}