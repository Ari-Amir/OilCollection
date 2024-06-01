package com.aco.oilcollection.ui

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aco.oilcollection.utils.alignBottomWithPadding
import java.time.LocalDate

@Composable
fun BlockScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { Button(onClick = { activity?.finish() }) { Text("Close") } }
        },
        text = {
            Text(
                text = "The trial period has ended.\nPlease purchase the full version.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.alignBottomWithPadding(bottomPadding = 150.dp))
}

@SuppressLint("NewApi")
fun isTrialPeriodExpired(): Boolean {
    val endDate = LocalDate.of(2030, 6, 3)
    val currentDate = LocalDate.now()
    return currentDate.isAfter(endDate) || currentDate.isEqual(endDate)
}