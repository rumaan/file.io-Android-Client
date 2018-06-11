package com.thecoolguy.rumaan.fileio.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.thecoolguy.rumaan.fileio.R
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

class UploadHistoryListAdapter(private var uploadList: List<FileEntity>) :
        RecyclerView.Adapter<UploadHistoryListAdapter.HistoryListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.upload_item, parent, false)
        return HistoryListViewHolder(itemView)
    }

    override fun getItemCount(): Int = uploadList.size

    override fun onBindViewHolder(holder: HistoryListViewHolder, position: Int) {
        holder.apply {
            fileName.text = uploadList[position].name
            fileUrl.text = uploadList[position].url
        }
    }

    fun swapList(list: List<FileEntity>) {
        uploadList = list
        notifyDataSetChanged()
    }

    class HistoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.file_name)
        val fileUrl: TextView = itemView.findViewById(R.id.file_url)
    }
}
