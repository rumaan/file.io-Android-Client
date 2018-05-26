package com.thecoolguy.rumaan.fileio.listeners

import android.app.Dialog
import android.support.v4.app.Fragment

interface DialogClickListener {
    fun onDialogPositiveClick(dialog: Dialog, dialogFragment: Fragment)
}