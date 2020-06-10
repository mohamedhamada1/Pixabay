package uae.enbd.pixabay.repository.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uae.enbd.pixabay.models.Hit

@Dao
interface HitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Hit>)

    @Query("SELECT * FROM Hit WHERE `query` LIKE :tags")
    fun hits(tags: String): DataSource.Factory<Int, Hit>
}