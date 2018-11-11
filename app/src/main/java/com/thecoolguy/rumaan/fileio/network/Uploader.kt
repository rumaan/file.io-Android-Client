package com.thecoolguy.rumaan.fileio.network

import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpUpload
import com.github.kittinunf.result.Result
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.utils.Utils
import com.thecoolguy.rumaan.fileio.utils.Utils.JSONParser.getDaysFromExpireString
import timber.log.Timber

object Uploader {

    private val TAG = Uploader::class.simpleName

    fun upload(localFile: LocalFile): Triple<Request, Response, Result<String, FuelError>> =
            "https://file.io"
                    .httpUpload()
                    .name { "file" }
                    .blob { _, _ -> Blob(localFile.name, localFile.size) { localFile.inputStream } }
                    .progress { readBytes, totalBytes ->
                        val progress = readBytes.toFloat() / totalBytes.toFloat()
                        Timber.d("Progress: $progress")
                    }
                    .responseString()
}

fun composeIntoFileEntity(
        response: com.thecoolguy.rumaan.fileio.data.models.Response,
        localFile: LocalFile): FileEntity =
        FileEntity(
                localFile.name, response.link, Utils.Date.currentDate,
                getDaysFromExpireString(response.expiry)
        )

