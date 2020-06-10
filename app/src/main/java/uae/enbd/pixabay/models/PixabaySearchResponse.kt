package uae.enbd.pixabay.models

data class PixabaySearchResponse(
    val total: Int, val totalHits: Int,
    val hits: List<Hit>
)
{
    var nextPage: Int? = null
}