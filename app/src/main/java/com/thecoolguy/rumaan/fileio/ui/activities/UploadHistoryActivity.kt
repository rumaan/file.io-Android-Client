package com.thecoolguy.rumaan.fileio.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.repository.ClearHistoryWorker
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
            R.id.menu_clear_history -> clearHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearHistory() {
        val work = OneTimeWorkRequestBuilder<ClearHistoryWorker>()
                .build()
        WorkManager.getInstance()
                .enqueue(work)
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
            TransitionManager.beginDelayedTransition(parent_history)
            progress.visibility = View.INVISIBLE
            /* Group the list by Date and make a linear list out of map */
            val composedList = mutableListOf<Any?>()
            list?.let {
                if (it.isEmpty()) {
                    no_uploads_view.visibility = View.VISIBLE
                    upload_history_list.visibility = View.INVISIBLE
                } else {
                    upload_history_list.visibility = View.VISIBLE
                    no_uploads_view.visibility = View.INVISIBLE

                    it.groupBy {
                        it.date
                    }.flatMap {
                        listOf(it.key, it.value)
                    }.forEach {
                        when (it) {
                            is String -> {
                                composedList.add(it)
                            }
                            is List<*> -> {
                                it.forEach { item ->
                                    composedList.add(item)
                                }
                            }
                        }
                    }
                    adapter.swapComposedList(composedList)
                }
            }
        })
    }
}
