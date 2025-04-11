package com.example.personaldiaryapp

import android.content.Context

class DiaryRepository(private val context: Context) {
    fun saveEntry(date: String, content: String) {
        context.openFileOutput("$date.txt", Context.MODE_PRIVATE).use {
            it.write(content.toByteArray())
        }
    }

    fun loadEntry(date: String): String {
        return try {
            context.openFileInput("$date.txt").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }
}
