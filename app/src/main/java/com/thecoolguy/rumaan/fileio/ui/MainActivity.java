package com.thecoolguy.rumaan.fileio.ui;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.jetbrains.annotations.NotNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

  public static final String TAG = "MainActivity";
  private static final int INTENT_FILE_REQUEST = 44;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == INTENT_FILE_REQUEST) {
      if (resultCode == RESULT_OK) {
        Uri fileUri = data.getData();
        Log.i(TAG, "URI: " + fileUri);
      } else {
        Toast.makeText(this, getString(R.string.cancel_file_choose_msg), Toast.LENGTH_SHORT).show();
      }
    }
  }

  @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  public void chooseFile(@NotNull Intent dataIntent) {
        /* Check for network connectivity */
    if (Utils.isConnectedToNetwork(this)) {
      // Use system file browser
      Intent intent = Utils
          .newIntent(Intent.ACTION_OPEN_DOCUMENT, Intent.CATEGORY_OPENABLE, "*/*");
      startActivityForResult(Intent.createChooser(intent, "Choose the file to Upload.."),
          INTENT_FILE_REQUEST);
    } else {
      /* Show no network dialog */
      Utils.showDialogFragment(new NoNetworkDialogFragment(), getSupportFragmentManager(),
          getString(R.string.no_net_dialog_fragment_tag));
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* Set theme to app theme after creating the activity */
    setTheme(R.style.NoActionBarTheme);
    setContentView(R.layout.activity_main);

  }

  @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  void showPermissionDeniedForStorage() {
    Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
  }

  @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE, permission.ACCESS_NETWORK_STATE})
  void showAppSettings() {
    Utils.showAppDetailsSettings(this);
  }


}
