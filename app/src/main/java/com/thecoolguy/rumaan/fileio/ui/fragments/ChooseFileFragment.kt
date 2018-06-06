package com.thecoolguy.rumaan.fileio.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.thecoolguy.rumaan.fileio.R

class ChooseFileFragment : Fragment() {
    /**
     *  Callback when a file is chosen and is stored in the local file object cache.
     * */
    fun buttonChooseFileClick() {
        listener?.onChooseFileClick()
    }


    private var listener: OnFragmentInteractionListener? = null
    private var chooseButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseButton = view.findViewById(R.id.btn_choose_file)

        chooseButton?.setOnClickListener {
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

    interface OnFragmentInteractionListener {
        fun onChooseFileClick()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChooseFileFragment()
    }
}
