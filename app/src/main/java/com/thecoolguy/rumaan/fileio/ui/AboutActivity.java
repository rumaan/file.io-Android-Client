package com.thecoolguy.rumaan.fileio.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.thecoolguy.rumaan.fileio.R;
import com.thecoolguy.rumaan.fileio.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.root_view)
    ConstraintLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.profile_img)
    ImageView profile;

    @BindView(R.id.about)
    TextView aboutText;

    @OnClick({R.id.github, R.id.gmail, R.id.twitter})
    void onSocialIconsClick(View view) {
        Intent intent = null;
        Uri uri;
        switch (view.getId()) {
            case R.id.github:
                uri = Consts.GITHUB_URL;
                intent = new Intent(Intent.ACTION_VIEW, uri);
                break;
            case R.id.twitter:
                uri = Consts.TWITTER_URL;
                intent = new Intent(Intent.ACTION_VIEW, uri);
                break;
            case R.id.gmail:
                String[] mailTo = new String[]{Consts.EMAIL};
                String subject = "Hello There! How are you doing?";
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, mailTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                break;
        }

        openIntent(intent);
    }

    private void openIntent(Intent intent) {
        if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Sorry, There's no app on your phone to do this task!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        /* Show Up button */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* Hide title from toolbar */
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* I mean come on its obvious that there's only one menu item */
        if (item.getItemId() == R.id.menu_open_source) {
            Snackbar.make(rootView, "Coming Soon!", Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_about, menu);
        return true;
    }
}
