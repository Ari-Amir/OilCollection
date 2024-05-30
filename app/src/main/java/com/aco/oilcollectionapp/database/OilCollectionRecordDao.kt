package com.aco.oilcollectionapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OilCollectionRecordDao {
    @Insert
    suspend fun insert(record: OilCollectionRecord)

    @Query("SELECT * FROM oil_collection_records")
    fun getAllRecords(): Flow<List<OilCollectionRecord>>
}

