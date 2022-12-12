package com.byandev.storyapp.common.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.byandev.storyapp.common.utils.Event
import com.byandev.storyapp.common.utils.NavigationCommand
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

    // FOR NAVIGATION
    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }

    protected val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        val suppressedException = exception.suppressed

        if (suppressedException.isNotEmpty()) {
            Log.d(TAG, "Caught $exception with another suppressed exception(s):")

            suppressedException.forEach { throwable ->
                Log.d(TAG, "Also caught $throwable")
            }
        } else {
            Log.d(TAG, "Caught $exception")
        }
    }

    protected fun launchWithViewModel(launchBlock: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(exceptionHandler) { launchBlock() }
}