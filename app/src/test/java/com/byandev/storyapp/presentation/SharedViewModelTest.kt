package com.byandev.storyapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import com.byandev.storyapp.services.ServicesRepository
import com.byandev.storyapp.utils.FakerResponseBase
import com.byandev.storyapp.utils.FakerResponseLogin
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.getOrAwaitValue
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class SharedViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var servicesRepository: ServicesRepository

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var fakeResponseBase: FakerResponseBase
    private lateinit var fakeResponseLogin: FakerResponseLogin

    @Before
    fun setUp() {
        servicesRepository = mock(ServicesRepository::class.java)
        sharedViewModel = Mockito.spy(SharedViewModel(servicesRepository))

        fakeResponseBase = FakerResponseBase
        fakeResponseLogin = FakerResponseLogin
    }

    @Test
    fun ` When successfully registering, ensure registration returns value from API`() {
        val registerFake = Register(name = "aaa", email = "aaa@mail.com", password = "123456")
        val expect = fakeResponseBase.responseBaseSuccessfulFaker

        Mockito
            .`when`(servicesRepository.postRegister(registerFake))
            .thenReturn(Single.just(expect))

        val liveData: Resources<ResponseBase> = sharedViewModel.registerUsers(registerFake).getOrAwaitValue()
        assertEquals(expect, liveData.data)
        assertNotNull(liveData)
        verify(servicesRepository).postRegister(registerFake)

    }

    @Test
    fun ` When successfully login, ensure registration returns value from API`() {
        val loginFake = Login(email = "aaa@mail.com", password = "123456")
        val expect = fakeResponseLogin.generateLoginFake()

        Mockito
            .`when`(servicesRepository.postLogin(loginFake))
            .thenReturn(Single.just(expect))

        val liveData: Resources<ResponseLogin> = sharedViewModel.loginUsers(loginFake).getOrAwaitValue()
        assertEquals(expect, liveData.data)
        assertNotNull(liveData)
        verify(servicesRepository).postLogin(loginFake)

    }

}