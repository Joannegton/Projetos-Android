package com.example.diariodememorias

import android.net.Uri

data class Memory(
    val title: String,
    val description: String,
    val imageUri: Uri?
)

