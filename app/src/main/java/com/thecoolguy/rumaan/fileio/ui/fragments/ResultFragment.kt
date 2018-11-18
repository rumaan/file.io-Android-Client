package com.thecoolguy.rumaan.fileio.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener
import com.thecoolguy.rumaan.fileio.utils.Utils.Android.copyTextToClipboard


class ResultFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_result, container, false)
        arguments?.also {
            val url = it.getString(PARAM_URL)
            val days = it.getInt(PARAM_DAYS)

            val linkText: TextView = view.findViewById(R.id.text_link)
            val daysText: TextView = view.findViewById(R.id.text_days)
            val btnDone: Button = view.findViewById(R.id.btn_done)

            linkText.text = url
            daysText.text = "Expires in: $days days"

            btnDone.setOnClickListener { listener?.onDone() }

            linkText.setOnClickListener {
                copyTextToClipboard(activity, "link", url)
                Toast.makeText(activity, activity?.getText(R.string.link_copy), Toast.LENGTH_SHORT)
                        .show()
            }
        }
        return view
    }

    companion object {
        private const val PARAM_URL = "url"
        private const val PARAM_DAYS = "days"
        val TAG: String = ResultFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(url: String, days: Int): ResultFragment {
            val fragment = ResultFragment()
            val bundle = Bundle().apply {
                putString(PARAM_URL, url)
                putInt(PARAM_DAYS, days)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
