package com.thecoolguy.rumaan.fileio.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun androidx.fragment.app.FragmentManager.addFragment(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        tag: String? = null
) {
    beginTransaction()
            .add(containerId, fragment, tag)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .addToBackStack(tag)
            .commit()
}

fun androidx.fragment.app.FragmentManager.replaceFragment(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        tag: String? = null
) {
    beginTransaction()
            .replace(containerId, fragment, tag)
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(tag)
            .commit()
}