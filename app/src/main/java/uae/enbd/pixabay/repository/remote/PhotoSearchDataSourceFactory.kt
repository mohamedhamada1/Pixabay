package uae.enbd.pixabay.repository.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.repository.api.PixabayService
import javax.inject.Inject


class PhotoSearchDataSourceFactory @Inject constructor(
    val pixabayService: PixabayService
) : DataSource.Factory<Int, Hit>() {
    val dataSourceLiveData = MutableLiveData<PhotoSearchDataSource>()

    private var dataSource: PhotoSearchDataSource? = null

    var query: String? = null

    override fun create(): DataSource<Int, Hit> {
        dataSource = PhotoSearchDataSource(pixabayService)
        dataSource?.query = query
        dataSourceLiveData.postValue(dataSource)
        return dataSource!!
    }

    fun updateQuery(query: String?) {
        this.query = query
        dataSource?.query = query
    }


}