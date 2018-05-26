package com.thecoolguy.rumaan.fileio.ui;

import static com.thecoolguy.rumaan.fileio.utils.Utils.Android.isConnectedToNetwork;

import android.Manifest;
import android.Manifest.permission;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.LocalFile;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.databinding.ActivityMainBinding;
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener;
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener;
import com.thecoolguy.rumaan.fileio.listeners.UploadListener;
import com.thecoolguy.rumaan.fileio.repository.DisposableBucket;
import com.thecoolguy.rumaan.fileio.repository.UploadWorker;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import com.thecoolguy.rumaan.fileio.viewmodel.MainActivityViewModel;
import org.jetbrains.annotations.NotNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity
    extends AppCompatActivity
    implements
    DialogClickListener,
    FileLoadListener, UploadListener {

  public static final String TAG = "MainActivity";
  private static final int INTENT_FILE_REQUEST = 44;
  private MainActivityViewModel viewModel;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == INTENT_FILE_REQUEST) {
      if (resultCode == RESULT_OK) {
        Uri fileUri = data.getData();
        if (fileUri != null) {
          viewModel.chooseFileFromUri(this, fileUri);
        } else {
          Toast.makeText(this, getString(R.string.oops_some_error_occurred), Toast.LENGTH_SHORT)
              .show();
        }
      } else {
        Toast.makeText(this, getString(R.string.cancel_file_choose_msg), Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    MainActivityPermissionsDispatcher
        .onRequestPermissionsResult(MainActivity.this, requestCode, grantResults);
  }

  @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  public void chooseFile() {
    /* Check for network connectivity */
    if (Utils.Android.isConnectedToNetwork(this)) {
      // Use system file browser
      Intent intent = Utils.Android.getChooseFileIntent();
      startActivityForResult(Intent.createChooser(intent, "Choose the file to Upload.."),
          INTENT_FILE_REQUEST);
    } else {
      /* Show no network dialog */
      Utils.Android.showDialogFragment(new NoNetworkDialogFragment(), getSupportFragmentManager(),
          getString(R.string.no_net_dialog_fragment_tag));
    }
  }

  private void chooseFileOffline() {
    // TODO: handle this in UI
    // Choose the file regardless
    Toast.makeText(this, "File will be uploaded once you're connected to the internet!",
        Toast.LENGTH_LONG).show();
    startActivityForResult(Intent.createChooser(Utils.Android.getChooseFileIntent(),
        "Choose file to Upload.."), INTENT_FILE_REQUEST);
  }

  @Override
  protected void onStart() {
    super.onStart();

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    /* Set theme to app theme after creating the activity */
    setTheme(R.style.NoActionBarTheme);
    super.onCreate(savedInstanceState);
    ActivityMainBinding activityMainBinding = DataBindingUtil
        .setContentView(this, R.layout.activity_main);

    viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

    activityMainBinding.chooseFile.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        MainActivityPermissionsDispatcher.chooseFileWithPermissionCheck(MainActivity.this);
      }
    });


  }


  @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  void showPermissionDeniedForStorage() {
    Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
  }

  @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE, permission.ACCESS_NETWORK_STATE})
  void showAppSettings() {
    Utils.Android.showAppDetailsSettings(this);
  }

  @Override
  public void onDialogPositiveClick(@NonNull Dialog dialog, @NonNull Fragment dialogFragment) {
    if (dialogFragment instanceof NoNetworkDialogFragment) {
      Utils.Android.dismissDialog(dialog);

      // Schedule for offline work
      chooseFileOffline();
    }
  }


  /* Callback for file load */
  @Override
  public void onFileLoad(@NotNull LocalFile localFile) {
    Log.i(TAG, localFile.toString());

    // TODO: Update the view
    // TODO: add upload the chosen file view
    if (isConnectedToNetwork(this)) {
      viewModel.uploadFile(this);
    } else {
      // Schedule a Work to upload and post it as notification after completion

      // Pass in the file URI
      // FIXME: redundant calls for getLocalFile()
      Data fileData = new Data.Builder()
          .putString(UploadWorker.KEY_URI, localFile.getUri().toString())
          .build();

      Constraints constraints = new Constraints.Builder()
          .setRequiredNetworkType(NetworkType.CONNECTED)
          .setRequiresBatteryNotLow(true)
          .build();

      OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
          .setConstraints(constraints)
          .setInputData(fileData)
          .build();

      WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }
  }

  @Override
  public void progress(int progress) {
    // Update the progress into the view
    Log.i(TAG, "uploadProgress: " + progress);
  }

  @Override
  public void onComplete(@NotNull FileEntity fileEntity) {
    Log.i(TAG, "onComplete: " + fileEntity.toString());

    // post a notification
    // new NotificationHelper().create(getApplicationContext(), fileEntity);
  }

  @Override
  protected void onStop() {
    super.onStop();
    DisposableBucket.INSTANCE.clearDisposableBucket();
  }

}