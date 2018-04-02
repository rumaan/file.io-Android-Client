package com.thecoolguy.rumaan.fileio.data

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.util.Log
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.data.repository.Repository
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener
import com.thecoolguy.rumaan.fileio.listeners.FileUploadProgressListener
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
    fun chooseFileFromUri(context: Context, fileUri: Uri, fileLoadListener: FileLoadListener) {
        launch {
            localFile = Utils.getLocalFile(context, fileUri)
            localFile?.let {
                fileLoadListener.onFileLoad(it)
            }
        }
        Log.i(TAG, "Outside!")
    }

    /**
     * Upload the file to server and save the response into the database.
     * */
    fun uploadFile(listener: FileUploadProgressListener) {
        localFile?.let {
            // upload file
            Repository.getInstance().upload(it, listener)

            // TODO: update progress
        }
    }

    companion object {
        private val TAG = "MainActivityViewModel"
    }

}
