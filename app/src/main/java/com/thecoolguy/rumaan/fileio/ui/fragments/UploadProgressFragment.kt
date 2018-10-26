package com.thecoolguy.rumaan.fileio.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_upload_progress.btn_close

class UploadProgressFragment : androidx.fragment.app.Fragment() {

  private var listener: OnFragmentInteractionListener? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_upload_progress, container, false)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnFragmentInteractionListener) {
      listener = context
    } else
      throw RuntimeException("${context.toString()} must implement OnFragmentInteractionListener")
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    btn_close.setOnClickListener {
      listener?.onClose()
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() = UploadProgressFragment()
  }

}
