package com.aco.oilcollection.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.aco.oilcollection.fragments.InputFragment
import com.aco.oilcollection.fragments.StatisticsFragment
import com.aco.oilcollection.viewmodel.OilCollectionViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

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