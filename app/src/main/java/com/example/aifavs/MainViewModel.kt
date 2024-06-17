package com.example.aifavs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel: ViewModel() {
    private val TAG = "MainViewModel"

    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }

    fun addCollection(url: String?) {
        if (url == null) {
            return // TODO: validate the url
        }
        loading.value = true
        val disposable = remoteApi.addCollection(AddContentRequestBody(url))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loading.value = false
            }
            .subscribe({ _ ->
                // TODO: emit a result msg
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })
    }
}