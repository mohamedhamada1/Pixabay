package uae.enbd.pixabay.ui.search

import android.os.Handler
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.repository.Status
import uae.enbd.pixabay.repository.local.PixabayDb
import uae.enbd.pixabay.repository.remote.PhotoSearchDataSource
import uae.enbd.pixabay.repository.remote.PhotoSearchDataSourceFactory
import uae.enbd.pixabay.utils.StatusAndError
import javax.inject.Inject


class SearchViewModel @Inject constructor(
    val photoSearchRepoFactory: PhotoSearchDataSourceFactory,
    val pixabayDB: PixabayDb
) :
    ViewModel() {
    private var searchLive = MutableLiveData<String>()
    private var switchStateLiveData = MutableLiveData<Unit>()
    private var isRemote: Boolean = true

    private val paginationConfig = PagedList.Config.Builder()
        .setPageSize(PAGE_SIZE)
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setEnablePlaceholders(false)
        .build()


    val loadData: LiveData<PagedList<Hit>> =
        Transformations.switchMap(searchLive) { search ->
            switchStateLiveData.postValue(Unit)
            if (isRemote) {
                initializedPagedListBuilderRemote(search)
            } else {
                initializedPagedListBuilderLocal(
                    search
                )
            }

        }


    fun research(query: String?) = query?.let {
        searchLive.value = query
    }

    fun invalidate() {
        searchLive.value = searchLive.value
    }

    fun changeLiveData(isRemote: Boolean) {
        this.isRemote = isRemote
    }

    fun listIsEmpty(): Boolean {
        return loadData.value.isNullOrEmpty()
    }

    private fun initializedPagedListBuilderLocal(query: String)
            : LiveData<PagedList<Hit>> {
        return LivePagedListBuilder<Int, Hit>(
            pixabayDB.hitDao().hits(query),
            paginationConfig
        ).build()
    }

    fun initializedPagedListBuilderRemote(query: String): LiveData<PagedList<Hit>> {
        photoSearchRepoFactory.updateQuery(query)
        return LivePagedListBuilder<Int, Hit>(
            photoSearchRepoFactory,
            paginationConfig
        ).build()
    }


    fun getState(): LiveData<StatusAndError> =
        Transformations.switchMap(switchStateLiveData) {
            if (isRemote)
                Transformations.switchMap(
                    photoSearchRepoFactory.dataSourceLiveData,
                    PhotoSearchDataSource::state
                ) else {
                isRemote = true
                val state: MutableLiveData<StatusAndError> = MutableLiveData()
                state.postValue(StatusAndError(Status.LOADING, null))
                Handler().postDelayed({
                    state.postValue(StatusAndError(Status.SUCCESS, null))
                }, 500)
                state
            }
        }

    companion object {
        const val PAGE_SIZE = 20
    }
}