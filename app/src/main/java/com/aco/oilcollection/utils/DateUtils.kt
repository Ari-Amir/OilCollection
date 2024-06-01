package com.aco.oilcollection.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(Date())
}

fun getCurrentDateTime(): Long {
    return System.currentTimeMillis()
}