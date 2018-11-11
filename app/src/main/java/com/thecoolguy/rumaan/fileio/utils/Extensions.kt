package com.thecoolguy.rumaan.fileio.utils

import android.content.Context
import android.view.View
import android.widget.Toast


infix fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

infix fun View.toggleClickable(v: Boolean) {
    this.isClickable = v
}

