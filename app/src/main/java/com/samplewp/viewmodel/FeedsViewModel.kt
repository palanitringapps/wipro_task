package com.samplewp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samplewp.api.OperationCallback
import com.samplewp.base.BaseViewModel
import com.samplewp.model.Row
import com.samplewp.model.SampleResponse
import com.samplewp.repo.FeedRepository

open class FeedsViewModel(app: Application, val repository: FeedRepository) : BaseViewModel(app) {


    var _sampleResponse = MutableLiveData<SampleResponse>().apply {
        value = SampleResponse(
            ArrayList(), "data"
        )
    }

    var response: LiveData<SampleResponse> = _sampleResponse

    val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()

    init {
        refreshFeed()
    }

    fun refreshFeed() {

        _isViewLoading.postValue(true)

        repository.getFeeds(object : OperationCallback {
            override fun onSuccess(obj: Any?) {

                _isViewLoading.postValue(false)

                if (obj != null && obj is SampleResponse) _sampleResponse.value = obj
                else {
                    _isEmptyList.postValue(true)
                }
            }

            override fun onError(obj: Any?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(obj)
            }

        })
    }

    class FeedsViewModelFactory(private var app: Application, private var repo: FeedRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java, FeedRepository::class.java)
                .newInstance(app, repo)
        }
    }
}