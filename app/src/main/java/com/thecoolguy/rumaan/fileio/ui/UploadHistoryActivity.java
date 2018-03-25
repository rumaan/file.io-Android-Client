package com.thecoolguy.rumaan.fileio.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import com.thecoolguy.rumaan.fileio.R;

public class UploadHistoryActivity extends AppCompatActivity {

  private static final String TAG = "UploadHistoryActivity";

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_history, menu);
    return true;
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload_history);

    if (getSupportActionBar() != null) {
      ActionBar actionBar = getSupportActionBar();
      actionBar.setTitle(getString(R.string.upload_history_title));
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

  }

}
