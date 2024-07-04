package com.aco.oilcollection.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.aco.oilcollection.database.Location
import com.aco.oilcollection.viewmodel.LocationViewModel

@Composable
fun LocationsFragment(viewModel: LocationViewModel) {
    var newLocationName by remember { mutableStateOf("") }
    val locations by viewModel.locations.collectAsState()
    var currentlyEditingLocationId by remember { mutableStateOf<Int?>(null) }
    var locationToDelete by remember { mutableStateOf<Location?>(null) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .clickable {
            currentlyEditingLocationId = null
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextField(
                    value = newLocationName,
                    onValueChange = { newLocationName = it },
                    label = { Text("Add new location") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (newLocationName.isNotBlank()) {
                            viewModel.addLocation(newLocationName)
                            newLocationName = ""
                        }
                    }),
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    enabled = currentlyEditingLocationId == null
                )
                Button(
                    onClick = {
                        if (newLocationName.isNotBlank()) {
                            viewModel.addLocation(newLocationName)
                            newLocationName = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterVertically),
                    enabled = currentlyEditingLocationId == null
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(locations, key = { it.id }) { location ->
                    LocationItem(
                        location = location,
                        onEdit = {
                            viewModel.updateLocation(it)
                            currentlyEditingLocationId = null
                        },
                        onDeleteRequest = {
                            locationToDelete = location
                        },
                        setIsEditing = { isEditing ->
                            currentlyEditingLocationId = if (isEditing) location.id else null
                        },
                        isEditing = currentlyEditingLocationId == location.id,
                        editingLocationId = currentlyEditingLocationId
                    )
                }
            }
        }

        locationToDelete?.let { location ->
            AlertDialog(
                onDismissRequest = { locationToDelete = null },
                title = { Text("Delete Location") },
                text = { Text("Are you sure you want to delete this location?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteLocation(location)
                        locationToDelete = null
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { locationToDelete = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onEdit: (Location) -> Unit,
    onDeleteRequest: () -> Unit,
    setIsEditing: (Boolean) -> Unit,
    isEditing: Boolean,
    editingLocationId: Int?
) {
    var isLocalEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(location.name) }
    var showMenu by remember { mutableStateOf(false) }

    if (isEditing) {
        setIsEditing(true)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = editedName,
                onValueChange = { editedName = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    onEdit(location.copy(name = editedName))
                    setIsEditing(false)
                }),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )
            Button(
                onClick = {
                    onEdit(location.copy(name = editedName))
                    setIsEditing(false)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Save")
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                location.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    enabled = editingLocationId == null
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(x = (-16).dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            isLocalEditing = true
                            setIsEditing(true)
                        },
                        enabled = editingLocationId == null
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            onDeleteRequest()
                        },
                        enabled = editingLocationId == null
                    )
                }
            }
        }
    }
}
