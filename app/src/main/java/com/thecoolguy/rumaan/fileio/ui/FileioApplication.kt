package com.thecoolguy.rumaan.fileio.ui

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.thecoolguy.rumaan.fileio.ui.activities.ErrorActivity


/**
 * Base Application class
 */

class FileioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Custom Activity on Crash
        CaocConfig.Builder.create()
                .errorActivity(ErrorActivity::class.java)
                .apply()
    }

    companion object {

        private val TAG = "FileioApplication"
    }
}
