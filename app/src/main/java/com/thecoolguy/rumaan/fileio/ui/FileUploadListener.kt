package com.thecoolguy.rumaan.fileio.ui

import com.thecoolguy.rumaan.fileio.data.models.FileEntity

interface FileUploadListener {
    fun onFileUpload(fileEntity: FileEntity)
    fun onFileUploadError(exception: Exception)
}