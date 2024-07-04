package com.aco.oilcollection.database.entities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "oil_collection_records",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Location::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OilCollectionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val litersCollected: Int,
    val userId: Int,
    val locationId: Int
)





//package com.aco.oilcollection.database.entities
//
//
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.PrimaryKey
//
//
//@Entity(
//    tableName = "oil_collection_records",
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["id"],
//            childColumns = ["userId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class OilCollectionRecord(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val dateTime: Long,
//    val litersCollected: Int,
//    val userId: Int,
//    val location: String
//)
//
