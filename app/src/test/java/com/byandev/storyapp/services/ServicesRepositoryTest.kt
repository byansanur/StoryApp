package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.utils.FakerResponseBase
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import retrofit2.Response

@RunWith(JUnit4::class)
class ServicesRepositoryTest {

    private lateinit var services: ApiServices
    private lateinit var servicesRepository: ServicesRepository

    private lateinit var fakerResponseBase: FakerResponseBase

    @Before
    fun setUp() {
        services = mock(ApiServices::class.java)
        servicesRepository = ServicesRepository(services)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

        fakerResponseBase = FakerResponseBase
    }

    @Test
    fun `When successfully registering, make sure to return Success`() {
        val registerFake = Register(name = "abcdefgh", email = "abcdefgh@mail.co.id", password = "123456")
        val response = fakerResponseBase.responseBaseSuccessfulRegisterFaker
        Mockito
            .`when`(services.postRegisterNewUser(registerFake))
            .thenReturn(Single.just(response))

        servicesRepository.postRegister(registerFake)

        val observer = services.postRegisterNewUser(registerFake)
        val testObserver = TestObserver.create<ResponseBase>()
        observer.subscribe(testObserver)

        testObserver.awaitCount(1)
        testObserver.assertComplete()
        testObserver.assertResult(response)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}