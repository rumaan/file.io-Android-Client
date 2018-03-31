package com.thecoolguy.rumaan.fileio.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpUpload
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.thecoolguy.rumaan.fileio.utils.Constants
import com.thecoolguy.rumaan.fileio.utils.Utils
import java.io.FileInputStream


object Uploader {
    val TAG = Uploader::class.simpleName
    fun uploadFile(context: Context, fileUri: Uri, fileInputStream: FileInputStream) {
        val localFile = Utils.getFileDetails(context, fileUri)

        Constants.BASE_URL.httpUpload()
                .blob { request, url ->
                    Log.d(TAG, request.toString())
                    Blob(localFile.name, localFile.size, {
                        fileInputStream
                    })
                }
                .progress { readBytes, totalBytes ->

                }
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            val data = result.get()
                            Log.d(TAG, data)
                        }
                        is Result.Failure -> {
                            val exception = result.getException()
                            Log.e(TAG, exception.localizedMessage, exception)
                        }
                    }
                }

    }
}

data class Response(val success: Boolean,
                    val key: String,
                    val link: String,
                    val expiry: String,
                    val message: String,
                    val error: Int) {
    class Deserializer : ResponseDeserializable<Response> {
        override fun deserialize(content: String): Response? {
            return Gson().fromJson(content, Response::class.java)
        }
    }
}