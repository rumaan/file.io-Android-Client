package com.thecoolguy.rumaan.fileio.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;
import com.thecoolguy.rumaan.fileio.ui.OnUploadItemLongClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

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
            holder.days.setText(uploadItem.getDaysToExpire() + mCtx.getString(R.string.days));
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.file_name)
        TextView fileName;
        @BindView(R.id.file_url)
        TextView fileUrl;
        @BindView(R.id.days)
        TextView days;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_view)
        void onClick(View view) {
            int position = getAdapterPosition();

            ClipboardManager clipboardManager = (ClipboardManager) mCtx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("link", uploadItemList.get(position).getUrl());
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(mCtx, mCtx.getText(R.string.link_copy), Toast.LENGTH_SHORT).show();
            }
        }

        @OnLongClick(R.id.root_view)
        boolean onLongClick(View view) {
            // Handle long click to delete the item
            int position = getAdapterPosition();
            UploadItem uploadItem = uploadItemList.get(position);
            onUploadItemLongClickListener.onUploadItemLongClick(uploadItem);
            return true;
        }

    }
}
