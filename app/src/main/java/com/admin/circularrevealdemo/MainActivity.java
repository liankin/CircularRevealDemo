package com.admin.circularrevealdemo;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 版本号大于5.0, 支持Material Design的动画效果.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_open_search)
    TextView btnOpenSearch;
    @BindView(R.id.btn_search_cancel)
    TextView btnSearchCancel;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.btn_fab)
    FloatingActionButton btnFab;

    private boolean isOpenSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isOpenSearch = false;
    }

    @OnClick({R.id.btn_open_search, R.id.btn_search_cancel, R.id.btn_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open_search:
                if (isOpenSearch) {
                    hideLayoutSearch();
                } else {
                    showLayoutSearch();
                }
                break;
            case R.id.btn_search_cancel:
                hideLayoutSearch();
                break;
            case R.id.btn_fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, btnFab, btnFab.getTransitionName());
                    startActivity(new Intent(MainActivity.this, ActAds.class), options.toBundle());
                } else {
                    startActivity(new Intent(MainActivity.this, ActAds.class));
                }
                break;
        }
    }

    /**
     * 显示搜索视图
     */
    public void showLayoutSearch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(
                    layoutSearch, layoutSearch.getWidth() - 56, 23, 0,
                    (float) Math.hypot(layoutSearch.getWidth(), layoutSearch.getHeight())
            );
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    layoutSearch.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isOpenSearch = true;
                    showSoftInput();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animator.setDuration(300);
            animator.start();
        } else {
            layoutSearch.setVisibility(View.VISIBLE);
            showSoftInput();
        }
    }

    /**
     * 隐藏搜索视图
     */
    public void hideLayoutSearch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Animator animator = ViewAnimationUtils.createCircularReveal(
                    layoutSearch,
                    layoutSearch.getWidth() - 56,
                    23,
                    //确定圆的半径（算长宽的斜边长，这样半径不会太短也不会太长
                    (float) Math.hypot(layoutSearch.getWidth(), layoutSearch.getHeight()),
                    0
            );
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    hideSoftInput();
                    layoutSearch.setVisibility(View.GONE);
                    isOpenSearch = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.setDuration(300);
            animator.start();
        } else {
            hideSoftInput();
            layoutSearch.setVisibility(View.GONE);
            isOpenSearch = false;
        }
    }

    /**
     * 强制显示键盘
     */
    private void showSoftInput() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏键盘
     */
    private void hideSoftInput() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(layoutSearch.getWindowToken(), 0);
    }
}
