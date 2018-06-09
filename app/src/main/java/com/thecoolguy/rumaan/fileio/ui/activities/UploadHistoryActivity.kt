package com.thecoolguy.rumaan.fileio.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.thecoolguy.rumaan.fileio.R

class UploadHistoryActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_history)
        supportActionBar?.title = getString(R.string.upload_history_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO: get these details from DB using the id
        val name = intent?.getStringExtra(getString(R.string.key_file_name))
        val url = intent?.getStringExtra(getString(R.string.key_file_url))

    }

    companion object {
        private val TAG = "UploadHistoryActivity"
    }

}
