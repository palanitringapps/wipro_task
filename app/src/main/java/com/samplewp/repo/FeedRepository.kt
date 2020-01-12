package com.samplewp.repo

import com.samplewp.api.ApiClient
import com.samplewp.api.OperationCallback
import com.samplewp.model.FeedsModel
import retrofit2.Call
import retrofit2.Callback

open class FeedRepository :  DataSource {


    private var call: Call<FeedsModel>? = null

    override fun getFeeds(callback: OperationCallback) {
        call = ApiClient.build()?.getFeeds()


        call?.enqueue(object : Callback<FeedsModel> {
            override fun onFailure(call: Call<FeedsModel>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<FeedsModel>,
                response: retrofit2.Response<FeedsModel>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful ) {
                        callback.onSuccess(it)
                    } else {
                        callback.onError("Response error")
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.cancel()
    }
}