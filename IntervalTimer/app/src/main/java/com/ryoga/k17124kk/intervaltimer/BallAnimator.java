package com.ryoga.k17124kk.intervaltimer;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class BallAnimator {


    public static void startAnimation(final float width, final float height, final ImageView ball) {

        //初期位置
        ViewCompat.animate(ball).translationX(0).translationY(0).setDuration(1);

        int sum = 0;

        Log.d("MYE", "motion");

        for (int i = 0; sum <= width; i++) {
            Log.d("MYE", Math.cos(i) + "::" + Math.sin(i));
            sum += (int) Math.cos(i);
        }

        ViewCompat.animate(ball).translationX(width).translationY(height).setDuration(1000).setListener(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View view) {
                startAnimation(width, height, ball);
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).start();

    }
}
