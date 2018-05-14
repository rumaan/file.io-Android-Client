package com.thecoolguy.rumaan.fileio.ui;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import com.thecoolguy.rumaan.fileio.repository.Repository;


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

    // Create the repository instance
    Repository.setInstance(this);

    Intent intent = new Intent(this, UploadService.class);
    bindService(intent, new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.i(TAG, "onServiceConnected: Connected");
      }

      @Override
      public void onServiceDisconnected(ComponentName componentName) {
        Log.i(TAG, "onServiceDisconnected: Disconnected");
      }
    }, Context.BIND_AUTO_CREATE);
  }


}
