package com.aco.oilcollection.database


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oil_collection_records")
data class OilCollectionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val litersCollected: Int,
    val user: String,
    val location: String
)

