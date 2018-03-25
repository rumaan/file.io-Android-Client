package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

public class UploadItemViewModel extends AndroidViewModel {

  private UploadItemViewModel(@NonNull Application application) {
    super(application);
  }
}
