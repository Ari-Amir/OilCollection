package com.aco.oilcollection.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OilCollectionRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oilCollectionRecordDao(): OilCollectionRecordDao
}
