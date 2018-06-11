package com.thecoolguy.rumaan.fileio.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryListAdapter
import com.thecoolguy.rumaan.fileio.viewmodel.UploadHistoryViewModel
import kotlinx.android.synthetic.main.activity_upload_history.*

class UploadHistoryActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "UploadHistoryActivity"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_history)
        supportActionBar?.title = getString(R.string.upload_history_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val viewModel = ViewModelProviders.of(this)
                .get(UploadHistoryViewModel::class.java)

        val adapter = UploadHistoryListAdapter(emptyList())
        upload_history_list.apply {
            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                    .apply {
                        setDrawable(getDrawable(R.drawable.divider_decor))
                    }
            addItemDecoration(dividerItemDecoration)
            setAdapter(adapter)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
        }


        viewModel.uploadList.observe(this, Observer { list ->
            list?.let {
                progress.visibility = View.GONE
                if (it.isEmpty())
                    no_uploads_view.visibility = View.VISIBLE
                else {
                    // set up recycler view and adapter
                    adapter.swapList(list)
                    upload_history_list.visibility = View.VISIBLE
                }
            }
        })
    }
}
