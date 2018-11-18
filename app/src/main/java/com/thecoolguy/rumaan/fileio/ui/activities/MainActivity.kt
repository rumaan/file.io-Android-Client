package com.thecoolguy.rumaan.fileio.ui.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import com.thecoolguy.rumaan.fileio.utils.createUploadWork
import com.thecoolguy.rumaan.fileio.repository.UploadWorker
import com.thecoolguy.rumaan.fileio.ui.fragments.HomeFragment
import com.thecoolguy.rumaan.fileio.ui.fragments.NoNetworkDialogFragment
import com.thecoolguy.rumaan.fileio.ui.fragments.ResultFragment
import com.thecoolguy.rumaan.fileio.utils.Utils
import com.thecoolguy.rumaan.fileio.utils.Utils.Android
import com.thecoolguy.rumaan.fileio.utils.toggleClickable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber

@RuntimePermissions
class MainActivity : AppCompatActivity(), OnFragmentInteractionListener, DialogClickListener {
    override fun onDialogPositiveClick(dialog: Dialog, dialogFragment: Fragment) {
        dialog.dismiss()
    }

    override fun onDone() {
        this.onBackPressed()
    }

    override fun onUploadFileClick() {
        if (Utils.Android.isConnectedToNetwork(this)) {
            btn_choose_file toggleClickable false
            chooseFileWithPermissionCheck()
        } else {
            NoNetworkDialogFragment.newInstance()
                    .show(supportFragmentManager, getString(R.string.no_net_dialog_fragment_tag))
        }
    }

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

    private fun setUpWork(uri: Uri) {
        val work = createUploadWork(uri.toString())
        WorkManager.getInstance().enqueue(work)

        Toast.makeText(this, "Uploading..", Snackbar.LENGTH_SHORT).show()

        val workId = work.id
        WorkManager.getInstance().getWorkInfoByIdLiveData(workId)
                .observe(this@MainActivity, Observer { workInfo ->
                    if (workInfo != null && workInfo.state.isFinished) {
                        val outputData = workInfo.outputData
                        val url = outputData.getString(UploadWorker.KEY_RESULT_URL) ?: ""
                        val days = outputData.getInt(UploadWorker.KEY_RESULT_DAYS, 14)
                        showResult(url, days)
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == FILE_REQUEST) {
            btn_choose_file toggleClickable true
            /* Check the returned URI */
            if (resultCode == Activity.RESULT_OK) {
                intent?.data?.let { it ->
                    setUpWork(it)
                }
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
        startActivityForResult(intent, FILE_REQUEST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBarTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
            setSupportActionBar(this)
        }

        initFragment()
    }

    private fun initFragment() {
        // Initialize the Main Fragment layout here
        val homeFragment = HomeFragment.newInstance()

        supportFragmentManager.beginTransaction()
                .addToBackStack(HomeFragment.TAG)
                .add(container.id, homeFragment)
                .commit()

    }

    private fun showResult(url: String, days: Int) {
        val resultFragment = ResultFragment.newInstance(url, days)
        supportFragmentManager.beginTransaction()
                .addToBackStack(ResultFragment.TAG)
                .add(container.id, resultFragment)
                .commit()
    }

    override fun onBackPressed() {
        Timber.d("Back Stack count: ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack(HomeFragment.TAG, 0)
        } else {
            finishAffinity()
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
        private const val FILE_REQUEST = 44
    }
}