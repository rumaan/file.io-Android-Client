package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import com.thecoolguy.rumaan.fileio.data.repository.Repository;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.jetbrains.annotations.NotNull;

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

public class MainActivityViewModel extends AndroidViewModel {

  private static final String TAG = "MainActivityViewModel";

  /* Store the current chosen file instance */
  private LocalFile localFile;

  private MainActivityViewModel(@NonNull Application application) {
    super(application);
  }


  /**
   * Get the file from the database and save in the current view model state.
   */
  public void chooseFileFromUri(@NotNull final Context context, @NotNull final Uri fileUri) {
    this.localFile = Utils.getLocalFile(context, fileUri);
  }

  public void uploadFile(@NotNull final Context context) {
    if (localFile == null) {
      Log.e(TAG, "LocalFile object null! uploadFile() called before choosing a file!",
          new NullPointerException());
      Toast.makeText(context, context.getString(R.string.oops_some_error_occurred),
          Toast.LENGTH_SHORT).show();
    } else {
      Repository.initiateUpload(localFile);
    }
  }

}
