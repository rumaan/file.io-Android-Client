package com.thecoolguy.rumaan.fileio.listeners

import android.app.Dialog

interface DialogClickListener {
    fun onDialogPositiveClick(
            dialog: Dialog,
            dialogFragment: androidx.fragment.app.Fragment
    )
}