package com.thecoolguy.rumaan.fileio.ui;

import android.app.Dialog;
import android.support.v4.app.Fragment;

/**
 * Handle click events from DialogFragments
 */

interface DialogClickListener {
    void onDialogPositiveClick(Dialog dialog, Fragment fragment);
}
