package com.ayoo.consumer

import android.app.Application
import com.backendless.Backendless

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // IMPORTANT: Replace with your actual Backendless App ID and Android API Key
        val APP_ID = "DAA2D434-2958-46C2-A527-676432D41735"
        val API_KEY = "CECEC6A6-8756-483C-BFFC-A59EDA33398A"

        Backendless.initApp(applicationContext, APP_ID, API_KEY)
    }
}
