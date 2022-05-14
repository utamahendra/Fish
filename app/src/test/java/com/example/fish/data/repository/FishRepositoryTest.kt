package com.example.fish.data.repository

import com.example.fish.data.remote.FishApiService
import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FishRepositoryTest : KoinTest {

    @Mock
    lateinit var apiService: FishApiService

    private val repository by inject<FishRepository>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single { FishRepository(apiService) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `on get fish list success`() {
        runTest {
            val expectedResult = listOf(FistListResponse("1", "", "", "", "", "", "", ""))
            BDDMockito.given(apiService.getFishList()).willReturn(expectedResult)
            val actual = repository.getFishList()
            assertEquals(expectedResult, actual)
        }
    }


    @Test
    fun `on get option area success`() {
        runTest {
            val expectedResult = listOf(OptionAreaResponse("1", "2"))
            BDDMockito.given(apiService.getOptionArea()).willReturn(expectedResult)
            val actual = repository.getOptionArea()
            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `on get option size success`() {
        runTest {
            val expectedResult = listOf(OptionSizeResponse("1"))
            BDDMockito.given(apiService.getOptionSize()).willReturn(expectedResult)
            val actual = repository.getOptionSize()
            assertEquals(expectedResult, actual)
        }
    }
}