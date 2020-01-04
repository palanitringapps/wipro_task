package com.samplewp.api

import com.samplewp.model.SampleResponse

data class FeedResponse(val status: Int?, val msg: String?, val data: SampleResponse?) {
    fun isSuccess(): Boolean = (status == 200)
}