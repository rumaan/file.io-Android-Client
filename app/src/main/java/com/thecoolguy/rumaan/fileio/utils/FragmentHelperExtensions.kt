package com.thecoolguy.rumaan.fileio.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

fun FragmentManager.addFragment(
  containerId: Int,
  fragment: Fragment,
  tag: String? = null
) {
  beginTransaction()
      .add(containerId, fragment, tag)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
      .addToBackStack(tag)
      .commit()
}

fun FragmentManager.replaceFragment(
  containerId: Int,
  fragment: Fragment,
  tag: String? = null
) {
  beginTransaction()
      .replace(containerId, fragment, tag)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
      .addToBackStack(tag)
      .commit()
}