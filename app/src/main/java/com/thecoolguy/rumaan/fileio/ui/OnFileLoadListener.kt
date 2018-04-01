package com.thecoolguy.rumaan.fileio.ui

import com.thecoolguy.rumaan.fileio.data.models.LocalFile

interface OnFileLoadListener {
    fun onFileLoad(localFile: LocalFile)
}