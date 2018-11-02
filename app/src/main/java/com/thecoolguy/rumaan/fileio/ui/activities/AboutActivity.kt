package com.thecoolguy.rumaan.fileio.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.thecoolguy.rumaan.fileio.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_about, menu)
        return true
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.TransparentNav)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_open_source -> startActivity(Intent(this, LicenseActivity::class.java))
            else -> return false
        }
        return super.onOptionsItemSelected(item)
    }
}
