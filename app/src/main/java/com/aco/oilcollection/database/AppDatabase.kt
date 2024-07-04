package com.aco.oilcollection.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aco.oilcollection.database.dao.LocationDao
import com.aco.oilcollection.database.dao.OilCollectionRecordDao
import com.aco.oilcollection.database.dao.UserDao
import com.aco.oilcollection.database.entities.Location
import com.aco.oilcollection.database.entities.OilCollectionRecord
import com.aco.oilcollection.database.entities.User

@Database(entities = [OilCollectionRecord::class, User::class, Location::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oilCollectionRecordDao(): OilCollectionRecordDao
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao
}
