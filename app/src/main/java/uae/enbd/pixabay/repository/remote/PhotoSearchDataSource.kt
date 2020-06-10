package uae.enbd.pixabay.repository.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.models.PixabaySearchResponse
import uae.enbd.pixabay.repository.Status
import uae.enbd.pixabay.repository.api.PixabayService
import uae.enbd.pixabay.utils.StatusAndError

class PhotoSearchDataSource constructor(
    private val pixabayService: PixabayService,
    var query: String? = null
) : PageKeyedDataSource<Int, Hit>() {


    var state: MutableLiveData<StatusAndError> = MutableLiveData()
    val FIRST_PAGE_INDEX = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Hit>
    ) {
        state.postValue(StatusAndError(Status.LOADING, null))
        searchRepo(page = FIRST_PAGE_INDEX, size = params.requestedLoadSize) {
            callback.onResult(it, null, FIRST_PAGE_INDEX + 1)
            state.postValue(StatusAndError(Status.SUCCESS, null))
        }
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searchRepo(page = params.key, size = params.requestedLoadSize) {
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Hit>) {
        searchRepo(page = FIRST_PAGE_INDEX, size = params.requestedLoadSize) {
            val key = if (params.key > FIRST_PAGE_INDEX) params.key - 1 else FIRST_PAGE_INDEX
            callback.onResult(it, key)
        }
    }

    private fun searchRepo(page: Int, size: Int, onResult: (List<Hit>) -> Unit) {
        if (query.isNullOrBlank()) {
            onResult(listOf())
            return
        }

        pixabayService.searchRepos(query = query ?: "", perPage = size, page = page)
            .enqueue(object : Callback<PixabaySearchResponse> {

                override fun onResponse(
                    call: Call<PixabaySearchResponse>,
                    response: Response<PixabaySearchResponse>
                ) {

                    val listing = response.body()?.hits
                    onResult(listing ?: listOf())
                }

                override fun onFailure(call: Call<PixabaySearchResponse>, t: Throwable) {
                    onResult(listOf())
                    state.postValue(StatusAndError(Status.ERROR, t))
                }
            })
    }

}