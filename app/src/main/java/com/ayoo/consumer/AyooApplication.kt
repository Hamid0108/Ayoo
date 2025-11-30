package com.ayoo.consumer

import android.app.Application
import com.backendless.Backendless

class AyooApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Backendless.initApp(
            this,
            "DAA2D434-2958-46C2-A527-676432D41735",
            "CECEC6A6-8756-483C-BFFC-A59EDA33398A"
        )
    }
}
