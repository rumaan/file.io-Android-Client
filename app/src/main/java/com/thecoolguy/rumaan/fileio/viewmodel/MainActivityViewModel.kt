package com.thecoolguy.rumaan.fileio.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener
import com.thecoolguy.rumaan.fileio.listeners.FileUploadProgressListener
import com.thecoolguy.rumaan.fileio.repository.Repository
import com.thecoolguy.rumaan.fileio.utils.Utils
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

class MainActivityViewModel : ViewModel() {
    /* Store the current chosen file instance */
    private lateinit var localFile: LocalFile

    /**
     * Get the file from the database and save in the current view model state.
     *
     *
     */
    fun chooseFileFromUri(context: Context, fileUri: Uri, fileLoadListener: FileLoadListener) {
        async(UI) {
            val deferredFile = bg {
                Utils.getLocalFile(context, fileUri)
            }

            localFile = deferredFile.await()
            localFile.let {
                fileLoadListener.onFileLoad(it)
            }
        }
    }

    /**
     * Upload the file to server and save the response into the database.
     *
     * @param listener Callback class where the upload callbacks must be published.
     * */
    fun uploadFile(listener: FileUploadProgressListener) {
        localFile?.let {
            // upload file
            Repository.getInstance().upload(it, listener)
            // TODO: Show progress window here.
        }
    }

    companion object {
        private val TAG = "MainActivityViewModel"
    }

}
