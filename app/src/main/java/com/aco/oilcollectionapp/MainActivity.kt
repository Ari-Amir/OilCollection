package com.aco.oilcollectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aco.oilcollectionapp.ui.theme.OilCollectionAppTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OilCollectionAppTheme {
                var remainingVolume by remember { mutableStateOf(1800) }
                var collectionHistory by remember { mutableStateOf(listOf<String>()) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewPagerScreen(
                        modifier = Modifier.padding(innerPadding),
                        remainingVolume = remainingVolume,
                        onAddLiters = { liters ->
                            val currentDateTime = getCurrentDateTime()
                            remainingVolume -= liters
                            collectionHistory = collectionHistory + "$currentDateTime Collected $liters liters"
                        },
                        collectionHistory = collectionHistory
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ViewPagerScreen(
    modifier: Modifier = Modifier,
    remainingVolume: Int,
    onAddLiters: (Int) -> Unit,
    collectionHistory: List<String>
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            Tab(
                text = { Text("Input") },
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(0)
                    }
                }
            )
            Tab(
                text = { Text("Statistics") },
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(1)
                    }
                }
            )
        }
        HorizontalPager(count = 2, state = pagerState) { page ->
            when (page) {
                0 -> InputFragment(remainingVolume = remainingVolume, onAddLiters = onAddLiters)
                1 -> StatisticsFragment(collectionHistory = collectionHistory)
            }
        }
    }
}

fun getCurrentDateTime(): String {
    val sdf = java.text.SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}

@Preview(showBackground = true)
@Composable
fun ViewPagerScreenPreview() {
    OilCollectionAppTheme {
        var remainingVolume by remember { mutableStateOf(1800) }
        var collectionHistory by remember { mutableStateOf(listOf<String>()) }

        ViewPagerScreen(
            remainingVolume = remainingVolume,
            onAddLiters = { liters ->
                val currentDateTime = getCurrentDateTime()
                remainingVolume -= liters
                collectionHistory = collectionHistory + "$currentDateTime Collected $liters liters"
            },
            collectionHistory = collectionHistory
        )
    }
}
