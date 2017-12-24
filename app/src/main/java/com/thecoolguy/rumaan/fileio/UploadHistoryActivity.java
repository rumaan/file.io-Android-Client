package com.thecoolguy.rumaan.fileio;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.thecoolguy.rumaan.fileio.adapters.UploadHistoryListAdapter;
import com.thecoolguy.rumaan.fileio.data.UploadItem;
import com.thecoolguy.rumaan.fileio.data.UploadItemViewModel;

import java.util.List;

public class UploadHistoryActivity extends AppCompatActivity {

    private UploadItemViewModel uploadItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.upload_history_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.list);
        final UploadHistoryListAdapter uploadHistoryListAdapter = new UploadHistoryListAdapter(this);
        recyclerView.setAdapter(uploadHistoryListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);

        uploadItemViewModel = ViewModelProviders.of(this).get(UploadItemViewModel.class);
        uploadItemViewModel.getUploadHistoryList().observe(this, new Observer<List<UploadItem>>() {
            @Override
            public void onChanged(@Nullable List<UploadItem> uploadItems) {
                uploadHistoryListAdapter.setUploadItemList(uploadItems);
            }
        });

    }
}
