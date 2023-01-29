package me.mengxiaolin.tidytracker.data
import androidx.room.*

@Dao
interface WashableItemDao {
    @Query("SELECT * FROM washable_items")
    suspend fun getAll(): List<WashableItem>
    @Query("SELECT * FROM washable_items WHERE id = :itemId")
    suspend fun getById(itemId: Int): List<WashableItem>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WashableItem)
    @Update
    suspend fun update(item: WashableItem)
}