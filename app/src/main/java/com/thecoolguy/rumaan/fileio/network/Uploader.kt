package com.thecoolguy.rumaan.fileio.network

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.data.models.Response
import com.thecoolguy.rumaan.fileio.listeners.UploadListener
import com.thecoolguy.rumaan.fileio.repository.DisposableBucket
import com.thecoolguy.rumaan.fileio.utils.Utils
import com.thecoolguy.rumaan.fileio.utils.Utils.JSONParser.getDaysFromExpireString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

object Uploader {

    private val TAG = Uploader::class.simpleName

    fun upload(localFile: LocalFile, uploadListener: UploadListener) {
        val disposable = getUploadObservable(localFile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            val fileEntity = composeIntoFileEntity(it, localFile)
                            fileEntity?.let {
                                Log.d(TAG, it.toString())
                                uploadListener.onUpload(it)
                            }
                        },
                        onError = {
                            Log.e(TAG, it.localizedMessage, it)
                        }
                )
        DisposableBucket.add(disposable)
    }

    fun getUploadObservable(localFile: LocalFile): Single<Result<Response, FuelError>> {
        return Fuel.upload("https://file.io")
                .name { "file" }
                .blob { _, _ -> Blob(localFile.name, localFile.size, { localFile.inputStream }) }
                .progress { readBytes, totalBytes ->
                    val progress = readBytes.toFloat() / totalBytes.toFloat()
                    Log.d(TAG, "Progress: $progress")
                }
                .rx_object(Response.Deserializer())
                .subscribeOn(Schedulers.io())
    }

    fun composeIntoFileEntity(result: Result<Response, FuelError>,
                              localFile: LocalFile): FileEntity? {
        val response = result.component1()
        response?.let {
            // FIXME: something is fishy here when minifyEnabled true
            return FileEntity(localFile.name, response.link, Utils.Date.getCurrentDate(),
                    getDaysFromExpireString(response.expiry))
        }
        return null
    }
}
