package com.thecoolguy.rumaan.fileio.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.UploadItem;

import java.util.List;

public class UploadHistoryListAdapter extends RecyclerView.Adapter<UploadHistoryListAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<UploadItem> uploadItemList;

    public UploadHistoryListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.upload_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (uploadItemList != null) {
            UploadItem uploadItem = uploadItemList.get(position);
            holder.fileName.setText(uploadItem.getFileName());
            holder.fileUrl.setText(uploadItem.getUrl());
        }
    }

    public void setUploadItemList(List<UploadItem> uploadItemList) {
        this.uploadItemList = uploadItemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return uploadItemList == null ? 0 : uploadItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName;
        private TextView fileUrl;

        ViewHolder(View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.file_name);
            fileUrl = itemView.findViewById(R.id.url);
        }
    }
}
