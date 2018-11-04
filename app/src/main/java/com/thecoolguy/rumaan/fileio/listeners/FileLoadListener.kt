package com.thecoolguy.rumaan.fileio.listeners

import com.thecoolguy.rumaan.fileio.data.models.LocalFile

interface FileLoadListener {
    fun onFileLoad(localFile: LocalFile)
}