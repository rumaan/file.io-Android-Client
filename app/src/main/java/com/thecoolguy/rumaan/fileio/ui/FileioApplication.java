package com.thecoolguy.rumaan.fileio.ui;

import android.app.Application;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * Base Application class
 */

public class FileioApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Custom Activity on Crash
        CaocConfig.Builder.create()
                .errorActivity(ErrorActivity.class)
                .apply();
    }
}
