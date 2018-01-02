package com.thecoolguy.rumaan.fileio;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.animation.OvershootInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.root)
    ConstraintLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_initial);

        ButterKnife.bind(this);


        final ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this, R.layout.activity_about);

        Transition imageTransition = new AutoTransition();
        imageTransition.setDuration(400);
        imageTransition.addTarget(R.id.image);
        imageTransition.setInterpolator(new FastOutLinearInInterpolator());


        Transition viewPagerTransition = new ChangeBounds();
        viewPagerTransition.setStartDelay(80);
        viewPagerTransition.setInterpolator(new OvershootInterpolator());
        viewPagerTransition.addTarget(R.id.viewpager_root);
        viewPagerTransition.setDuration(300);

        final TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(viewPagerTransition);
        transitionSet.addTransition(imageTransition);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionManager.beginDelayedTransition(rootView, transitionSet);
                        constraintSet.applyTo(rootView);
                    }
                });
            }
        }, 50);


    }
}
