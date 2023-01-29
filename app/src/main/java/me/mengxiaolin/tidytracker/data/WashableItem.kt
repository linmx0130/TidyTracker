package me.mengxiaolin.tidytracker.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/// Describe a washable item
@Entity(tableName="washable_items")
data class WashableItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name="id") val id:Int,
    // the name of the item
    val name: String,
    // last time of cleaning in UTC timestamp
    val lastCleanTime: Long,
    // the time period to wait until next cleaning
    val expireTime: Long
)