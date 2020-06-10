package uae.enbd.pixabay.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(primaryKeys = ["id"])
data class Hit(
    val id: Long,
    val tags: String,
    val previewURL: String,
    val largeImageURL: String,
    val likes: Int,
    val comments: Int
) : Parcelable {
    var query: String? = null
    var pageIndex: Int = -1
}