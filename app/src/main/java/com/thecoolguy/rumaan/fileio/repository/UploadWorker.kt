package com.thecoolguy.rumaan.fileio.repository

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.httpUpload
import com.thecoolguy.rumaan.fileio.data.db.DatabaseHelper
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.models.File
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.Response
import com.thecoolguy.rumaan.fileio.ui.NotificationHelper
import com.thecoolguy.rumaan.fileio.utils.Utils.Date.currentDate
import com.thecoolguy.rumaan.fileio.utils.Utils.JSONParser.getDaysFromExpireString
import com.thecoolguy.rumaan.fileio.utils.getFile
import timber.log.Timber

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val KEY_URI = "file_uri"
        const val KEY_RESULT_URL = "file_url"
        const val KEY_RESULT_DAYS = "days"
    }

    private fun save(fileEntity: FileEntity) {
        val id = DatabaseHelper.saveToDatabase(fileEntity, UploadHistoryRoomDatabase.getInstance(applicationContext).uploadItemDao())
        Timber.d("Insert Id: $id")
    }


    private fun postNotification(fileEntity: FileEntity) {
        NotificationHelper().create(applicationContext, fileEntity)
    }

    fun upload(file: File): Result {
        val inputStream = applicationContext.contentResolver.openInputStream(file.uri)
        inputStream?.let {
            /*
             * Make sure the request the request is Synchronous, since WorkManager will run this in a Worker Thread.
             * */
            val (request, response, result) =
                    "https://file.io"
                            .httpUpload()
                            .name { "file" }
                            .blob { _, _ -> Blob(file.name, file.size.toLong()) { it } }
                            .progress { readBytes, totalBytes ->
                                val progress = readBytes.toFloat() / totalBytes.toFloat()
                                //  Timber.d("Progress: %f", progress)
                            }
                            .responseObject(Response.Deserializer())

            // TODO: handle FuelError
            Timber.d("Response: %s", result)
            val (res, _) = result
            res?.let {
                val days = getDaysFromExpireString(res.expiry)
                val fileEntity = FileEntity(file.name, res.link, currentDate, days)
                val output = workDataOf(KEY_RESULT_URL to res.link, KEY_RESULT_DAYS to days)
                /* Set output data */
                outputData = output

                /* Insert the result into DB */
                save(fileEntity)
                /* Send a notification about the upload */
                postNotification(fileEntity)

                return@let Result.SUCCESS
            }
        }
        return Result.FAILURE
    }

    override fun doWork(): Result {
        val input = inputData.getString(KEY_URI)
        val uri: Uri? = Uri.parse(input)

        uri?.let {
            val file: File? = getFile(applicationContext, it)
            if (file != null) {
                return upload(file)
            }
        }

        return Result.FAILURE
    }
}