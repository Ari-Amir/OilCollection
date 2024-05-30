package com.aco.oilcollectionapp.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class OilCollectionViewModel(private val repository: OilCollectionRepository) : ViewModel() {

    val collectionHistory: StateFlow<List<OilCollectionRecord>> =
        repository.getAllRecords()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addRecord(dateTime: String, litersCollected: Int, user: String, location: String) {
        val newRecord = OilCollectionRecord(
            dateTime = dateTime,
            litersCollected = litersCollected,
            user = user,
            location = location
        )

        viewModelScope.launch {
            repository.insertRecord(newRecord)
        }
    }
}
