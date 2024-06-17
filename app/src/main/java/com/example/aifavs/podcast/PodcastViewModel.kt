package com.example.aifavs.podcast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aifavs.PodcastInfo
import com.example.aifavs.RemoteApi
import com.example.aifavs.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PodcastViewModel : ViewModel() {
    private val TAG = "PodcastViewModel"
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _podcasts: MutableLiveData<List<PodcastInfo>>  = MutableLiveData()
    val podcasts: LiveData<List<PodcastInfo>> get() = _podcasts

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }


    fun fetchData() {
        _loading.postValue(true)
        val disposable = remoteApi.getPodcastList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { _loading.postValue(false) }
            .subscribe({ result ->
                result?.data?.let {
                    _podcasts.postValue(it)
                }
            }, { throwable ->
                throwable.message?.let {
                    Log.w(TAG, it)
                }
            })
    }
}