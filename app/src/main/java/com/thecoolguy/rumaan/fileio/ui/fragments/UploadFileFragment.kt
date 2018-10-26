package com.thecoolguy.rumaan.fileio.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.fragment_upload_file.btn_upload_file
import kotlinx.android.synthetic.main.fragment_upload_file.file_name

class UploadFileFragment : androidx.fragment.app.Fragment() {

  private var listener: OnFragmentInteractionListener? = null
  private var fileName: String? = null

  private fun uploadFile() {
    listener?.onUploadFileClick()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      fileName = it.getString(ARG_FILENAME)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_upload_file, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    btn_upload_file.setOnClickListener {
      uploadFile()
    }

    file_name.text = fileName
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is OnFragmentInteractionListener) {
      listener = context
    } else {
      throw RuntimeException("${context.toString()} must implement OnFragmentInteractionListener")
    }
  }

  companion object {
    const val ARG_FILENAME = "filename"
    const val TAG = "UploadFileFragment"

    @JvmStatic
    fun newInstance(fileName: String) =
      UploadFileFragment()
          .apply {
            arguments = Bundle().apply {
              putString(ARG_FILENAME, fileName)
            }
          }
  }
}

