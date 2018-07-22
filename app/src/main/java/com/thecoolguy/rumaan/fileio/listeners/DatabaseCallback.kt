package com.thecoolguy.rumaan.fileio.listeners

import com.thecoolguy.rumaan.fileio.data.models.FileEntity

interface DatabaseCallback {
  fun onSave(
    fileEntity: FileEntity,
    id: Long
  )
}