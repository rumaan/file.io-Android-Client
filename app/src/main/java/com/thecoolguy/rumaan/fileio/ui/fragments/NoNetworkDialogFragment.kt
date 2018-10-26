package com.thecoolguy.rumaan.fileio.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.Button
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.DialogClickListener

class NoNetworkDialogFragment : androidx.fragment.app.DialogFragment(), View.OnClickListener {

  private var mListener: DialogClickListener? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    try {
      mListener = activity as DialogClickListener?
    } catch (e: ClassCastException) {
      throw ClassCastException(activity!!.toString() + " must implement NoticeDialogListener")
    }

  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(activity!!)
    val inflater = activity!!.layoutInflater
    val view = inflater.inflate(R.layout.dialog_no_network_error, null)

    val okBtn = view.findViewById<Button>(R.id.btn_ok)
    okBtn.setOnClickListener(this)

    builder.setView(view)

    return builder.create()
  }

  override fun onClick(view: View) {
    mListener?.onDialogPositiveClick(dialog, this)
  }

}
