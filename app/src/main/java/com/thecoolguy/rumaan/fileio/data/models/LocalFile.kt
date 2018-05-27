package com.thecoolguy.rumaan.fileio.data.models

import android.net.Uri
import java.io.InputStream

data class LocalFile(val name: String, val size: Long,
                     val uri: Uri,
                     val inputStream: InputStream)