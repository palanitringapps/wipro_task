package com.samplewp.repo

import com.samplewp.api.OperationCallback

interface DataSource {
    fun getFeeds(callback: OperationCallback)
    fun cancel()
}