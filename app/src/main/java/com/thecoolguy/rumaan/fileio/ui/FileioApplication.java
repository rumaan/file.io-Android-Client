package com.thecoolguy.rumaan.fileio.ui;

import android.app.Application;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import com.thecoolguy.rumaan.fileio.ui.activities.ErrorActivity;


/**
 * Base Application class
 */

public class FileioApplication extends Application {

  private static final String TAG = "FileioApplication";

  @Override
  public void onCreate() {
    super.onCreate();

    // Custom Activity on Crash
    CaocConfig.Builder.create()
        .errorActivity(ErrorActivity.class)
        .apply();
  }
}
