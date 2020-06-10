package uae.enbd.pixabay.repository.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.repository.api.PixabayService
import uae.enbd.pixabay.repository.local.HitDao
import javax.inject.Inject


class PhotoSearchDataSourceFactory @Inject constructor(
    val pixabayService: PixabayService, private val hitDao: HitDao
) : DataSource.Factory<Int, Hit>() {
    val dataSourceLiveData = MutableLiveData<PhotoSearchDataSource>()

    private var dataSource: PhotoSearchDataSource? = null

    var query: String? = null

    override fun create(): DataSource<Int, Hit> {
        dataSource = PhotoSearchDataSource(pixabayService, query, hitDao)
        dataSource?.query = query
        dataSourceLiveData.postValue(dataSource)
        return dataSource!!
    }

    fun updateQuery(query: String?) {
        this.query = query
    }


}