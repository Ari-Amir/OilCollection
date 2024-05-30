package com.aco.oilcollection

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.aco.oilcollection.database.AppDatabase
import com.aco.oilcollection.database.OilCollectionRepository
import com.aco.oilcollection.ui.theme.OilCollectionAppTheme
import com.aco.oilcollection.viewmodel.OilCollectionViewModel
import com.aco.oilcollection.viewmodel.OilCollectionViewModelFactory
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "oil_collection_database"
        ).build()

        enableEdgeToEdge()
        setContent {
            OilCollectionAppTheme {
                val repository = OilCollectionRepository(database.oilCollectionRecordDao())
                val viewModel: OilCollectionViewModel = viewModel(factory = OilCollectionViewModelFactory(repository))

                val remainingVolume by viewModel.remainingVolume.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewPagerScreen(
                        modifier = Modifier.padding(innerPadding),
                        remainingVolume = remainingVolume,
                        onAddLiters = { liters ->
                            val currentDateTime = getCurrentDateTime()
                            viewModel.addRecord(currentDateTime, liters, "default_user", "default_location")
                        },
                        viewModel = viewModel
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
    viewModel: OilCollectionViewModel
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                        keyboardController?.hide()
                    }
                }
            )
        }
        HorizontalPager(count = 2, state = pagerState) { page ->
            when (page) {
                0 -> InputFragment(remainingVolume = remainingVolume, onAddLiters = onAddLiters)
                1 -> {
                    keyboardController?.hide()
                    StatisticsFragment(viewModel = viewModel)
                }
            }
        }
    }
}
private fun getCurrentDateTime(): Long {
    return System.currentTimeMillis()
}
