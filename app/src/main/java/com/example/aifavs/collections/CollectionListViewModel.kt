package com.example.aifavs.collections

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aifavs.Collection
import com.example.aifavs.CreatePodcastRequestBody
import com.example.aifavs.RemoteApi
import com.example.aifavs.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CollectionListViewModel: ViewModel() {
    private val TAG = "CollectionListViewModel"
    private val _collectionList: MutableLiveData<List<Collection>> = MutableLiveData()
    val collectionList: LiveData<List<Collection>> get() = _collectionList

    private val _refreshingList: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshingList: LiveData<Boolean> get() = _refreshingList

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }
    
    private var getContentListDisposable: Disposable? = null
    private var createPodcastDisposable: Disposable? = null

    fun getContentList(
        categoryId: String? = null,
        tagId: String? = null
    ) {
        // 取消之前的请求
        getContentListDisposable?.dispose()
        
        _refreshingList.postValue(true)
        getContentListDisposable = remoteApi.getCollectionList(categoryId, tagId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                _refreshingList.postValue(false)
            }
            .subscribe({ response ->
                val list = response.data
                Log.d(TAG, "list.size = ${list?.size}")
                list?.let { _collectionList.postValue(it) }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })
    }

    fun createPodcast(collectionId: String, onSuccess: () -> Unit = {}) {
        // 取消之前的请求
        createPodcastDisposable?.dispose()
        
        _loading.postValue(true)
        createPodcastDisposable = remoteApi.createPodcast(CreatePodcastRequestBody(collectionId = collectionId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                _loading.postValue(false)
            }
            .subscribe({ result ->
                if (result.isSuccess()) {
                    onSuccess()
                }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })
    }
    
    override fun onCleared() {
        super.onCleared()
        // 清理资源
        getContentListDisposable?.dispose()
        createPodcastDisposable?.dispose()
    }
}