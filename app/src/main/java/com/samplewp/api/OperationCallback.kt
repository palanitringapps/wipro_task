package com.samplewp.api

interface OperationCallback {
    fun onSuccess(obj:Any?)
    fun onError(obj:Any?)
}