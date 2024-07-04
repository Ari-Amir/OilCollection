package com.aco.oilcollection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import com.aco.oilcollection.database.AppDatabase
import com.aco.oilcollection.repository.LocationRepository
import com.aco.oilcollection.repository.OilCollectionRepository
import com.aco.oilcollection.repository.UserRepository
import com.aco.oilcollection.ui.AuthScreen
import com.aco.oilcollection.ui.BlockScreen
import com.aco.oilcollection.ui.ViewPagerScreen
import com.aco.oilcollection.ui.isTrialPeriodExpired
import com.aco.oilcollection.ui.theme.OilCollectionAppTheme
import com.aco.oilcollection.utils.alignBottomWithPadding
import com.aco.oilcollection.utils.getCurrentDateTime
import com.aco.oilcollection.viewmodel.AuthViewModel
import com.aco.oilcollection.viewmodel.AuthViewModelFactory
import com.aco.oilcollection.viewmodel.LocationViewModel
import com.aco.oilcollection.viewmodel.LocationViewModelFactory
import com.aco.oilcollection.viewmodel.OilCollectionViewModel
import com.aco.oilcollection.viewmodel.OilCollectionViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private var isUserLoggedIn by mutableStateOf(false)
    private var currentUserId: Int? = null
    private var currentUserEmail: String? = null
    private var isContentVisible by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "oil_collection_database"
        ).build()

        enableEdgeToEdge()

        val oilCollectionRepository = OilCollectionRepository(database.oilCollectionRecordDao())
        val locationRepository = LocationRepository(database.locationDao())
        val userRepository = UserRepository(database.userDao())

        lifecycleScope.launch {
            try {
                val loggedInUser = userRepository.getLoggedInUser()
                if (loggedInUser != null) {
                    isUserLoggedIn = true
                    currentUserEmail = loggedInUser.email
                    currentUserId = loggedInUser.id
                }
            } catch (e: Exception) {
                //Logs
            }

            setContent {
                OilCollectionAppTheme {
                    val navController = rememberNavController()
                    LaunchedEffect(Unit) {
                        isContentVisible = true
                    }
                    AnimatedVisibility(
                        visible = isContentVisible,
                        enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 1500))
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = if (isUserLoggedIn) "home" else "auth"
                        ) {
                            composable("auth") {
                                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))
                                AuthScreen(viewModel = authViewModel) {
                                    lifecycleScope.launch {
                                        val loggedInUser = userRepository.getLoggedInUser()
                                        if (loggedInUser != null) {
                                            isUserLoggedIn = true
                                            currentUserEmail = loggedInUser.email
                                            currentUserId = loggedInUser.id
                                            authViewModel.setCurrentUser(loggedInUser)
                                        }
                                        navController.navigate("home") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                }
                            }
                            composable("home") {
                                val oilCollectionViewModel: OilCollectionViewModel = viewModel(factory = OilCollectionViewModelFactory(oilCollectionRepository))
                                val locationViewModel: LocationViewModel = viewModel(factory = LocationViewModelFactory(locationRepository))
                                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))

                                LaunchedEffect(Unit) {
                                    try {
                                        val loggedInUser = userRepository.getLoggedInUser()
                                        if (loggedInUser != null) {
                                            authViewModel.setCurrentUser(loggedInUser)
                                        }
                                    } catch (e: Exception) {
                                        //Logs
                                    }
                                }

                                val remainingVolume by oilCollectionViewModel.remainingVolume.collectAsState()
                                val isTrialExpired = remember { isTrialPeriodExpired() }

                                if (isTrialExpired) {
                                    BlockScreen()
                                } else {
                                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                        ViewPagerScreen(
                                            modifier = Modifier.padding(innerPadding),
                                            remainingVolume = remainingVolume,
                                            onAddLiters = { liters, locationId ->
                                                val currentDateTime = getCurrentDateTime()
                                                oilCollectionViewModel.addRecord(
                                                    currentDateTime,
                                                    liters,
                                                    currentUserId ?: 1,
                                                    locationId
                                                )
                                            },
                                            oilCollectionViewModel = oilCollectionViewModel,
                                            authViewModel = authViewModel,
                                            locationViewModel = locationViewModel,
                                            navController = navController
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
