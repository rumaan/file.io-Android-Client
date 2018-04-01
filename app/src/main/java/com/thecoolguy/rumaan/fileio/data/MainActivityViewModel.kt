package com.thecoolguy.rumaan.fileio.data

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.util.Log
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.data.repository.Repository
import com.thecoolguy.rumaan.fileio.ui.OnFileLoadListener
import com.thecoolguy.rumaan.fileio.utils.Utils
import kotlinx.coroutines.experimental.launch

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

class MainActivityViewModel : ViewModel() {
    /* Store the current chosen file instance */
    private var localFile: LocalFile? = null

    /**
     * Get the file from the database and save in the current view model state.
     */
    fun chooseFileFromUri(context: Context, fileUri: Uri, fileLoadListener: OnFileLoadListener) {
        launch {
            localFile = Utils.getLocalFile(context, fileUri)
            localFile?.let {
                fileLoadListener.onFileLoad(it)
            }
        }
        Log.i(TAG, "Outside!")
    }

    fun uploadFile(context: Context) {
        localFile?.let {
            Repository.initiateUpload(it)
        }
    }

    companion object {
        private val TAG = "MainActivityViewModel"
    }

}
