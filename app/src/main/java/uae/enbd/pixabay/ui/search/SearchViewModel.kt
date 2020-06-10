package uae.enbd.pixabay.ui.search

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.repository.remote.PhotoSearchDataSource
import uae.enbd.pixabay.repository.remote.PhotoSearchDataSourceFactory
import uae.enbd.pixabay.utils.StatusAndError
import javax.inject.Inject


class SearchViewModel @Inject constructor(val photoSearchRepoFactory: PhotoSearchDataSourceFactory) :
    ViewModel() {

    var hitList: LiveData<PagedList<Hit>>? = null

    fun setupPaging() {

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(20)
            .setEnablePlaceholders(false)
            .build()

        hitList = LivePagedListBuilder<Int, Hit>(
            photoSearchRepoFactory,
            config
        ).build()
    }

    fun invalide() {
        photoSearchRepoFactory.dataSourceLiveData.value?.invalidate()
    }

    fun research(query: String?) = query?.let {
        photoSearchRepoFactory.updateQuery(query)
        invalide()
    }

    fun listIsEmpty(): Boolean {
        return hitList == null ||  hitList?.value.isNullOrEmpty()
    }

    fun getState(): LiveData<StatusAndError> =
        Transformations.switchMap<PhotoSearchDataSource, StatusAndError>(
            photoSearchRepoFactory.dataSourceLiveData,
            PhotoSearchDataSource::state
        )
}