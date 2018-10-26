package com.thecoolguy.rumaan.fileio.listeners

import android.app.Dialog
import androidx.fragment.app.Fragment

interface DialogClickListener {
  fun onDialogPositiveClick(
    dialog: Dialog,
    dialogFragment: androidx.fragment.app.Fragment
  )
}