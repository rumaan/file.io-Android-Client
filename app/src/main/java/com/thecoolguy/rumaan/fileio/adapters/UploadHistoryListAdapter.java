package com.thecoolguy.rumaan.fileio.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thecoolguy.rumaan.fileio.OnUploadItemLongClickListener;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import java.util.List;

public class UploadHistoryListAdapter extends RecyclerView.Adapter<UploadHistoryListAdapter.ViewHolder> {
    private static final String TAG = "UploadHistoryAdapter";
    private LayoutInflater layoutInflater;
    private List<UploadItem> uploadItemList;
    private Context mCtx;

    private OnUploadItemLongClickListener onUploadItemLongClickListener;

    public UploadHistoryListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mCtx = context;
    }

    public void setOnUploadItemLongClickListener(OnUploadItemLongClickListener itemLongClickListener) {
        this.onUploadItemLongClickListener = itemLongClickListener;
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

        // TODO: use DiffUtil
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return uploadItemList == null ? 0 : uploadItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CardView rootView;
        private TextView fileName;
        private TextView fileUrl;

        ViewHolder(View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.file_name);
            fileUrl = itemView.findViewById(R.id.file_url);
            rootView = itemView.findViewById(R.id.root_view);
            rootView.setOnClickListener(this);

            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            ClipboardManager clipboardManager = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("link", uploadItemList.get(position).getUrl());
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(mCtx, mCtx.getText(R.string.link_copy), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            // Handle long click to delete the item
            int position = getAdapterPosition();
            UploadItem uploadItem = uploadItemList.get(position);
            onUploadItemLongClickListener.onUploadItemLongClick(uploadItem);

            return true;
        }
    }
}
