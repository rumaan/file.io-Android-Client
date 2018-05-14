package com.thecoolguy.rumaan.fileio.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.utils.Utils;

/* Parse and Decrypt the link */

public class DownloadActivity extends AppCompatActivity {

  private static final String TAG = "DownloadActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download);

    Intent intent = getIntent();
    if (intent == null) {
      Crashlytics.log("Download Intent Called somehow, and intent is null!");
      throw new RuntimeException("Download Intent Called Somehow!");
    } else {
      Uri uri = intent.getData();
      if (uri != null) {
        Toast.makeText(this, "" + uri.toString(), Toast.LENGTH_SHORT).show();
        openIntent(uri);
      } else {
        Toast.makeText(this, "No Data received!", Toast.LENGTH_SHORT).show();
      }
    }
  }


  private void openIntent(Uri uri) {
    String temp = uri.toString();
    // Strip the url on '/d'

    String url = Utils.URLParser.parseEncryptUrl(temp);

    Log.d(TAG, "openIntent: " + url);

    // Open the URL using Web Browser
    Intent intent = Utils.Android.newIntent(Intent.ACTION_VIEW, Intent.CATEGORY_APP_BROWSER, null);
    if (intent.resolveActivity(getPackageManager()) != null) {
      finishAffinity();
      startActivity(intent);
    } else {
      finishAffinity();
      Toast.makeText(this, getString(R.string.error_no_app_on_device), Toast.LENGTH_SHORT).show();
    }
  }
}
