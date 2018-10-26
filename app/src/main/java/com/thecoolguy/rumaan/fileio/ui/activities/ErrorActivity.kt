package com.thecoolguy.rumaan.fileio.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thecoolguy.rumaan.fileio.R

/**
 * Activity that's displayed if any runtime crashes occur
 */

class ErrorActivity : AppCompatActivity() {

  override fun onBackPressed() {
    super.onBackPressed()
    finishAffinity()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_error)
  }
}
