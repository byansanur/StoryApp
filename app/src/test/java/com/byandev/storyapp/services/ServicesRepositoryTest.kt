package com.byandev.storyapp.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import com.byandev.storyapp.utils.FakerListStory
import com.byandev.storyapp.utils.FakerResponseBase
import com.byandev.storyapp.utils.FakerResponseLogin
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.io.IOException

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class ServicesRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var services: ApiServices
    private lateinit var servicesRepository: ServicesRepository

    private lateinit var fakerResponseBase: FakerResponseBase
    private lateinit var fakerResponseLogin: FakerResponseLogin
    private lateinit var fakerResponseStory: FakerListStory

    @Before
    fun setUp() {
        services = mock(ApiServices::class.java)
        servicesRepository = mock(ServicesRepository::class.java)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { Schedulers.trampoline() }

        fakerResponseBase = FakerResponseBase
        fakerResponseLogin = FakerResponseLogin
        fakerResponseStory = FakerListStory
    }

    @Test
    fun `When successfully registering, make sure to return Success`() = runTest {
        val registerFake = Register(name = "abcdefgh", email = "abcdefgh@mail.co.id", password = "123456")
        val response = fakerResponseBase.responseBaseSuccessfulFaker
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


    @Test
    fun `When register fails, make sure to return an Error with a message from the API`() = runTest {
        val registerFake = Register(name = "abcdefgh", email = "abcdefgmai", password = "123456")
        val response = fakerResponseBase.responseBaseErrorFaker
        Mockito
            .`when`(services.postRegisterNewUser(registerFake))
            .thenReturn(Single.just(response).doOnError { IOException(response.message) })

        servicesRepository.postRegister(registerFake)

        val observer = services.postRegisterNewUser(registerFake)
        val testObserver = TestObserver.create<ResponseBase>()
        observer.subscribe(testObserver)

        testObserver.awaitCount(1)
        testObserver.assertComplete()
        testObserver.assertResult(response)

        verify(servicesRepository).postRegister(registerFake)
    }

    @Test
    fun `When successfully login, make sure to return Success`() = runTest {
        val loginFake = Login(email = "abcdefgh@mail.co.id", password = "123456")
        val response = fakerResponseLogin.generateLoginFake()
        Mockito
            .`when`(services.postLoginUser(loginFake))
            .thenReturn(Single.just(response))

        servicesRepository.postLogin(loginFake)

        val observer = services.postLoginUser(loginFake)
        val testObserver = TestObserver.create<ResponseLogin>()
        observer.subscribe(testObserver)

        testObserver.awaitCount(1)
        testObserver.assertComplete()
        testObserver.assertResult(response)

        verify(servicesRepository).postLogin(loginFake)
    }

    @Test
    fun `When login fails, make sure to return an Error with a message from the API`() = runTest {
        val loginFake = Login(email = "abcdefgmai", password = "")
        val response = fakerResponseLogin.generateLoginErrorFake()
        Mockito
            .`when`(services.postLoginUser(loginFake))
            .thenReturn(Single.just(response).doOnError { IOException(response.message) })

        servicesRepository.postLogin(loginFake)

        val observer = services.postLoginUser(loginFake)
        val testObserver = TestObserver.create<ResponseLogin>()
        observer.subscribe(testObserver)

        testObserver.awaitCount(1)
        testObserver.assertComplete()
        testObserver.assertResult(response)

        verify(servicesRepository).postLogin(loginFake)
    }

    @Test
    fun `When successful get List story, Make sure data is not null`() = runTest {
        launch {
            val listMock = fakerResponseStory.generateListStory()
            Mockito
                .`when`(services.getAllStories(1, 10, 0))
                .thenReturn(listMock)
            val expected = PagingSource.LoadResult.Page(
                data = listMock.listStory,
                prevKey = null,
                nextKey = 1
            )
            Assert.assertNotNull(expected.data)
        }
    }

    @Test
    fun `Make sure the expected size data is correct from the actual response`() = runTest {
        launch {
            val listMock = fakerResponseStory.generateListStory()

            Mockito
                .`when`(services.getAllStories(1, 10, 0))
                .thenReturn(listMock)

            val expected = PagingSource.LoadResult.Page(
                data = listMock.listStory,
                prevKey = null,
                nextKey = 1
            )
            Assert.assertEquals(
                expected.data.size,
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 11,
                    placeholdersEnabled = true
                ).loadSize
            )
        }
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