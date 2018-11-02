package com.thecoolguy.rumaan.fileio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import com.thecoolguy.rumaan.fileio.utils.Utils
import kotlinx.android.synthetic.main.fragment_upload_result.*

class UploadResultFragment : androidx.fragment.app.Fragment() {
    private var fileUrl: String? = null
    private var listener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fileUrl = it.getString(ARG_URL)
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_result, container, false)
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        btn_close.setOnClickListener {
            listener?.onClose()
        }
        link.text = fileUrl

        copy.setOnClickListener {
            Utils.Android.copyTextToClipboard(activity?.applicationContext, "Link", link.text.toString())
            Toast.makeText(context, getText(R.string.link_copy), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    companion object {
        const val ARG_URL = "FILE_UPLOAD_URL"
        const val TAG = "UploadResultFragment"
        @JvmStatic
        fun newInstance(url: String) =
                UploadResultFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_URL, url)
                    }
                }
    }
}
