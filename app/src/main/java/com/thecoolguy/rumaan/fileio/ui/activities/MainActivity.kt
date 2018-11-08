package com.thecoolguy.rumaan.fileio.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import com.thecoolguy.rumaan.fileio.ui.fragments.HomeFragment
import com.thecoolguy.rumaan.fileio.utils.Utils.Android
import com.thecoolguy.rumaan.fileio.utils.toast
import com.thecoolguy.rumaan.fileio.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    override fun onChooseFileClick() {
        "Choose File Clicked" toast this
        chooseFileWithPermissionCheck()
    }

    override fun onUploadFileClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var viewModel: MainActivityViewModel

    private val fragmentManager by lazy { supportFragmentManager }
    private val fragmentTransaction by lazy { fragmentManager.beginTransaction() }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, UploadHistoryActivity::class.java))
                return true
            }
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun chooseFile() {
        val intent = Android.chooseFileIntent
        startActivityForResult(Intent.createChooser(intent, "Choose the file to Upload.."),
                INTENT_FILE_REQUEST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBarTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
            setSupportActionBar(this)
        }

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        initFragment()
    }

    private fun initFragment() {
        /* Initialize the Main Fragment layout here */
        val homeFragment = HomeFragment.newInstance()

        fragmentTransaction.apply {
            add(container.id, homeFragment, HomeFragment.TAG)
            commit()
        }

    }


    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showPermissionDeniedForStorage() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showAppSettings() {
        Toast.makeText(this, getString(R.string.permission_deny), Toast.LENGTH_LONG).show()
        Android.showAppDetailsSettings(this)
    }


    companion object {
        const val TAG = "MainActivity"
        private const val INTENT_FILE_REQUEST = 44
    }
}