package com.thecoolguy.rumaan.fileio.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.work.WorkStatus;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener;
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener;
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener;
import com.thecoolguy.rumaan.fileio.listeners.UploadListener;
import com.thecoolguy.rumaan.fileio.repository.DisposableBucket;
import com.thecoolguy.rumaan.fileio.ui.NotificationHelper;
import com.thecoolguy.rumaan.fileio.ui.fragments.ChooseFileFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.NoNetworkDialogFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.UploadFileFragment;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import com.thecoolguy.rumaan.fileio.viewmodel.MainActivityViewModel;
import org.jetbrains.annotations.NotNull;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements DialogClickListener, UploadListener,
    OnFragmentInteractionListener, FileLoadListener {

  // TODO: replace all Toasts with Snackbars

  public static final String TAG = "MainActivity";
  private static final int INTENT_FILE_REQUEST = 44;

  private MainActivityViewModel viewModel;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_history:
        startActivity(new Intent(this, UploadHistoryActivity.class));
        return true;
      case R.id.menu_about:
        startActivity(new Intent(this, AboutActivity.class));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == INTENT_FILE_REQUEST && resultCode == RESULT_OK) {
      Uri fileUri = data.getData();
      if (fileUri != null) {
        viewModel.chooseFileFromUri(this, fileUri);
      } else {
        Toast.makeText(this, getString(R.string.oops_some_error_occurred), Toast.LENGTH_SHORT)
            .show();
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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.NoActionBarTheme);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);

    // set up initial fragment
    getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.parent_fragment_container, ChooseFileFragment.newInstance(),
            ChooseFileFragment.TAG)
        .commit();

    viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
  }

  @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  void showPermissionDeniedForStorage() {
    Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show();
  }

  @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE})
  void showAppSettings() {
    Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show();
    Utils.Android.showAppDetailsSettings(this);
  }

  @Override
  public void onDialogPositiveClick(@NonNull Dialog dialog, @NonNull Fragment dialogFragment) {
    if (dialogFragment instanceof NoNetworkDialogFragment) {
      Utils.Android.dismissDialog(dialog);
    }
  }

  @Override
  public void progress(int progress) {
    // Update the progress into the view
    Log.i(TAG, "uploadProgress: " + progress);
  }

  @Override
  public void onUpload(@NotNull FileEntity fileEntity) {
  }

  @Override
  protected void onStop() {
    super.onStop();
    DisposableBucket.INSTANCE.clearDisposableBucket();
  }

  @Override
  public void onComplete(@NotNull FileEntity fileEntity) {
    Toast.makeText(this, "Upload and Save Successful!", Toast.LENGTH_SHORT).show();
    new NotificationHelper().create(getApplicationContext(), fileEntity);
  }

  @Override
  public void onChooseFileClick() {
    MainActivityPermissionsDispatcher.chooseFileWithPermissionCheck(this);
  }

  @Override
  public void onUploadFileClick() {
    viewModel.uploadFile();

    if (viewModel.getUploadWorkStatus() != null) {
      viewModel.getUploadWorkStatus().observe(this, new Observer<WorkStatus>() {
        @Override
        public void onChanged(WorkStatus workStatus) {
          if (workStatus.getState().isFinished()) {
            Toast.makeText(MainActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  @Override
  public void onFileLoad(@NotNull LocalFile localFile) {
    // change the current fragment to upload
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
        android.R.anim.fade_in, android.R.anim.fade_out);
    transaction.replace(R.id.parent_fragment_container, UploadFileFragment.newInstance(),
        UploadFileFragment.TAG);
    transaction.addToBackStack(null);
    transaction.commit();

  }
}