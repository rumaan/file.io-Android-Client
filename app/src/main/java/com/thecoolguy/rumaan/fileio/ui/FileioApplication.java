package com.thecoolguy.rumaan.fileio.ui;

import android.app.Application;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import com.thecoolguy.rumaan.fileio.repository.Repository;


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

    // Create the repository instance
    Repository.setInstance(this);
  }
}
