package com.aco.oilcollectionapp

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFragment(remainingVolume: Int, onAddLiters: (Int) -> Unit) {
    var liters by remember { mutableStateOf("") }
    val currentDate = remember { mutableStateOf(getCurrentDate()) }
    val context = LocalContext.current

    fun handleAddLiters() {
        Log.d("InputFragment", "handleAddLiters called")
        val enteredLiters = liters.filter { it.isDigit() }.toIntOrNull()
        if (remainingVolume == 0) {
            Toast.makeText(
                context,
                "Вы достигли лимита, больше нет свободного объема.",
                Toast.LENGTH_SHORT
            ).show()
        } else if (enteredLiters != null && enteredLiters <= remainingVolume) {
            Log.d("InputFragment", "onAddLiters called with $enteredLiters")
            onAddLiters(enteredLiters)
            liters = ""
        } else {
            Toast.makeText(
                context,
                "Можно только $remainingVolume литров или меньше",
                Toast.LENGTH_SHORT
            ).show()
            liters = ""
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = currentDate.value,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Remaining Volume: ${remainingVolume}L",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = liters,
                onValueChange = { newLiters ->
                    if (newLiters.length <= 4 && newLiters.all { it.isDigit() }) {
                        liters = newLiters
                    }
                },
                label = {
                    Text(
                        "Enter liters",
                        fontSize = 40.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { handleAddLiters() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
                    containerColor = MaterialTheme.colorScheme.background,
                    cursorColor = Color.Transparent,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
                    disabledIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { handleAddLiters() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFA0E7E5),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(50))
            ) {
                Text(
                    text = "Add",
                    fontSize = 35.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                )
            }
        }
    }
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(Date())
}