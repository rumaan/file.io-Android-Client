package com.thecoolguy.rumaan.fileio.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

class UploadHistoryListAdapter(private var uploadList: List<FileEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var composedList = mutableListOf<Any?>()

    private val date: Int = 0
    private val list: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            date -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upload_history_item_date, parent, false)
                UploadHistoryListDateViewHolder(itemView)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upload_history_item_content, parent, false)
                UploadHistoryListItemViewHolder(itemView)
            }

        }
    }

    override fun getItemCount(): Int = composedList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.apply {
            when (this.itemViewType) {
                date -> {
                    (holder as UploadHistoryListDateViewHolder).date.text = composedList[position] as String
                }
                list -> {
                    (holder as UploadHistoryListItemViewHolder).fileName.text = (composedList[position] as FileEntity).name
                    holder.fileUrl.text = (composedList[position] as FileEntity).url
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

    class UploadHistoryListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.file_name)
        val fileUrl: TextView = itemView.findViewById(R.id.file_url)
    }

    class UploadHistoryListDateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.upload_item_date)
    }
}
