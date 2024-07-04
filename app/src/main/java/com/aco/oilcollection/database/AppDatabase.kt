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


//INSERT INTO locations (name) VALUES
//('Amber Roast Manurewa'),
//('BurgerShop/Notorious Morepork'),
//('Candella Krd'),
//('Cheek and Chong Orewa'),
//('Chicking Albany'),
//('Chicking Botany'),
//('Chicking East Tamaki'),
//('Chicking Glen Eden'),
//('Chicking Glenfield'),
//('Chicking Henderson'),
//('Chicking Mangere'),
//('Chicking Manukau'),
//('Chicking Onehunga'),
//('Chicking Sandringham'),
//('Chicking Takanini'),
//('Coast - Bites Orewa'),
//('Downlow Henderson'),
//('Downlow Kohimarama'),
//('Downlow Ponsoby'),
//('Flying Chicken Albany'),
//('Food Hut Manurewa'),
//('K Chicken Avondale'),
//('K Chicken Glen Eden'),
//('K Chicken Hobsonville'),
//('K Chicken Manukau'),
//('K Chicken Parnell'),
//('Le Vietnamese Kitchen'),
//('Little and Kitchen Takeaways'),
//('Lowbrow CBD'),
//('Lowbrow Ellerslie'),
//('Lowbrow K Road'),
//('Mad for Chicken'),
//('Miss Krispy Pukekohe'),
//('My Fried Chicken Britomart'),
//('My Fried Chicken Mission Bay'),
//('My Fried Chicken New Market'),
//('My Fried Chicken Ponsonby'),
//('My Fried Chicken Takapuna'),
//('Natural Bakery'),
//('No1 Chicken NM'),
//('Oh My Chicken'),
//('Paradise Restaurant Sandringham'),
//('Paradise Takeaway'),
//('Pocha Albany'),
//('Pocha CBD'),
//('Pocha Henderson'),
//('Pocha Mt Roskill'),
//('Tok Tok Hobsonville'),
//('Tok Tok Takapuna'),
//('Windmill Bakery');
