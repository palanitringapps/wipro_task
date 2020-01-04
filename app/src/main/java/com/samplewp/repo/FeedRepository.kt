package com.samplewp.repo

import com.samplewp.api.ApiClient
import com.samplewp.api.FeedResponse
import com.samplewp.api.OperationCallback
import com.samplewp.model.SampleResponse
import retrofit2.Call
import retrofit2.Callback

open class FeedRepository :  DataSource {


    private var call: Call<SampleResponse>? = null

    override fun getFeeds(callback: OperationCallback) {
        call = ApiClient.build()?.getFeeds()


        call?.enqueue(object : Callback<SampleResponse> {
            override fun onFailure(call: Call<SampleResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(
                call: Call<SampleResponse>,
                response: retrofit2.Response<SampleResponse>
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
        call?.let {
            it.cancel()
        }
    }
}