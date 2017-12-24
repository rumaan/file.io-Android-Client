package com.thecoolguy.rumaan.fileio;

import android.app.Application;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * Created by rumaankhalander on 24/12/17.
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
