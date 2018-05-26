package com.thecoolguy.rumaan.fileio.listeners

import com.thecoolguy.rumaan.fileio.data.LocalFile

interface FileLoadListener {
    fun onFileLoad(localFile: LocalFile)
}