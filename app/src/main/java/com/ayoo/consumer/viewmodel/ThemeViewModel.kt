package com.ayoo.consumer.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.ayoo.consumer.ui.theme.ThemeManager

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themeManager = ThemeManager(application)

    val isDarkTheme = mutableStateOf(themeManager.getTheme())

    fun storeTheme(isDark: Boolean) {
        themeManager.storeTheme(isDark)
        isDarkTheme.value = isDark
    }
}
