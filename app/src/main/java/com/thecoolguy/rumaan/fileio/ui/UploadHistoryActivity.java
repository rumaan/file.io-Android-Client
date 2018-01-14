package com.thecoolguy.rumaan.fileio.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.thecoolguy.rumaan.fileio.utils.MaterialIn;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadHistoryActivity extends AppCompatActivity implements OnUploadItemLongClickListener, Upload {
    private static final String TAG = "UploadHistoryActivity";

    @BindView(R.id.no_uploads_view)
    View noUploadsView;
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


        final RecyclerView recyclerView = findViewById(R.id.list);
        final UploadHistoryListAdapter uploadHistoryListAdapter = new UploadHistoryListAdapter(this);

        uploadItemViewModel = ViewModelProviders.of(this).get(UploadItemViewModel.class);
        uploadItemViewModel
                .getUploadHistoryList()
                .observe(this, new Observer<List<UploadItem>>() {
                    @Override
                    public void onChanged(@Nullable List<UploadItem> uploadItems) {
                        if ((uploadItems == null) || uploadItems.isEmpty()) {
                            noUploadsView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            MaterialIn.animate(noUploadsView);
                        } else {
                            noUploadsView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            uploadHistoryListAdapter.setUploadItemList(uploadItems);
                        }
                    }
                });

        recyclerView.setAdapter(uploadHistoryListAdapter);

        uploadHistoryListAdapter.setOnUploadItemLongClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_anim_fall_down);
        recyclerView.setLayoutAnimation(layoutAnimationController);

    }

    @Override
    public void onUploadItemLongClick(UploadItem uploadItem) {
        Log.d(TAG, "onUploadItemLongClick: ");
        // Upload item in history long click delete
        uploadItemViewModel.delete(uploadItem, this);
    }

    //TODO: abstract class for all this

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
