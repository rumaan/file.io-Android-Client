package com.thecoolguy.rumaan.fileio;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final String URL = "http://file.io";
    public static final String PACKAGE = "com.thecoolguy.rumaan.fileio";

    public static final int INTENT_FILE_REQUEST = 42;

    private Button uploadButton;
    private TextView linkTextView;
    private ConstraintLayout rootView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri filePath = data.getData();
                if (filePath != null) {
                    Log.d(TAG, "onActivityResult: " + filePath.getPath());
                    MainActivityPermissionsDispatcher.uploadFileWithPermissionCheck(MainActivity.this, filePath);
                } else {
                    //TODO: show a delightful dialog to the user
                    Log.e(TAG, "onActivityResult: ERROR", new NullPointerException("File path URI is null"));
                    Toast.makeText(this, "Some Error Occurred.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.cancel_file_choose_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NeedsPermission({Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void uploadFile(@NonNull Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);

        Rx2AndroidNetworking.upload(URL)
                .addMultipartFile("file", file)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //TODO: update progress
                    }
                })
                .getJSONObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //TODO: Do something with this disposable object
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        Log.d(TAG, "onNext: Response -> " + jsonObject.toString());
                        try {
                            if (jsonObject.getBoolean("success")) {
                                String link = jsonObject.getString("link");
                                Log.i(TAG, "Link: " + link);
                                updateLinkText(link);
                            }
                            // TODO: handle failure in JSON
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Some Error Occurred", e);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void updateLinkText(String link) {
        linkTextView.setText(link);
        TransitionManager.beginDelayedTransition(rootView);
        linkTextView.setVisibility(View.VISIBLE);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void chooseFile() {
        Log.i(TAG, "Read file permissions granted.");

        // Show an file intent picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, INTENT_FILE_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        uploadButton = findViewById(R.id.btn_upload);
        linkTextView = findViewById(R.id.link);
        rootView = findViewById(R.id.root_view);

        AndroidNetworking.initialize(getApplicationContext());

        /* Handle incoming intent content */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        // FIXME: Google photos URI
        if (type != null) {
            Log.d(TAG, "Receive Type: " + type);
            Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            Log.d(TAG, "\nURI: " + fileUri);
            if (Intent.ACTION_SEND.equals(action) && fileUri != null) {
                MainActivityPermissionsDispatcher.uploadFileWithPermissionCheck(this, fileUri);
            }
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.chooseFileWithPermissionCheck(MainActivity.this);
            }
        });

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy the content of the link text to Clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("link", linkTextView.getText());
                if (clipboardManager != null) {
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(MainActivity.this, getString(R.string.link_copy), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForStorage() {
        Toast.makeText(this, getString(R.string.app_wont_work), Toast.LENGTH_LONG).show();

        //TODO: show a generic dialog for why the permission is denied
        showAppDetailsSettings();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showPermissionDeniedForStorage() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(MainActivity.this, requestCode, grantResults);
    }


    /* Opens App info screen in settings */
    void showAppDetailsSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                intent = new Intent(Intent.ACTION_APPLICATION_PREFERENCES);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package: " + getPackageName()));
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
            e.printStackTrace();
        }

    }
}
