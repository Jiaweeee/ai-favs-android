package com.example.aifavs.collections

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aifavs.Collection
import com.example.aifavs.RemoteApi
import com.example.aifavs.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

data class Folder(
    val id: String,
    val name: String,
    val itemCount: Int
)

class CollectionsViewModel : ViewModel() {
    private val TAG = "CollectionsViewModel"
    val folders : MutableLiveData<List<Folder>> = MutableLiveData(emptyList())
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }

    fun fetchData() {
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
                list?.let {
                    folders.value = toFolders(it)
                }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })
    }

    private fun toFolders(contentList: List<Collection>): List<Folder> {
        val categories = contentList.filter { it.category != null }.map { it.category }
        // Group categories by id
        val countMap = categories.groupingBy { it!!.id }.eachCount()
        val folders = countMap.map { (categoryId, itemCount) ->
            val categoryName = categories.first { it!!.id == categoryId }!!.name
            Folder(
                id = categoryId,
                name = categoryName,
                itemCount = itemCount
            )
        }
        return folders
    }
}