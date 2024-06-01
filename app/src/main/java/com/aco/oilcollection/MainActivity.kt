package com.aco.oilcollection


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.aco.oilcollection.ui.BlockScreen
import com.aco.oilcollection.ui.isTrialPeriodExpired
import com.aco.oilcollection.database.AppDatabase
import com.aco.oilcollection.database.OilCollectionRepository
import com.aco.oilcollection.ui.ViewPagerScreen
import com.aco.oilcollection.ui.theme.OilCollectionAppTheme
import com.aco.oilcollection.utils.alignBottomWithPadding
import com.aco.oilcollection.utils.getCurrentDateTime
import com.aco.oilcollection.viewmodel.OilCollectionViewModel
import com.aco.oilcollection.viewmodel.OilCollectionViewModelFactory

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
                                "default_user",
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



