package com.aco.oilcollection.repository


import com.aco.oilcollection.database.Location
import com.aco.oilcollection.database.LocationDao
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {

    suspend fun insertLocation(location: Location) {
        locationDao.insert(location)
    }

    suspend fun updateLocation(location: Location) {
        locationDao.update(location)
    }

    suspend fun deleteLocation(location: Location) {
        locationDao.delete(location)
    }

    fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations()
    }
}
