package com.samplewp.viewmodel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.samplewp.api.OperationCallback
import com.samplewp.model.Row
import com.samplewp.model.FeedsModel
import com.samplewp.repo.FeedRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class FeedsViewModelTest {

    @Mock
    private lateinit var repository: FeedRepository
    @Mock
    private lateinit var context: Application

    @Captor
    private lateinit var operationCallbackCaptor: ArgumentCaptor<OperationCallback>

    private lateinit var viewModel: FeedsViewModel

    private lateinit var isViewLoadingObserver: Observer<Boolean>
    private lateinit var onMessageErrorObserver: Observer<Any>
    private lateinit var emptyListObserver: Observer<Boolean>
    private lateinit var sampleResponseObserver: Observer<FeedsModel>
    private lateinit var row: ArrayList<Row>

    private lateinit var feedEmptyList: FeedsModel
    private lateinit var feedList: FeedsModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`<Context>(context.applicationContext).thenReturn(context)

        viewModel = FeedsViewModel(context, repository)
        feedEmptyList = FeedsModel(ArrayList(), "demo")

        mockData()
        setupObservers()
    }

    @Test
    fun ListRepositoryAndViewModel() {

        with(viewModel) {
            isViewLoading.observeForever(isViewLoadingObserver)
            feedResponse.observeForever(sampleResponseObserver)
        }
        viewModel.getNewsFeeds(false)
        verify(repository, times(1)).getFeeds(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onSuccess(feedList)

        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertEquals(viewModel.feedResponse.value?.rows?.size, 3)
    }

    @Test
    fun FailRepositoryAndViewModel() {
        with(viewModel) {
            isViewLoading.observeForever(isViewLoadingObserver)
            onMessageError.observeForever(onMessageErrorObserver)
        }
        viewModel.getNewsFeeds(false)
        verify(repository, times(1)).getFeeds(capture(operationCallbackCaptor))
        operationCallbackCaptor.value.onError("Response error")
        Assert.assertNotNull(viewModel.isViewLoading.value)
        Assert.assertTrue(viewModel.onMessageError.value.toString().equals("Response error"))
    }

    private fun setupObservers() {
        isViewLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        onMessageErrorObserver = mock(Observer::class.java) as Observer<Any>
        emptyListObserver = mock(Observer::class.java) as Observer<Boolean>
        sampleResponseObserver = mock(Observer::class.java) as Observer<FeedsModel>
    }


    private fun mockData() {

        row = ArrayList()

        row.add(
            Row(
                "Museo Nacional de Arqueología, Antropología e Historia del Perú",
                "sample",
                "mock"
            )
        )
        row.add(
            Row(
                "Museo Nacional de Arqueología, Antropología e Historia del Perú",
                "sample",
                "mock"
            )
        )
        row.add(
            Row(
                "Museo Nacional de Arqueología, Antropología e Historia del Perú",
                "sample",
                "mock"
            )
        )
        feedList = FeedsModel(row, "Sample title")
    }

}