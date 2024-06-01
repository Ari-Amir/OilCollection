package com.aco.oilcollection.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aco.oilcollection.viewmodel.OilCollectionViewModel

@Composable
fun StatisticsFragment(viewModel: OilCollectionViewModel) {
    val collectionHistory by viewModel.collectionHistory.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(collectionHistory) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = "${formatDateTime(item.dateTime)}  -  Collected ${item.litersCollected} liters",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun formatDateTime(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMMM yyyy 'at' HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(millis))
}

