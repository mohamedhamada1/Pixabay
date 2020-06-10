package uae.enbd.pixabay.repository.remote

import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uae.enbd.pixabay.models.Hit
import uae.enbd.pixabay.models.PixabaySearchResponse
import uae.enbd.pixabay.repository.Status
import uae.enbd.pixabay.repository.api.PixabayService
import uae.enbd.pixabay.repository.local.HitDao
import uae.enbd.pixabay.utils.StatusAndError
import java.util.concurrent.Executors
import javax.inject.Inject

class HitBoundaryCallback  constructor(
    val pixabayService: PixabayService, private val hitDao: HitDao, val query: String? = null
) :
    PagedList.BoundaryCallback<Hit>() {

    private val executor = Executors.newSingleThreadExecutor()
    val helper = PagingRequestHelper(executor)
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            searchRepo(page = 1, size = 20,pagingRequstHelper = it) {

            }

        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Hit) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            searchRepo(page = itemAtEnd.pageIndex + 1, size = 20,pagingRequstHelper = it) {

            }

        }
    }

    private fun searchRepo(page: Int, size: Int,pagingRequstHelper: PagingRequestHelper.Request.Callback, onResult: (List<Hit>) -> Unit) {
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
                    executor.execute {
                        listing?.let {
                            listing.forEach {
                                it.query = query
                                it.pageIndex = page
                            }
                            hitDao.insert(listing)
                            pagingRequstHelper.recordSuccess()
                        }
                    }
                    onResult(listing ?: listOf())
                }

                override fun onFailure(call: Call<PixabaySearchResponse>, t: Throwable) {
                    onResult(listOf())
                    pagingRequstHelper.recordFailure(t)
                }
            })
    }
}