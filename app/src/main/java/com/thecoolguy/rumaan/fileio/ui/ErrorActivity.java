package com.thecoolguy.rumaan.fileio.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.thecoolguy.rumaan.fileio.R;

/**
 * Activity that's displayed if any runtime crashes occur
 */

public class ErrorActivity extends AppCompatActivity {

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_error);
  }
}
