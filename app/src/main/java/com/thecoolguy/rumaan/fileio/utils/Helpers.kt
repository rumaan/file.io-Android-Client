package com.thecoolguy.rumaan.fileio.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.thecoolguy.rumaan.fileio.data.models.File
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.Response
import com.thecoolguy.rumaan.fileio.utils.Utils.Date.currentDate
import com.thecoolguy.rumaan.fileio.utils.Utils.JSONParser.getDaysFromExpireString
import timber.log.Timber


fun getFileMetaData(context: Context, uri: Uri) {
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use { it ->
        if (it.moveToFirst()) {
            val displayName: String = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            Timber.i("File Name: %s", displayName)

            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            val size: String = if (!it.isNull(sizeIndex)) {
                it.getString(sizeIndex)
            } else {
                "Unknown"
            }

            Timber.i("File Size: %s", size)
        }
    }

}

fun getFile(context: Context, uri: Uri): File? {
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    var file: File? = null
    cursor?.use {
        if (it.moveToFirst()) {
            val name: String = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            val size: String = if (!it.isNull(sizeIndex)) {
                it.getString(sizeIndex)
            } else {
                "Unknown"
            }
            file = File(name, size, uri)
        }
    }
    return file
}

fun composeIntoFileEntity(file: File, response: Response) = FileEntity(file.name, file.uri.toString(), currentDate, getDaysFromExpireString(response.expiry))


