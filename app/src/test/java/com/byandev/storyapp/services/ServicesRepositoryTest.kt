package com.byandev.storyapp.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.byandev.storyapp.MainDispatcherRule
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.utils.FakeApiServices
import com.byandev.storyapp.utils.FakerListStory
import com.byandev.storyapp.utils.FakerResponseBase
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class ServicesRepositoryTest {

    private lateinit var services: ApiServices
    private lateinit var services2: ApiServices
    private lateinit var servicesRepository: ServicesRepository
    private lateinit var servicesRepository2: ServicesRepository

    private lateinit var fakerResponseBase: FakerResponseBase

    @Before
    fun setUp() {
        services = mock(ApiServices::class.java)
        services2 = FakeApiServices()
        servicesRepository = mock(ServicesRepository::class.java)
        servicesRepository2 = ServicesRepository(services2)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

        fakerResponseBase = FakerResponseBase
    }

    @Test
    fun `When successfully registering, make sure to return Success`() = runTest {
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

        verify(servicesRepository).postRegister(registerFake)
    }


    @Test // bug
    fun `when getStory Should Not Null`() = runTest {
        val expectedStory = FakerListStory.generateListStory()
        Mockito
            .`when`(services2.getAllStories(1, 10, 0))
            .thenReturn(expectedStory)

        val actual = servicesRepository2.getListStory(0)
        Assert.assertNotNull(actual)
    }

    @Test // bug
    fun `when getStoryLocation Should Not Null`() = runTest {
        val expectedStory = FakerListStory.generateListStory()
        Mockito
            .`when`(services.getStoriesLocation())
            .thenReturn(Single.just(expectedStory))
        var actualSize = 0
        val actual = servicesRepository.getStoryLocation().test().assertValue {
            actualSize = it.listStory.size
            true
        }
        Assert.assertNotNull(actual)
        Assert.assertEquals(expectedStory.listStory.size, actualSize)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}