package com.example.draw_android.section05_canvas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.draw_android.R;


/**
 * Created by Chao on 2018-09-08.
 */

public class TitleTextWindow implements View.OnTouchListener {

    private Context mContext;
    private WindowManager wm;
    private LinearLayout linearLayout;
    private int downY;

    public TitleTextWindow(Context context) {
        mContext = context;
    }

    private android.os.Handler mHander = new android.os.Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                animDismiss();
        }
    };

    /**
     * 动画，从顶部弹出
     */
    private void animShow(){
        //使用动画从顶部弹出
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", -linearLayout.getMeasuredHeight(), 0);
        animator.setDuration(600);
        animator.start();
    }

    /**
     * 动画，从顶部收回
     */
    private void animDismiss(){
        if (linearLayout == null || linearLayout.getParent() == null) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getTranslationY(), -linearLayout.getMeasuredHeight());
        animator.setDuration(600);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //移除HeaderToast  (一定要在动画结束的时候移除,不然下次进来的时候由于wm里边已经有控件了，所以会导致卡死)
                if (null != linearLayout && null != linearLayout.getParent()) {
                    wm.removeView(linearLayout);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }

    /**
     * 向外部暴露显示的方法
     */
    public void show(){
        createTitleView();
        animShow();
        //3S后自动关闭
        mHander.sendEmptyMessageDelayed(20, 3000);
    }
    /**
     * 向外部暴露关闭的方法
     */
    public void dismiss(){
        animDismiss();
    }


    /**
     * 视图创建方法
     */
    private void createTitleView(){
        //准备Window要添加的View
        linearLayout = new LinearLayout(mContext);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        View titleView = View.inflate(mContext, R.layout.header_toast, null);//这里是你弹窗的UI
        // 为titleView设置Touch事件
        linearLayout.setOnTouchListener(this);
        linearLayout.addView(titleView);
        // 定义WindowManager 并且将View添加到WindowManagar中去
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wm_params = new WindowManager.LayoutParams();
        wm_params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //这里需要注意，因为不同系统版本策略不一，所以需要根据版本判断设置type，否则会引起崩溃。
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {//大于android SDK 7.1.1
            wm_params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wm_params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        wm_params.gravity =  Gravity.TOP;
        wm_params.x = 200;
        wm_params.y = 0;
        wm_params.format = -3;  // 会影响Toast中的布局消失的时候父控件和子控件消失的时机不一致，比如设置为-1之后就会不同步
        wm_params.alpha = 1f;
        linearLayout.measure(0, 0);
        wm_params.height = linearLayout.getMeasuredHeight();
        wm.addView(linearLayout, wm_params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getRawY();
                if (moveY - downY < 0) {//如果是向上滑动
                    linearLayout.setTranslationY(moveY - downY);
                }
                break;
            case MotionEvent.ACTION_UP:
                //达到一定比例后，松开手指将关闭弹窗
                if (Math.abs(linearLayout.getTranslationY()) > linearLayout.getMeasuredHeight() / 1.5) {
                    Log.e("TAG", "回弹");
                    animDismiss();
                } else {
                    linearLayout.setTranslationY(0);
                }
                break;
            default:
                break;
        }
        return true;
    }
}
