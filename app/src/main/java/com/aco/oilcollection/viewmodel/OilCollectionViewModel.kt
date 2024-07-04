package com.aco.oilcollection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aco.oilcollection.database.entities.OilCollectionRecord
import com.aco.oilcollection.repository.OilCollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

open class OilCollectionViewModel(private val repository: OilCollectionRepository) : ViewModel() {

    private val _remainingVolume = MutableStateFlow(1800)
    val remainingVolume: StateFlow<Int> = _remainingVolume

    private val _collectionHistory = MutableStateFlow<List<Triple<OilCollectionRecord, String, String>>>(emptyList())
    val collectionHistory: StateFlow<List<Triple<OilCollectionRecord, String, String>>> = _collectionHistory

    init {
        loadRecordsForToday()
        loadAllRecordsWithDetails()
    }

    private fun loadRecordsForToday() {
        val startOfDay = getStartOfDay()
        val endOfDay = getEndOfDay()
        viewModelScope.launch {
            repository.getRecordsForToday(startOfDay, endOfDay).collect { records ->
                val totalCollected = records.sumOf { it.litersCollected }
                _remainingVolume.value = 1800 - totalCollected
            }
        }
    }

    private fun loadAllRecordsWithDetails() {
        viewModelScope.launch {
            repository.getAllRecords().collect { records ->
                val recordsWithDetails = records.map { record ->
                    val userName = repository.getUserNameById(record.userId) ?: "Unknown"
                    val locationName = repository.getLocationNameById(record.locationId) ?: "Unknown"
                    Triple(record, userName, locationName)
                }
                _collectionHistory.value = recordsWithDetails
            }
        }
    }

    fun addRecord(dateTime: Long, litersCollected: Int, userId: Int, locationId: Int) {
        val newRecord = OilCollectionRecord(
            dateTime = dateTime,
            litersCollected = litersCollected,
            userId = userId,
            locationId = locationId
        )

        viewModelScope.launch {
            repository.insertRecord(newRecord)
            loadRecordsForToday()
        }
    }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getEndOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}
