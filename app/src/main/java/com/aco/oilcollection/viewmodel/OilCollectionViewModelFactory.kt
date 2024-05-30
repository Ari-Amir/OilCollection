package com.aco.oilcollection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aco.oilcollection.database.OilCollectionRepository

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
