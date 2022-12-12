package com.byandev.storyapp.services

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.paging.PagingSource
//import com.byandev.storyapp.data.model.*
//import com.byandev.storyapp.utils.*
//import io.reactivex.rxjava3.core.Single
//import io.reactivex.rxjava3.observers.TestObserver
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.Mockito
//import org.mockito.Mockito.mock
//import java.io.File
//import java.io.IOException
//
//@RunWith(JUnit4::class)
//@ExperimentalCoroutinesApi
//class ServicesRepositoryTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @get:Rule
//    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()
//
//    private lateinit var services: ApiServices
//    private lateinit var servicesRepository: ServicesRepository
//
//    private lateinit var fakerResponseBase: FakerResponseBase
//    private lateinit var fakerResponseLogin: FakerResponseLogin
//    private lateinit var fakerResponseStory: FakerListStory
//
//    @Before
//    fun setUp() {
//        services = mock(ApiServices::class.java)
//        servicesRepository = ServicesRepository(services)
//
//        fakerResponseBase = FakerResponseBase
//        fakerResponseLogin = FakerResponseLogin
//        fakerResponseStory = FakerListStory
//    }
//
//    @Test
//    fun `When successfully registering, make sure to return Success`() = runTest {
//        val registerFake = Register(name = "abcdefgh", email = "abcdefgh@mail.co.id", password = "123456")
//        val response = fakerResponseBase.responseBaseSuccessfulFaker
//        Mockito
//            .`when`(services.postRegisterNewUser(registerFake))
//            .thenReturn(Single.just(response))
//
//        val actualData = servicesRepository.postRegister(registerFake).blockingGet()
//
//        val observer = services.postRegisterNewUser(registerFake)
//        val testObserver = TestObserver.create<ResponseBase>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(response, actualData)
//    }
//
//    @Test
//    fun `When register fails, make sure to return an Error with a message from the API`() = runTest {
//        val registerFake = Register(name = "abcdefgh", email = "abcdefgmai", password = "123456")
//        val response = fakerResponseBase.responseBaseErrorFaker
//        Mockito
//            .`when`(services.postRegisterNewUser(registerFake))
//            .thenReturn(Single.just(response).doOnError { IOException(response.message) })
//
//        val actualData = servicesRepository.postRegister(registerFake).blockingGet()
//
//        val observer = services.postRegisterNewUser(registerFake)
//        val testObserver = TestObserver.create<ResponseBase>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(response, actualData)
//    }
//
//    @Test
//    fun `When successfully login, make sure to return Success`() = runTest {
//        val loginFake = Login(email = "abcdefgh@mail.co.id", password = "123456")
//        val response = fakerResponseLogin.generateLoginFake()
//        Mockito
//            .`when`(services.postLoginUser(loginFake))
//            .thenReturn(Single.just(response))
//
//        val actualData = servicesRepository.postLogin(loginFake).blockingGet()
//
//        val observer = services.postLoginUser(loginFake)
//        val testObserver = TestObserver.create<ResponseLogin>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(response, actualData)
//    }
//
//    @Test
//    fun `When login fails, make sure to return an Error with a message from the API`() = runTest {
//        val loginFake = Login(email = "abcdefgmai", password = "")
//        val response = fakerResponseLogin.generateLoginErrorFake()
//        Mockito
//            .`when`(services.postLoginUser(loginFake))
//            .thenReturn(Single.just(response).doOnError { IOException(response.message) })
//
//        val actualData = servicesRepository.postLogin(loginFake).blockingGet()
//
//        val observer = services.postLoginUser(loginFake)
//        val testObserver = TestObserver.create<ResponseLogin>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(response, actualData)
//    }
//
//    @Test
//    fun `When successful get List story, Make sure data is not null`() = runTest {
//        launch {
//            val listMock = fakerResponseStory.generateListStory()
//            Mockito
//                .`when`(services.getAllStories(1, 10, 0))
//                .thenReturn(listMock)
//            val expected = PagingSource.LoadResult.Page(
//                data = listMock.listStory,
//                prevKey = null,
//                nextKey = 1
//            )
//            Assert.assertNotNull(expected.data)
//        }
//    }
//
//    @Test
//    fun `When successful get List story,Make sure the expected size data is correct`() = runTest {
//        launch {
//            val listMock = fakerResponseStory.generateListStory()
//
//            Mockito
//                .`when`(services.getAllStories(1, 10, 0))
//                .thenReturn(listMock)
//
//            val expected = PagingSource.LoadResult.Page(
//                data = listMock.listStory,
//                prevKey = null,
//                nextKey = 1
//            )
//            Assert.assertEquals(
//                expected.data.size,
//                PagingSource.LoadParams.Refresh(
//                    key = 0,
//                    loadSize = 11,
//                    placeholdersEnabled = true
//                ).loadSize
//            )
//        }
//    }
//
//    @Test
//    fun `When successful get List story location, Make sure data is not null`() = runTest {
//        val expectedStory = FakerListStory.generateListStory()
//        Mockito
//            .`when`(services.getStoriesLocation())
//            .thenReturn(Single.just(expectedStory))
//
//        val actualData = servicesRepository.getStoryLocation().blockingGet()
//
//        val observer = services.getStoriesLocation(1)
//        val testObserver = TestObserver.create<ResponseAllStories>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(expectedStory, actualData)
//    }
//
//    @Test
//    fun `When successful get List story location,Make sure the expected size data is correct`() = runTest {
//        val expectedStory = FakerListStory.generateListStory()
//        Mockito
//            .`when`(services.getStoriesLocation())
//            .thenReturn(Single.just(expectedStory))
//
//        val actualData = servicesRepository.getStoryLocation().blockingGet()
//
//        val observer = services.getStoriesLocation(1)
//        val testObserver = TestObserver.create<ResponseAllStories>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(actualData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(expectedStory.listStory.size, actualData.listStory.size)
//    }
//
//    @Test
//    fun `When successful Post story, Make sure post story data is not null`() = runTest {
//        val filePhoto = convertRequestBody(File("app/src/main/res/drawable/ic_dicoding.webp"))
//        val description = convertRequestBody("description")
//        val expectedData = fakerResponseBase.responseBaseSuccessfulFaker
//        Mockito
//            .`when`(services.postStories(description, filePhoto, null, null))
//            .thenReturn(Single.just(expectedData))
//
//        val actualData = servicesRepository.postStories(
//            description = description,
//            photo = filePhoto,
//            lat = null, lon = null
//        ).blockingGet()
//
//        val observer = services.postStories(description, filePhoto, null, null)
//        val testObserver = TestObserver.create<ResponseBase>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(expectedData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(expectedData, actualData)
//    }
//
//    @Test
//    fun `When successful Post story with location, Make sure post story data is not null`() = runTest {
//        val filePhoto = convertRequestBody(File("app/src/main/res/drawable/ic_dicoding.webp"))
//        val description = convertRequestBody("description")
//        val lat = convertRequestBody("37.4220936")
//        val lon = convertRequestBody("-122.083922")
//        val expectedData = fakerResponseBase.responseBaseSuccessfulFaker
//        Mockito
//            .`when`(services.postStories(description, filePhoto, lat, lon))
//            .thenReturn(Single.just(expectedData))
//
//        val actualData = servicesRepository.postStories(
//            description = description,
//            photo = filePhoto,
//            lat = lat, lon = lon
//        ).blockingGet()
//
//        val observer = services.postStories(description, filePhoto, lat, lon)
//        val testObserver = TestObserver.create<ResponseBase>()
//        observer.subscribe(testObserver)
//
//        testObserver.awaitCount(1)
//        testObserver.assertComplete()
//        testObserver.assertResult(expectedData)
//
//        Assert.assertNotNull(actualData)
//        Assert.assertEquals(expectedData, actualData)
//    }
//
//}