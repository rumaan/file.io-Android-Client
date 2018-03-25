package com.thecoolguy.rumaan.fileio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

  @OnClick(R.id.btn_restart_app)
  void restartApp() {
    finishAffinity();
    startActivity(new Intent(getApplicationContext(), MainActivity.class));
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_error);
    ButterKnife.bind(this);
  }
}
