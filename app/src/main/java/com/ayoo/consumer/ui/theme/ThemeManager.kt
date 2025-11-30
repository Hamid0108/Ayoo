package com.ayoo.consumer.ui.theme

import android.content.Context
import android.content.SharedPreferences

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun getTheme(): Boolean = prefs.getBoolean("is_dark", false)

    fun storeTheme(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark", isDark).apply()
    }
}
