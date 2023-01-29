package me.mengxiaolin.tidytracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WashableItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun washableItemDao(): WashableItemDao
}
