package com.nick.mobile.dev.videostreaming;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;

public class Timer {

    public static void timer(int time, Runnable runnable){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(MainActivity.timerView, "alpha", 1);
        objectAnimator.setDuration(time);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runnable.run();
            }
        });
    }

}
