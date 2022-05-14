package com.example.fish.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fish.common.ErrorCode
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.domain.Either
import com.example.fish.domain.model.FishData
import com.example.fish.domain.source.FishDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class GetFishListUseCaseTest : KoinTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val useCase by inject<GetFishListUseCase>()

    @Mock
    private lateinit var repository: FishDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single {
                    GetFishListUseCaseImpl(repository) as GetFishListUseCase
                }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `get fish list empty`() {
        runTest {
            val expected = Either.Success(listOf<FishData>())
            BDDMockito.given(repository.getFishList())
                .willReturn(listOf(FistListResponse("1", "", "", "", "", "", "", "")))

            val actual = useCase.invoke(Unit)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `get fish list success`() {
        runTest {
            val expected = Either.Success(listOf(FishData("1", "2", "3", "4", "5", "6")))
            BDDMockito.given(repository.getFishList())
                .willReturn(listOf(FistListResponse("1", "2", "3", "4", "5", "6", "7", "8")))

            val actual = useCase.invoke(Unit)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `internal server error get users`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR))
            BDDMockito.given(repository.getFishList()).willAnswer { throw RuntimeException() }

            val actual = useCase.invoke(Unit)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `IO error get users`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_INTERNET_ERROR))
            BDDMockito.given(repository.getFishList()).willAnswer { throw IOException() }

            val actual = useCase.invoke(Unit)

            assertEquals(expected, actual)
        }
    }
}