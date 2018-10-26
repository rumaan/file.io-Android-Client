package com.thecoolguy.rumaan.fileio.ui.activities

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.repository.ClearHistoryWorker
import com.thecoolguy.rumaan.fileio.repository.DELETE_TAG
import com.thecoolguy.rumaan.fileio.repository.DeleteSingleItemWorker
import com.thecoolguy.rumaan.fileio.repository.ID
import com.thecoolguy.rumaan.fileio.ui.SwipeToDeleteCallBack
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryListAdapter
import com.thecoolguy.rumaan.fileio.viewmodel.UploadHistoryViewModel
import kotlinx.android.synthetic.main.activity_upload_history.no_uploads_view
import kotlinx.android.synthetic.main.activity_upload_history.parent_history
import kotlinx.android.synthetic.main.activity_upload_history.progress
import kotlinx.android.synthetic.main.activity_upload_history.upload_history_list

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
    WorkManager
        .getInstance()
        .enqueue(work)
  }

  private fun removeHistoryItem(id: Long) {
    val inputData = Data.Builder()
        .putLong(ID, id)
        .build()

    val work = OneTimeWorkRequestBuilder<DeleteSingleItemWorker>()
        .setInputData(inputData)
        .addTag(DELETE_TAG)
        .build()

    WorkManager.getInstance()
        .enqueue(work)

    WorkManager
        .getInstance()
        .getStatusesByTag(DELETE_TAG)
        .observe(this, Observer { listOfWorkStatuses ->
            listOfWorkStatuses.let {
                if (it[0].state.isFinished)
                    Snackbar.make(
                            parent_history, "Item Removed Successfully!", Snackbar.LENGTH_SHORT
                    ).show()
            }
        })

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_upload_history)
    supportActionBar?.title = getString(R.string.upload_history_title)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    val viewModel = ViewModelProviders.of(this)
        .get(UploadHistoryViewModel::class.java)

    val adapter = UploadHistoryListAdapter(this, emptyList())

    // Handle swipe left to delete the item
    val swipeHandler = object : SwipeToDeleteCallBack(this) {
      override fun onSwiped(
              viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?,
              direction: Int
      ) {
        viewHolder?.let {
          val itemId = adapter.getFileEntityIdAtPosition(viewHolder.adapterPosition)
          Log.d(TAG, "ItemId = $itemId")

          // remove item from db
          removeHistoryItem(itemId)

          // check if adapter is empty
          if (adapter.itemCount == 1) {
            toggleToEmpty()
          }
        }
      }
    }
    val itemTouchHelper = ItemTouchHelper(swipeHandler)
    itemTouchHelper.attachToRecyclerView(upload_history_list)

    upload_history_list.apply {
      val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(context, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
          .apply {
            setDrawable(getDrawable(R.drawable.divider_decor))
          }
      layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
      addItemDecoration(dividerItemDecoration)
      setAdapter(adapter)
      layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down)
    }

    viewModel.uploadList.observe(this, Observer { list ->
      TransitionManager.beginDelayedTransition(parent_history)

      /* Group the list by Date and make a linear list out of map */
      val composedList = mutableListOf<Any?>()

      list?.let {
        if (it.isEmpty()) {
          toggleToEmpty()
        } else {
          upload_history_list.visibility = View.VISIBLE
          no_uploads_view.visibility = View.INVISIBLE

          /* Group the list based on dates Map(Date, List<FileEntities>) */
          it.groupBy {
            it.date
          }
              .toSortedMap() // Sort according to Chronological order
              .asIterable()
              .reversed()
              .flatMap {
                /* Transform the Map() into a linear List(String, List()) */
                listOf(it.key, it.value)
              }
              .forEach {
                /* Compose list based on the item type */
                when (it) {
                  is String -> {
                    composedList.add(it)
                  }
                  is List<*> -> {
                    /* TODO: replace this with Kotlin Operator (alternate for forEach) */
                    it.forEach { item ->
                      composedList.add(item)
                    }
                  }
                }
              }
          progress.visibility = View.INVISIBLE

          /* Swap the list in Adapter */
          adapter.swapComposedList(composedList)
        }
      }
    })
  }

  private fun toggleToEmpty() {
    progress.visibility = View.INVISIBLE
    no_uploads_view.visibility = View.VISIBLE
    upload_history_list.visibility = View.INVISIBLE
  }
}
