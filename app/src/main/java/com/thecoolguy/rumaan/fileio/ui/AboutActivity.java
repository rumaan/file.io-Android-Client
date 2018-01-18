package com.thecoolguy.rumaan.fileio.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ScrollView;

import com.thecoolguy.rumaan.fileio.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.root_view)
    ConstraintLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        /* Show UP button */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* Hide title from toolbar */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
