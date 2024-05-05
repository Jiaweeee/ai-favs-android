package com.example.aifavs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContentViewModel: ViewModel() {
    private val TAG = "ContentViewModel"
    val contentList: MutableLiveData<List<ContentItem>> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }

    fun getContentList() {
        loading.value = true
        val disposable = remoteApi.getContentList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loading.value = false
            }
            .subscribe({ response ->
                val list = response.data
                Log.d(TAG, "list.size = ${list?.size}")
                list?.let { contentList.value = it }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })

    }
}