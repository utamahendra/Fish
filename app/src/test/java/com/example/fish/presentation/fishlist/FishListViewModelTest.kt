package com.example.fish.presentation.fishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fish.common.ErrorCode
import com.example.fish.common.viewstate.ListViewState
import com.example.fish.common.viewstate.ViewError
import com.example.fish.domain.Either
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.model.FishData
import com.example.fish.domain.usecase.GetFilterUseCase
import com.example.fish.domain.usecase.GetFishListUseCase
import com.example.fish.getTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
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

class FishListViewModelTest : KoinTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val viewModel by inject<FishListViewModel>()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var getFilterUseCase: GetFilterUseCase

    @Mock
    private lateinit var getFishListUseCase: GetFishListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)

        startKoin {
            modules(module {
                single { FishListViewModel(getFilterUseCase, getFishListUseCase) }
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
            BDDMockito.given(getFishListUseCase.invoke(Unit))
                .willReturn(Either.Success(listOf(FishData("1", "2", "3", "4", "5", "6"))))
            val expected = listOf(
                ListViewState.Loading(),
                ListViewState.Success(listOf(FishData("1", "2", "3", "4", "5", "6"))))
            val actual = viewModel.fishesState.getTestObserver().observedValues
            viewModel.getFishes()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get fish list error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(getFishListUseCase.invoke(Unit)).willReturn(Either.Fail(error))
            val expected = listOf<ListViewState<List<FishData>>>(
                ListViewState.Loading(),
                ListViewState.Error(error)
            )
            val actual = viewModel.fishesState.getTestObserver().observedValues
            viewModel.getFishes()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get users empty`() {
        runTest {
            BDDMockito.given(getFishListUseCase.invoke(Unit)).willReturn(Either.Success(listOf()))
            val expected = listOf<ListViewState<List<FishData>>>(
                ListViewState.Loading(),
                ListViewState.EmptyData()
            )
            val actual = viewModel.fishesState.getTestObserver().observedValues
            viewModel.getFishes()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get filter success`() {
        runTest {
            BDDMockito.given(getFilterUseCase.invoke(Unit))
                .willReturn(Either.Success(FilterData(listOf("1"), listOf("2"), listOf("3"))))
            val expected = listOf(
                ListViewState.Success(FilterData(listOf("1"), listOf("2"), listOf("3"))))
            val actual = viewModel.filterState.getTestObserver().observedValues
            viewModel.getFilterData()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `on get filter error`() {
        runTest {
            val error = ViewError(ErrorCode.GLOBAL_UNKNOWN_ERROR)
            BDDMockito.given(getFilterUseCase.invoke(Unit)).willReturn(Either.Fail(error))
            val expected = listOf<ListViewState<FilterData>>(
                ListViewState.Error(error)
            )
            val actual = viewModel.filterState.getTestObserver().observedValues
            viewModel.getFilterData()
            assertEquals(expected, actual)
        }
    }
}