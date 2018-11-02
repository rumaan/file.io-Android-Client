package com.thecoolguy.rumaan.fileio.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import androidx.work.WorkStatus
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.FileLoadListener
import com.thecoolguy.rumaan.fileio.network.createWorkRequest
import com.thecoolguy.rumaan.fileio.utils.Utils

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

class MainActivityViewModel : ViewModel() {
    /* Store the current chosen file instance */
    private lateinit var localFile: LocalFile

    /* Upload Work status */
    var uploadWorkStatus: LiveData<WorkStatus>? = null

    /**
     * Get the file from the database and save in the current view model state.
     */
    fun chooseFileFromUri(context: Context, fileUri: Uri) {
        val fileLoadListener = context as FileLoadListener
        Utils.getLocalFile(context, fileUri)
                ?.let {
                    localFile = it
                    fileLoadListener.onFileLoad(localFile)
                }
    }

    /**
     * Upload the file to server and save the response into the database.
     *
     * */
    fun uploadFile() {
        /* Enqueue file to be uploaded into WorkManager */
        val workRequest = createWorkRequest(localFile.uri.toString())
        WorkManager.getInstance()
                .enqueue(workRequest)
        uploadWorkStatus = WorkManager.getInstance().getStatusByIdLiveData(workRequest.id)
    }

    companion object {
        private const val TAG = "MainActivityViewModel"
    }

}
