package com.thecoolguy.rumaan.fileio.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.utils.Utils

class UploadHistoryListAdapter(
  private var context: Context,
  private var uploadList: List<FileEntity>
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
  private var composedList = mutableListOf<Any?>()

  private val date: Int = 0
  private val list: Int = 1

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): androidx.recyclerview.widget.RecyclerView.ViewHolder {
    return when (viewType) {
      date -> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.upload_history_item_date, parent, false)
        UploadHistoryListDateViewHolder(itemView)
      }
      else -> {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.upload_history_item_content, parent, false)
        UploadHistoryListItemViewHolder(itemView)
      }

    }
  }

  override fun getItemCount(): Int = composedList.size

  override fun onBindViewHolder(
          holder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
          position: Int
  ) {
    holder.apply {
      when (this.itemViewType) {
        date -> {
          (holder as UploadHistoryListDateViewHolder).date.text = composedList[position] as String
        }
        list -> {
          (holder as UploadHistoryListItemViewHolder).fileName.text =
              (composedList[position] as FileEntity).name
          holder.fileUrl.text = (composedList[position] as FileEntity).url
          holder.rootView.setOnClickListener {
            Utils.Android.copyTextToClipboard(
                context, "link", (composedList[position] as FileEntity).url
            )
            Toast.makeText(context, context.getText(R.string.link_copy), Toast.LENGTH_SHORT)
                .show()
          }
        }
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (composedList[position]) {
      is String -> date
      else -> list
    }
  }

  fun swapComposedList(list: MutableList<Any?>) {
    composedList = list
    notifyDataSetChanged()
  }

  fun swapList(list: List<FileEntity>) {
    uploadList = list
    notifyDataSetChanged()
  }

  fun getFileEntityIdAtPosition(position: Int): Long {
    return when (composedList[position]) {
      is FileEntity -> (composedList[position] as FileEntity).id
      else -> -1
    }
  }

  fun remoteAt(position: Int) {
    composedList.removeAt(position)
    notifyItemRemoved(position)
  }

  class UploadHistoryListItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    val fileName: TextView = itemView.findViewById(R.id.file_name)
    val fileUrl: TextView = itemView.findViewById(R.id.file_url)
    val rootView: ViewGroup = itemView.findViewById(R.id.root_view)
  }

  class UploadHistoryListDateViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    val date: TextView = itemView.findViewById(R.id.upload_item_date)
  }

}
