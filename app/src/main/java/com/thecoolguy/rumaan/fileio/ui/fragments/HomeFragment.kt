package com.thecoolguy.rumaan.fileio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.layout_main.*

class HomeFragment : androidx.fragment.app.Fragment() {

    /**
     *  Callback when a file is chosen and is stored in the local file object cache.
     * */
    private fun buttonChooseFileClick() {
        listener?.onUploadFileClick()
    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_main, container, false)
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        btn_choose_file.setOnClickListener {
            buttonChooseFileClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        const val TAG = "HomeFragment"
        @JvmStatic
        fun newInstance(/* Add Params later */) = HomeFragment()
    }
}
