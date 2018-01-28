package com.thecoolguy.rumaan.fileio.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.github.kittinunf.fuel.core.FuelError;
import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.adapters.UploadHistoryListAdapter;
import com.thecoolguy.rumaan.fileio.data.Upload;
import com.thecoolguy.rumaan.fileio.data.UploadItemViewModel;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

public class UploadHistoryActivity extends AppCompatActivity implements OnUploadItemLongClickListener, Upload {
    private static final String TAG = "UploadHistoryActivity";

    @BindView(R.id.no_uploads_view)
    View noUploadsView;
    @BindView(R.id.list)
    TimeLineRecyclerView recyclerView;
    private UploadItemViewModel uploadItemViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_history);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.upload_history_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final UploadHistoryListAdapter uploadHistoryListAdapter = new UploadHistoryListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        uploadItemViewModel = ViewModelProviders.of(this).get(UploadItemViewModel.class);
        uploadItemViewModel
                .getUploadHistoryList()
                .observe(this, new Observer<List<UploadItem>>() {
                    @Override
                    public void onChanged(@Nullable List<UploadItem> uploadItems) {
                        if ((uploadItems == null) || uploadItems.isEmpty()) {
                            noUploadsView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            noUploadsView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            uploadHistoryListAdapter.setUploadItemList(uploadItems);
                            recyclerView.addItemDecoration(getSectionCallback(uploadItemViewModel.getUploadHistoryList().getValue()));
                        }
                    }
                });
        recyclerView.setAdapter(uploadHistoryListAdapter);
        uploadHistoryListAdapter.setOnUploadItemLongClickListener(this);


        LayoutAnimationController layoutAnimationController =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<UploadItem> items) {

        return new RecyclerSectionItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int position) {
                // Same section if the dates are same
                return items.get(position).getDate().equals(items.get(position - 1).getDate());
            }

            @org.jetbrains.annotations.Nullable
            @Override
            public SectionInfo getSectionHeader(int i) {
                String days = String.valueOf(items.get(i).getDaysToExpire()) + getString(R.string.days_rem);
                return new SectionInfo(items.get(i).getDate(), days);
            }
        };
    }

    @Override
    public void onUploadItemLongClick(UploadItem uploadItem) {
        uploadItemViewModel.delete(uploadItem, this);
    }

    @Override
    public void onUpload(String result) {

    }

    @Override
    public void progress(int progress) {

    }

    @Override
    public void onError(FuelError error) {

    }

    @Override
    public void onDelete() {
        Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
    }
}
