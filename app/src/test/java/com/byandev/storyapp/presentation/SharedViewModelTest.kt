package com.byandev.storyapp.presentation

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.paging.PagingData
//import com.byandev.storyapp.data.model.*
//import com.byandev.storyapp.services.ServicesRepository
//import com.byandev.storyapp.utils.*
//import io.reactivex.rxjava3.core.Single
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertNotNull
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.Mockito
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.verify
//import java.io.File

//@RunWith(JUnit4::class)
//@ExperimentalCoroutinesApi
//class SharedViewModelTest {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @get:Rule
//    val mainDispatcherRule = MainDispatcherRule()
//
//    private lateinit var servicesRepository: ServicesRepository
//
//    private lateinit var sharedViewModel: SharedViewModel
//
//    private lateinit var fakeResponseBase: FakerResponseBase
//    private lateinit var fakeResponseLogin: FakerResponseLogin
//    private lateinit var fakeResponseStory: FakerListStory
//
//    @Before
//    fun setUp() {
//        servicesRepository = mock(ServicesRepository::class.java)
//        sharedViewModel = SharedViewModel(servicesRepository,)
//
//        fakeResponseBase = FakerResponseBase
//        fakeResponseLogin = FakerResponseLogin
//        fakeResponseStory = FakerListStory
//    }
//
//    @Test
//    fun ` When successfully registering, ensure registration returns Not Null`() {
//        val registerFake = Register(name = "aaa", email = "aaa@mail.com", password = "123456")
//        val expect = fakeResponseBase.responseBaseSuccessfulFaker
//
//        Mockito
//            .`when`(servicesRepository.postRegister(registerFake))
//            .thenReturn(Single.just(expect))
//
//        val actual: Resources<ResponseBase> = sharedViewModel.registerUsers(registerFake).getOrAwaitValue()
//
//        assertNotNull(actual.data)
//        verify(servicesRepository).postRegister(registerFake)
//
//    }
//
//    @Test
//    fun ` When successfully login, ensure registration returns Not Null`() {
//        val loginFake = Login(email = "aaa@mail.com", password = "123456")
//        val expect = fakeResponseLogin.generateLoginFake()
//
//        Mockito
//            .`when`(servicesRepository.postLogin(loginFake))
//            .thenReturn(Single.just(expect))
//
//        val actual: Resources<ResponseLogin> = sharedViewModel.loginUsers(loginFake).getOrAwaitValue()
//
//        assertNotNull(actual.data)
//        verify(servicesRepository).postLogin(loginFake)
//
//    }
//
//    @Test
//    fun ` When successfully get story, ensure story returns Not Null`() = runTest {
//        launch {
//            val expected = fakeResponseStory.generateListStory()
//
//            val returnData: Flow<PagingData<Story>> = flow {
//                emit(PagingData.from(expected.listStory))
//            }
//
//            Mockito
//                .`when`(servicesRepository.getListStory(0))
//                .thenReturn(returnData)
//
//            val actual : Flow<PagingData<Story>> = sharedViewModel.getListStory(0)
//
//            assertNotNull(actual.collectLatest { it })
//            verify(servicesRepository).getListStory(0)
//        }
//
//    }
//
//    @Test
//    fun `When successfully get story location, ensure story returns Not Null`() {
//        val expected = fakeResponseStory.generateListStory()
//
//        Mockito
//            .`when`(servicesRepository.getStoryLocation())
//            .thenReturn(Single.just(expected))
//
//        val actual: Resources<ResponseAllStories> = sharedViewModel.getListStoryLocation().getOrAwaitValue()
//
//        assertNotNull(actual.data)
//        verify(servicesRepository).getStoryLocation()
//    }
//
//    @Test
//    fun `When successfully post story, ensure story returns Not Null`() {
//        val expected = fakeResponseBase.responseBaseSuccessfulFaker
//        val filePhoto = convertRequestBody(File("app/src/main/res/drawable/ic_dicoding.webp"))
//        val description = convertRequestBody("description")
//
//        Mockito
//            .`when`(servicesRepository.postStories(description, filePhoto, null, null))
//            .thenReturn(Single.just(expected))
//
//        val actual: Resources<ResponseBase> = sharedViewModel
//            .postStories(description, filePhoto, null, null).getOrAwaitValue()
//
//        assertNotNull(actual.data)
//        verify(servicesRepository).postStories(description, filePhoto, null, null)
//    }
//}