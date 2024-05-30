package com.aco.oilcollectionapp.database

import kotlinx.coroutines.flow.Flow

class OilCollectionRepository(private val dao: OilCollectionRecordDao) {

    suspend fun insertRecord(record: OilCollectionRecord) {
        dao.insert(record)
    }

    fun getAllRecords(): Flow<List<OilCollectionRecord>> {
        return dao.getAllRecords()
    }
}


