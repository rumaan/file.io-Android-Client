package com.thecoolguy.rumaan.fileio.ui.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import com.thecoolguy.rumaan.fileio.repository.UploadWorker
import com.thecoolguy.rumaan.fileio.ui.fragments.*
import com.thecoolguy.rumaan.fileio.utils.Utils.Android
import com.thecoolguy.rumaan.fileio.viewmodel.MainActivityViewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity(), DialogClickListener, OnFragmentInteractionListener, FileLoadListener {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var rootView: ConstraintLayout
    private lateinit var progressFragment: UploadProgressFragment


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_FILE_REQUEST && resultCode == RESULT_OK) {
            val fileUri = data?.data
            if (fileUri != null) {
                viewModel.chooseFileFromUri(this, fileUri)
            } else {
                Toast.makeText(this, getString(R.string.oops_some_error_occurred), Toast.LENGTH_SHORT)
                        .show()
            }
        }
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
        rootView = findViewById(R.id.root_view)
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
            setSupportActionBar(this)
        }

        // set up initial fragment
        setUpInitialFragment()

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    private fun setUpInitialFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.parent_fragment_container, ChooseFileFragment.newInstance(),
                        ChooseFileFragment.TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .addToBackStack(ChooseFileFragment.TAG)
                .commit()
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

    override fun onDialogPositiveClick(dialog: Dialog, dialogFragment: Fragment) {
        if (dialogFragment is NoNetworkDialogFragment) {
            Android.dismissDialog(dialog)
        }
    }

    override fun onChooseFileClick() {
        chooseFileWithPermissionCheck()
    }

    override fun onBackPressed() {
        val backStackCount = supportFragmentManager.backStackEntryCount
        /* Pop until first fragment i.e (ChooseFileFragment) */
        if (backStackCount > 1) {
            supportFragmentManager
                    .popBackStack(ChooseFileFragment.TAG, 0)
        } else {
            finish()
        }
    }

    override fun onUploadFileClick() {
        viewModel.uploadFile()
        progressFragment = UploadProgressFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.parent_fragment_container, progressFragment)
                .addToBackStack(null)
                .commit()

        viewModel.uploadWorkStatus?.observe(this, Observer { workStatus ->
            if (workStatus.state.isFinished) {
                val url = workStatus.outputData.getString(UploadWorker.KEY_RESULT)
                // switch to results fragment
                supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.parent_fragment_container, UploadResultFragment.newInstance(url!!),
                                UploadResultFragment.TAG)
                        .addToBackStack(UploadResultFragment.TAG)
                        .commit()
            }
        })
    }

    override fun onClose() {
        /* Pop all fragments till (Exclusive of) ChooseFileFragment from back stack */
        supportFragmentManager
                .popBackStack(ChooseFileFragment.TAG, 0)
    }

    override fun onFileLoad(localFile: LocalFile) {
        // change the current fragment to upload
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(R.id.parent_fragment_container,
                    UploadFileFragment.newInstance(localFile.name),
                    UploadFileFragment.TAG)
            addToBackStack(UploadFileFragment.TAG)
            commit()
        }
    }

    companion object {
        const val TAG = "MainActivity"
        private const val INTENT_FILE_REQUEST = 44
    }
}