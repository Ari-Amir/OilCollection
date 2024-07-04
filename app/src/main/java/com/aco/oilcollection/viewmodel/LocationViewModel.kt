package com.aco.oilcollection.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aco.oilcollection.database.entities.Location
import com.aco.oilcollection.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            repository.getAllLocations().collect { locations ->
                _locations.value = locations
            }
        }
    }

    fun addLocation(name: String) {
        val newLocation = Location(name = name)
        viewModelScope.launch {
            repository.insertLocation(newLocation)
            loadLocations()
        }
    }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            repository.updateLocation(location)
            loadLocations()
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            repository.deleteLocation(location)
            loadLocations()
        }
    }
}
