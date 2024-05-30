package com.aco.oilcollectionapp.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OilCollectionViewModelFactory(
    private val repository: OilCollectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OilCollectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OilCollectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
