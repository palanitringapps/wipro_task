package com.samplewp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.samplewp.api.OperationCallback
import com.samplewp.base.BaseViewModel
import com.samplewp.model.FeedsModel
import com.samplewp.model.Row
import com.samplewp.repo.FeedRepository

open class FeedsViewModel(app: Application, private val repository: FeedRepository) : BaseViewModel(app) {


    var _feedsModel = MutableLiveData<FeedsModel>().apply {
        value = FeedsModel(
            ArrayList(), "data"
        )
    }
    var filteredRows = ArrayList<Row>()

    var feedResponse: LiveData<FeedsModel> = _feedsModel

    val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()

    fun getNewsFeeds(isPulltoRefresh: Boolean) {


        _isViewLoading.postValue(!isPulltoRefresh)

        repository.getFeeds(object : OperationCallback {
            override fun onSuccess(obj: Any?) {

                _isViewLoading.postValue(false)

                if (obj != null && obj is FeedsModel) _feedsModel.value = obj
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

    fun filterEmptyObjects() {
        for (row in _feedsModel.value!!.rows) {
            if (!(row.description.isNullOrBlank() && row.imageHref.isNullOrBlank() && row.title.isNullOrBlank())) {
                filteredRows.add(row)
            }
        }
    }

    class FeedsViewModelFactory(private var app: Application, private var repo: FeedRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(Application::class.java, FeedRepository::class.java)
                .newInstance(app, repo)
        }
    }
}