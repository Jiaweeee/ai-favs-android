package com.example.aifavs.collections

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aifavs.RemoteApi
import com.example.aifavs.ServiceCreator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: rename this class.
data class DisplayItem(
    val id: String,
    val name: String,
    val itemCount: Int,
    val type: ItemType
)

enum class ItemType {
    FOLDER, TAG
}

class CollectionHomeViewModel : ViewModel() {
    private val TAG = "CollectionsViewModel"
    val folders : MutableLiveData<List<DisplayItem>> = MutableLiveData(emptyList())
    val tags: MutableLiveData<List<DisplayItem>> = MutableLiveData(emptyList())
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val remoteApi: RemoteApi by lazy {
        ServiceCreator.create(RemoteApi::class.java)
    }

    fun fetchData() {
        loading.value = true
        val disposable = remoteApi.getCollectionOverview()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally{
                loading.value = false
            }
            .subscribe({ result ->
                val data = result.data
                if (data != null) {
                    val categories = data.categories
                    if (categories != null) {
                        this.folders.value = categories.map {
                            DisplayItem(
                                id = it.id,
                                name = it.name,
                                itemCount = it.collectionCount,
                                type = ItemType.FOLDER
                            )
                        }
                    }
                    val tags = data.tags
                    if (tags != null) {
                        this.tags.value = tags.map {
                            DisplayItem(
                                id = it.id,
                                name = it.name,
                                itemCount = it.collectionCount,
                                type = ItemType.TAG
                            )
                        }
                    }
                }
            }, { throwable ->
                throwable?.message?.let { Log.e(TAG, it) }
            })
    }
}