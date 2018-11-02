package com.thecoolguy.rumaan.fileio.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.listeners.OnFragmentInteractionListener

class HomeActivity : AppCompatActivity(), OnFragmentInteractionListener {
    override fun onChooseFileClick() {

    }

    override fun onUploadFileClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NoActionBarTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }
}