package com.thecoolguy.rumaan.fileio.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener;
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener;
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener;
import com.thecoolguy.rumaan.fileio.repository.UploadWorker;
import com.thecoolguy.rumaan.fileio.ui.fragments.ChooseFileFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.NoNetworkDialogFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.UploadFileFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.UploadProgressFragment;
import com.thecoolguy.rumaan.fileio.ui.fragments.UploadResultFragment;
import com.thecoolguy.rumaan.fileio.utils.Utils.Android;
import com.thecoolguy.rumaan.fileio.viewmodel.MainActivityViewModel;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.WorkStatus;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements DialogClickListener,
        OnFragmentInteractionListener, FileLoadListener {

    // TODO: replace all Toasts with Snackbars

    public static final String TAG = "MainActivity";
    private static final int INTENT_FILE_REQUEST = 44;

    private MainActivityViewModel viewModel;
    private ConstraintLayout rootView;
    private UploadProgressFragment progressFragment;

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

    @NeedsPermission({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    public void chooseFile() {
        Intent intent = Android.INSTANCE.getChooseFileIntent();
        startActivityForResult(Intent.createChooser(intent, "Choose the file to Upload.."),
                INTENT_FILE_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoActionBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.root_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // set up initial fragment
        setUpInitialFragment();

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
    }

    private void setUpInitialFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.parent_fragment_container, ChooseFileFragment.newInstance(),
                        ChooseFileFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .addToBackStack(ChooseFileFragment.TAG)
                .commit();
    }

    @OnPermissionDenied({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void showPermissionDeniedForStorage() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    })
    void showAppSettings() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show();
        Android.INSTANCE.showAppDetailsSettings(this);
    }

    @Override
    public void onDialogPositiveClick(@NonNull Dialog dialog, @NonNull Fragment dialogFragment) {
        if (dialogFragment instanceof NoNetworkDialogFragment) {
            Android.INSTANCE.dismissDialog(dialog);
        }
    }

    @Override
    public void onChooseFileClick() {
        MainActivityPermissionsDispatcher.chooseFileWithPermissionCheck(this);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        /* Pop until first fragment i.e (ChooseFileFragment) */
        if (backStackCount > 1) {
            getSupportFragmentManager()
                    .popBackStack(ChooseFileFragment.TAG, 0);
        } else {
            finish();
        }
    }

    @Override
    public void onUploadFileClick() {
        viewModel.uploadFile();

        // showSnackBar();
        // replace the fragment container with progress

        progressFragment = UploadProgressFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.parent_fragment_container, progressFragment)
                .addToBackStack(null)
                .commit();

        if (viewModel.getUploadWorkStatus() != null) {
            viewModel.getUploadWorkStatus().observe(this, new Observer<WorkStatus>() {
                @Override
                public void onChanged(WorkStatus workStatus) {
                    if (workStatus.getState().isFinished()) {
                        String url = workStatus.getOutputData().getString(UploadWorker.KEY_RESULT);

                        // switch to results fragment
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.parent_fragment_container, UploadResultFragment.newInstance(url),
                                        UploadResultFragment.TAG)
                                .addToBackStack(UploadResultFragment.TAG)
                                .commit();
                    }
                }
            });
        }
    }

    @Override
    public void onClose() {
        /* Pop all fragments till (Exclusive of) ChooseFileFragment from back stack */
        getSupportFragmentManager()
                .popBackStack(ChooseFileFragment.TAG, 0);
    }


    @Override
    public void onFileLoad(@NotNull LocalFile localFile) {
        // change the current fragment to upload
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.parent_fragment_container,
                UploadFileFragment.newInstance(localFile.getName()),
                UploadFileFragment.TAG);
        transaction.addToBackStack(UploadFileFragment.TAG);
        transaction.commit();


    }
}