package com.thecoolguy.rumaan.fileio.listeners

import com.thecoolguy.rumaan.fileio.data.models.FileEntity

interface UploadListener {
    fun onUpload(fileEntity: FileEntity)
    fun progress(progress: Int)
    fun onComplete(fileEntity: FileEntity)
}