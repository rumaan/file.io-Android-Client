package com.thecoolguy.rumaan.fileio.ui;

import android.Manifest;
import android.Manifest.permission;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.repository.Repository;
import com.thecoolguy.rumaan.fileio.databinding.ActivityMainBinding;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity implements DialogClickListener {

  public static final String TAG = "MainActivity";
  private static final int INTENT_FILE_REQUEST = 44;


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == INTENT_FILE_REQUEST) {
      if (resultCode == RESULT_OK) {

        Uri fileUri = data.getData();

        Log.d(TAG, "FileURI: " + fileUri);
        Log.d(TAG, "FileName: " + fileUri.getLastPathSegment());

        Repository.initiateUpload(fileUri, getApplicationContext());

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
      Intent intent = Utils
          .Android.newIntent(Intent.ACTION_OPEN_DOCUMENT, Intent.CATEGORY_OPENABLE, "*/*");
      startActivityForResult(Intent.createChooser(intent, "Choose the file to Upload.."),
          INTENT_FILE_REQUEST);
    } else {
      /* Show no network dialog */
      Utils.Android.showDialogFragment(new NoNetworkDialogFragment(), getSupportFragmentManager(),
          getString(R.string.no_net_dialog_fragment_tag));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* Set theme to app theme after creating the activity */
    setTheme(R.style.NoActionBarTheme);

    ActivityMainBinding activityMainBinding = DataBindingUtil
        .setContentView(this, R.layout.activity_main);

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
  public void onDialogPositiveClick(Dialog dialog, Fragment dialogFragment) {
    if (dialogFragment instanceof NoNetworkDialogFragment) {
      Utils.Android.dismissDialog(dialog);
    }
  }
}
