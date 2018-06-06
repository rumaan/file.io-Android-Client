package com.thecoolguy.rumaan.fileio.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.thecoolguy.rumaan.fileio.R;

public class AboutActivity extends AppCompatActivity {

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.option_about, menu);
    return true;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    setTheme(R.style.TransparentNav);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }
}
