package com.example.personaldiaryapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ViewModel(private val dataStoreManager: DataStoreManager, private val diaryRepo: DiaryRepository) : ViewModel() {

    val theme: StateFlow<String?> = dataStoreManager.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val fontSize: StateFlow<Int?> = dataStoreManager.fontSizeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    var diaryText = mutableStateOf("")
        private set

    fun updateDiaryText(newText: String) {
        diaryText.value = newText
    }

    fun saveDiary(date: String) {
        diaryRepo.saveEntry(date, diaryText.value)
    }

    fun loadDiary(date: String) {
        diaryText.value = diaryRepo.loadEntry(date)
    }

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            dataStoreManager.saveTheme(theme)
        }
    }

    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            dataStoreManager.saveFontSize(size)
        }
    }
}
