package com.thecoolguy.rumaan.fileio.data.repository

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.listeners.FileUploadListener
import com.thecoolguy.rumaan.fileio.listeners.FileUploadProgressListener
import com.thecoolguy.rumaan.fileio.utils.Utils
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.annotations.NotNull

object Uploader {

    private val TAG = Uploader::class.simpleName

    // TODO: fix this mess
    fun uploadFile(@NotNull localFile: LocalFile, uploadListener: FileUploadListener,
                   progressListener: FileUploadProgressListener) {
        // Async task thread
        Fuel.upload("https://file.io")
                .blob { _, _ ->
                    Blob(localFile.name, localFile.size, {
                        localFile.fileInputStream
                    })
                }
                .name {
                    "file"
                }
                .progress { readBytes, totalBytes ->
                    val p = (readBytes.toFloat() / totalBytes * 100).toInt()

                    // publish progress to all observer
                    progressListener.uploadProgress(p)
                    uploadListener.uploadProgress(p)
                }
                .responseObject(Response.Deserializer()) { _, _, result ->
                    // Close the stream
                    localFile.fileInputStream.close()
                    when (result) {
                        is Result.Success -> {
                            runBlocking {
                                val deferredFileEntity = async {
                                    val res = result.get()
                                    return@async FileEntity(localFile.name,
                                            res.link, Utils.Date.getCurrentDate(),
                                            Utils.JSONParser.getDaysFromExpireString(res.expiry))
                                }

                                // publish result to all listeners
                                val fileEntity = deferredFileEntity.await()
                                uploadListener.onFileUpload(fileEntity)
                            }

                        }
                        is Result.Failure -> {
                            val exception = result.getException()
                            uploadListener.onFileUploadError(exception)
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
