package com.aco.oilcollection.fragments

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aco.oilcollection.R
import com.aco.oilcollection.utils.getCurrentDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFragment(
    remainingVolume: Int,
    onAddLiters: (Int, String) -> Unit,
) {
    var liters by remember { mutableStateOf("") }
    val currentDate = remember { mutableStateOf(getCurrentDate()) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var enteredLiters by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("Select location") }
    val locations = listOf(
        "KFC Mt Albert",
        "KFC Mt Roskill",
        "KFC Manukau",
        "McDonald's East Tamaki",
        "McDonald's North Shore",
        "McDonald's Britomart",
        "McDonald's Avondale",
        "McDonald's New Lynn",
        "McDonald's Takanini",
        "KFC Mt Albert",
        "KFC Mt Roskill",
        "KFC Manukau",
        "McDonald's East Tamaki",
        "McDonald's North Shore",
        "McDonald's Britomart",
        "McDonald's Avondale",
        "McDonald's New Lynn",
        "McDonald's Takanini"
    )


    fun handleAddLiters() {
        enteredLiters = liters.filter { it.isDigit() }.toIntOrNull() ?: 0
        if (enteredLiters == 0) {
            Toast.makeText(
                context,
                "Please enter a value greater than zero.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (selectedLocation == "Select location") {
            Toast.makeText(
                context,
                "Please enter a location.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (remainingVolume == 0) {
            Toast.makeText(
                context,
                "You have reached the limit, there is no more available space.",
                Toast.LENGTH_SHORT
            ).show()
            liters = ""
        } else if (enteredLiters in 1..remainingVolume) {
            showDialog = true
        } else {
            Toast.makeText(
                context,
                "You can only have $remainingVolume liters or less.",
                Toast.LENGTH_SHORT
            ).show()
            liters = ""
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Action") },
            text = { Text("Are you sure you want to add $enteredLiters liters to $selectedLocation?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (enteredLiters <= remainingVolume) {
                            onAddLiters(enteredLiters, selectedLocation)
                            liters = ""
                            selectedLocation = "Select location"
                        } else {
                            Toast.makeText(
                                context,
                                "You can only have $remainingVolume liters or less.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showDialog = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
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
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
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
                    containerColor = MaterialTheme.colorScheme.background,
                    cursorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val boxWidth = constraints.maxWidth

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = selectedLocation,
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(16.dp),
                        style = TextStyle(
                            lineHeight = 50.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current) { boxWidth.toDp() })
                            .heightIn(max = 300.dp)
                    ) {
                        locations.forEach { location ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedLocation = location
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                text = { Text(text = location) }
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (selectedLocation != "Select location") 2.dp else 1.dp)
                            .background(Color.Black)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { handleAddLiters() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
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
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                )
            }
        }
    }
}
