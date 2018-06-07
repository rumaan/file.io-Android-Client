package com.thecoolguy.rumaan.fileio.ui.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener

class UploadFileFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private fun uploadFile() {
        listener?.onUploadFileClick()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_upload_file).setOnClickListener {
            uploadFile()
        }
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
        const val TAG = "UploadFileFragment"
        @JvmStatic
        fun newInstance() =
                UploadFileFragment()
    }
}
