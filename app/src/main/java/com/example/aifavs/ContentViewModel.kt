package com.example.aifavs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContentViewModel: ViewModel() {
    private val TAG = "ContentViewModel"
    val contentList: MutableLiveData<List<ContentItem>> = MutableLiveData()

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }

    fun getContentList() {
        val disposable = remoteApi.getContentList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val list = response.data
                Log.d(TAG, "list.size = ${list?.size}")
                list?.let { contentList.value = it }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })

    }
}