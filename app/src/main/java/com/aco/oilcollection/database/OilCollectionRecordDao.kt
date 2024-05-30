package com.aco.oilcollection.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OilCollectionRecordDao {
    @Insert
    suspend fun insert(record: OilCollectionRecord)

    @Query("SELECT * FROM oil_collection_records ORDER BY dateTime DESC")
    fun getAllRecords(): Flow<List<OilCollectionRecord>>

    @Query("SELECT * FROM oil_collection_records WHERE dateTime BETWEEN :startOfDay AND :endOfDay")
    fun getRecordsForToday(startOfDay: Long, endOfDay: Long): Flow<List<OilCollectionRecord>>

}

