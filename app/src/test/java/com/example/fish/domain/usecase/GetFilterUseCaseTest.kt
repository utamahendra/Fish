package com.example.fish.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fish.common.ErrorCode
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse
import com.example.fish.domain.Either
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.source.FishDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
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

class GetFilterUseCaseTest: KoinTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val useCase by inject<GetFilterUseCase>()

    @Mock
    private lateinit var repository: FishDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(module {
                single {
                    GetFilterUseCaseImpl(repository) as GetFilterUseCase
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
    fun `get area and size list empty`() {
        runTest {
            val expected = Either.Success(FilterData(listOf(), listOf(), listOf()))
            BDDMockito.given(repository.getOptionArea()).willReturn(listOf())
            BDDMockito.given(repository.getOptionSize()).willReturn(listOf())

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `get area list empty`() {
        runTest {
            val size = listOf(OptionSizeResponse("1"))
            val expected = Either.Success(FilterData(listOf(), listOf(), listOf("1")))
            BDDMockito.given(repository.getOptionArea()).willReturn(listOf())
            BDDMockito.given(repository.getOptionSize()).willReturn(size)

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `get size list empty`() {
        runTest {
            val area = listOf(OptionAreaResponse("1", "2"))
            val expected = Either.Success(FilterData(listOf("2"), listOf("1"), listOf()))
            BDDMockito.given(repository.getOptionArea()).willReturn(area)
            BDDMockito.given(repository.getOptionSize()).willReturn(listOf())

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `get size and area success`() {
        runTest {
            val area = listOf(OptionAreaResponse("1", "2"))
            val size = listOf(OptionSizeResponse("1"))
            val expected = Either.Success(FilterData(listOf("2"), listOf("1"), listOf("1")))
            BDDMockito.given(repository.getOptionArea()).willReturn(area)
            BDDMockito.given(repository.getOptionSize()).willReturn(size)

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `internal server error get users`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR))
            BDDMockito.given(repository.getOptionArea()).willAnswer { throw RuntimeException() }
            BDDMockito.given(repository.getOptionSize()).willAnswer { throw RuntimeException() }

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `IO error get users`() {
        runTest {
            val expected = Either.Fail(ViewError(ErrorCode.GLOBAL_INTERNET_ERROR))
            BDDMockito.given(repository.getOptionArea()).willAnswer { throw IOException() }
            BDDMockito.given(repository.getOptionSize()).willAnswer { throw IOException() }

            val actual = useCase.invoke(Unit)

            Assert.assertEquals(expected, actual)
        }
    }
}