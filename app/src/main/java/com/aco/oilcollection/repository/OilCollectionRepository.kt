package com.aco.oilcollection.repository

import com.aco.oilcollection.database.entities.OilCollectionRecord
import com.aco.oilcollection.database.dao.OilCollectionRecordDao
import kotlinx.coroutines.flow.Flow

class OilCollectionRepository(private val oilCollectionRecordDao: OilCollectionRecordDao) {

    suspend fun insertRecord(record: OilCollectionRecord) {
        oilCollectionRecordDao.insert(record)
    }

    fun getAllRecords(): Flow<List<OilCollectionRecord>> {
        return oilCollectionRecordDao.getAllRecords()
    }

    fun getRecordsForToday(startOfDay: Long, endOfDay: Long): Flow<List<OilCollectionRecord>> {
        return oilCollectionRecordDao.getRecordsForToday(startOfDay, endOfDay)
    }

    suspend fun getUserNameById(userId: Int): String? {
        return oilCollectionRecordDao.getUserNameById(userId)
    }
}
