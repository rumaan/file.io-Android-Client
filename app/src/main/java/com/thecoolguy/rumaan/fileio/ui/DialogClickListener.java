package com.thecoolguy.rumaan.fileio.ui;

import android.app.Dialog;
import android.support.v4.app.Fragment;

/**
 * Handle click events from DialogFragments
 */

interface DialogClickListener {

  // TODO: change name
  void onDialogPositiveClick(Dialog dialog, Fragment dialogFragment);
}
