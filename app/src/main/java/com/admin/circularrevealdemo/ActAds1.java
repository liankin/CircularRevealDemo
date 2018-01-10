package com.admin.circularrevealdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/9.
 */

public class ActAds1 extends AppCompatActivity {

    @BindView(R.id.btn_ads)
    TextView btnAds;
    @BindView(R.id.layout_container)
    RelativeLayout layoutContainer;
    @BindView(R.id.img_big_image)
    ImageView imgBigImage;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.layout_content)
    RelativeLayout layoutContent;

    private  final Context mContext = ActAds1.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ads1);
        ButterKnife.bind(this);

        initViews();
    }

    @OnClick({R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                backActivity();
                break;
        }
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        //若版本号大于5.0, 则执行圆形打开动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation();
            Fade fade = new Fade();
            getWindow().setReturnTransition(fade);
            fade.setDuration(300);
        } else {
            showViews();
        }
    }

    /**
     * 显示视图
     */
    private void showViews() {
        AnimationSet aset= new AnimationSet(true);
        AlphaAnimation aa= new AlphaAnimation(0,1);
        aa.setDuration(800);
        aset.addAnimation(aa);
        imgBigImage.startAnimation(aset);
        imgBigImage.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animation.setDuration(300);
        //imgBigImage.startAnimation(animation);
        tvText.startAnimation(animation);
        imgClose.setAnimation(animation);
        tvText.setVisibility(View.VISIBLE);
        imgClose.setVisibility(View.VISIBLE);
    }

    /**
     * 入场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    /**
     * 圆形显示动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        int cx = (layoutContainer.getLeft() + layoutContainer.getRight()) / 2;
        int cy = (layoutContainer.getTop() + layoutContainer.getBottom()) / 2;
        int startRadius = btnAds.getWidth() / 2;
        float finalRadius = (float) Math.hypot(layoutContainer.getWidth(), layoutContainer.getHeight());
        // 与入场动画的区别就是圆圈起始和终止的半径相反
        Animator anim = ViewAnimationUtils.createCircularReveal(layoutContainer, cx, cy, startRadius, finalRadius);
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                layoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutContainer.setVisibility(View.VISIBLE);
                showViews();
            }
        });
        anim.start();
    }

    /**
     * 退出页面
     */
    public void backActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //执行关闭动画
            setupExitAnimation();
        } else {
            onBackPressed();
        }
    }

    /**
     * 退出动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        int cx = (layoutContainer.getLeft() + layoutContainer.getRight()) / 2;
        int cy = (layoutContainer.getTop() + layoutContainer.getBottom()) / 2;
        int initialRadius = layoutContainer.getWidth();
        float finalRadius = btnAds.getWidth() / 2;
        // 与入场动画的区别就是圆圈起始和终止的半径相反
        Animator anim = ViewAnimationUtils.createCircularReveal(layoutContainer, cx, cy, initialRadius, finalRadius);
        anim.setDuration(300);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                layoutContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                layoutContent.setVisibility(View.GONE);
                onBackPressed();
                layoutContainer.setVisibility(View.GONE);//先返回再设置GONE，不然btnAds无动画效果
            }
        });
        anim.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
