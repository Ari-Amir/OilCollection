package com.aco.oilcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.aco.oilcollection.database.AppDatabase
import com.aco.oilcollection.repository.OilCollectionRepository
import com.aco.oilcollection.ui.AuthScreen
import com.aco.oilcollection.ui.BlockScreen
import com.aco.oilcollection.ui.ViewPagerScreen
import com.aco.oilcollection.ui.isTrialPeriodExpired
import com.aco.oilcollection.ui.theme.OilCollectionAppTheme
import com.aco.oilcollection.utils.alignBottomWithPadding
import com.aco.oilcollection.utils.getCurrentDateTime
import com.aco.oilcollection.viewmodel.AuthViewModel
import com.aco.oilcollection.viewmodel.AuthViewModelFactory
import com.aco.oilcollection.viewmodel.OilCollectionViewModel
import com.aco.oilcollection.viewmodel.OilCollectionViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private var isUserLoggedIn by mutableStateOf(false)
    private var currentUserId: Int? = null
    private var currentUserEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "oil_collection_database"
        ).build()

        enableEdgeToEdge()

        lifecycleScope.launch {
            delay(500)
            try {
                val userDao = database.userDao()
                val loggedInUser = userDao.getLoggedInUser()
                if (loggedInUser != null) {
                    isUserLoggedIn = true
                    currentUserEmail = loggedInUser.email
                    currentUserId = loggedInUser.id
                }
            } catch (e: Exception) {
                // Log error if needed
            }

            setContent {
                OilCollectionAppTheme {
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        isLoading = false
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (isUserLoggedIn) {
                            val repository = OilCollectionRepository(database.oilCollectionRecordDao())
                            val viewModel: OilCollectionViewModel =
                                viewModel(factory = OilCollectionViewModelFactory(repository))

                            val remainingVolume by viewModel.remainingVolume.collectAsState()
                            val isTrialExpired = remember { isTrialPeriodExpired() }

                            if (isTrialExpired) {
                                BlockScreen()
                            }

                            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                ViewPagerScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    remainingVolume = remainingVolume,
                                    onAddLiters = { liters ->
                                        val currentDateTime = getCurrentDateTime()
                                        viewModel.addRecord(
                                            currentDateTime,
                                            liters,
                                            currentUserId ?: 1,
                                            "default_location"
                                        )
                                    },
                                    viewModel = viewModel
                                )

                                Text(
                                    text = "® 2024 AriAmir",
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .alignBottomWithPadding(5.dp),
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        } else {
                            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(database.userDao()))
                            AuthScreen(viewModel = authViewModel) {
                                lifecycleScope.launch {
                                    val userDao = database.userDao()
                                    val loggedInUser = userDao.getLoggedInUser()
                                    if (loggedInUser != null) {
                                        isUserLoggedIn = true
                                        currentUserEmail = loggedInUser.email
                                        currentUserId = loggedInUser.id
                                    }
                                }
                                setContent {
                                    OilCollectionAppTheme {
                                        val repository = OilCollectionRepository(database.oilCollectionRecordDao())
                                        val viewModel: OilCollectionViewModel =
                                            viewModel(factory = OilCollectionViewModelFactory(repository))

                                        val remainingVolume by viewModel.remainingVolume.collectAsState()
                                        val isTrialExpired = remember { isTrialPeriodExpired() }

                                        if (isTrialExpired) {
                                            BlockScreen()
                                        }

                                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                            ViewPagerScreen(
                                                modifier = Modifier.padding(innerPadding),
                                                remainingVolume = remainingVolume,
                                                onAddLiters = { liters ->
                                                    val currentDateTime = getCurrentDateTime()
                                                    viewModel.addRecord(
                                                        currentDateTime,
                                                        liters,
                                                        currentUserId ?: 1,
                                                        "default_location"
                                                    )
                                                },
                                                viewModel = viewModel
                                            )

                                            Text(
                                                text = "® 2024 AriAmir",
                                                modifier = Modifier
                                                    .padding(start = 16.dp)
                                                    .alignBottomWithPadding(5.dp),
                                                color = Color.Gray.copy(alpha = 0.5f),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Light
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
