package com.ryoga.k17124kk.intervaltimer;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class BallAnimator {

    static int duration = 448;

    static boolean isAnimation = false;


    public static void startAnimation(final float endX, final float endY, final ImageView ball) {

        //初期位置
        isAnimation = true;


        animation1(endX, endY, ball);

    }


    public static void animation1(final float movePosition_X, final float movePosition_Y, final ImageView ball) {
        ViewCompat
                .animate(ball)
                .translationX(movePosition_X / 2)
                .translationY(movePosition_Y - ball.getHeight())
                .setDuration(duration)
                .rotation(720)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (isAnimation) {
                            animation2(movePosition_X, movePosition_Y, ball);
                        }

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
    }

    public static void animation2(final float movePosition_X, final float movePosition_Y, final ImageView ball) {
        ViewCompat.animate(ball)
                .translationX(movePosition_X + ball.getWidth())
                .translationY(0)
                .setDuration(duration)
                .rotation(720)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (isAnimation) {
                            animation3(movePosition_X, movePosition_Y, ball);
                        }

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
    }

    public static void animation3(final float movePosition_X, final float movePosition_Y, final ImageView ball) {
        ViewCompat
                .animate(ball)
                .translationX(movePosition_X / 2)
                .translationY(movePosition_Y - ball.getHeight())
                .setDuration(duration)
                .rotation(-720)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (isAnimation) {
                            animation4(movePosition_X, movePosition_Y, ball);
                        }

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
    }

    public static void animation4(final float movePosition_X, final float movePosition_Y, final ImageView ball) {
        ViewCompat
                .animate(ball)
                .translationX(0 - ball.getWidth() * 2)
                .translationY(0)
                .setDuration(duration)
                .rotation(-720)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        if (isAnimation) {
                            animation1(movePosition_X, movePosition_Y, ball);
                        }

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                });
    }


    public static void cancelAnimation(ImageView ball) {
        isAnimation = false;
        resetPosition(ball);
    }

    public static void resetPosition(ImageView ball) {
        ViewCompat.animate(ball).translationX(0 - ball.getWidth() * 2).translationY(0).setDuration(1);
    }


}
