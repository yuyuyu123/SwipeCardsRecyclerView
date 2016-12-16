package com.yulin.swipecardsrecyclerview.view;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yulin.swipecardsrecyclerview.utils.DensityUtils;
import com.yulin.swipecardsrecyclerview.utils.ScreenUtils;

import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YuLin on 2016/12/16 0016.
 */

public class SwipeCardsRecyclerView extends RecyclerView {
    private static final String TAG = SwipeCardsRecyclerView.class.getSimpleName();

    private float mTopViewX;
    private float mTopViewY;

    private float mTopViewOffsetX = 0;
    private float mTopViewOffsetY = 0;

    private float mTouchDownX;
    private float mTouchDownY;

    private boolean isCardsDelete = false;
    private boolean isCardsRightDeleted = false;

    private long mCardsOutDuration = 3000;

    public static final int CARDS_OUT_BORDER = 80;
    private float mCardsOutBorder;

    private OnCardsRemovedListener mOnCardsRemovedListener;

    private FrameLayout mDecorView;
    private int[] mDecorViewLocation = new int[2];

    private Map<View, Animator> mAnimatorsMap;

    private SwipeCardsAnimatorEffect mSwipeCardsAnimatorEffect;

    public SwipeCardsRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeCardsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeCardsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
    }

    private void initData() {
        mCardsOutBorder = getCardsOutBorder(CARDS_OUT_BORDER);
        mDecorView = (FrameLayout) ((Activity) getContext()).getWindow().getDecorView();
        mDecorView.getLocationOnScreen(mDecorViewLocation);
        mAnimatorsMap = new HashMap<>();
        mSwipeCardsAnimatorEffect = SwipeCardsAnimatorEffect.DEFAULT_EFFECT;
    }

    public void setSwipeCardsAnimatorEffect(SwipeCardsAnimatorEffect swipeCardsAnimatorEffect) {
        this.mSwipeCardsAnimatorEffect = swipeCardsAnimatorEffect;
    }

    public void setOnCardsRemovedListener(OnCardsRemovedListener onCardsRemovedListener) {
        this.mOnCardsRemovedListener = onCardsRemovedListener;
    }

    private int getCardsOutBorder(float dip) {
        return DensityUtils.dp2px(getContext(), dip);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        if (getChildCount() == 0) return super.onTouchEvent(e);
        View topView = getChildAt(getChildCount() - 1);
        float touchX = e.getX();
        float touchY = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mAnimatorsMap.containsKey(topView)) {
                    mAnimatorsMap.get(topView).cancel();
                    mAnimatorsMap.remove(topView);
                    mTopViewOffsetX = topView.getX();
                    mTopViewOffsetY = topView.getY();
                } else {
                    mTopViewX = topView.getX();
                    mTopViewY = topView.getY();
                    mTopViewOffsetX = 0;
                    mTopViewOffsetY = 0;
                }
                mTouchDownX = touchX;
                mTouchDownY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = touchX - mTouchDownX;
                float dy = touchY - mTouchDownY;
                topView.setX(mTopViewX + dx + mTopViewOffsetX);
                topView.setY(mTopViewY + dy + mTopViewOffsetY);
                updateNextCard((float) (Math.abs(topView.getX() - mTopViewX) * 0.2 / mCardsOutBorder + 0.8));
                break;
            case MotionEvent.ACTION_UP:
                mTouchDownX = 0;
                mTouchDownY = 0;
                swipeOutScreen(topView);
                break;
        }
        return super.onTouchEvent(e);
    }

    public void setCardsOutDuration(long mCardsOutDuration) {
        this.mCardsOutDuration = mCardsOutDuration;
    }

    private void updateNextCard(float value) {
        if (getChildCount() < 2) return;
        if (value > 1) value = 1;
        View cardView = getChildAt(getChildCount() - 2);
        cardView.setScaleX(value);
        cardView.setScaleX(value);
    }

    private void swipeOutScreen(final View view) {
        float targetX = 0;
        float targetY = 0;
        isCardsDelete = false;
        if (Math.abs(view.getX() - mTopViewX) < mCardsOutBorder) {
            targetX = mTopViewX;
            targetY = mTopViewY;
        } else if (view.getX() - mTopViewX > mCardsOutBorder) {
            isCardsDelete = true;
            isCardsRightDeleted = true;
            targetX = ScreenUtils.getScreenWidth(getContext()) * 2;
            if (mOnCardsRemovedListener != null) mOnCardsRemovedListener.onCardsRightRemoved(view,getChildCount() - 1);
        } else {
            isCardsDelete = true;
            isCardsRightDeleted = false;
            targetX = -ScreenUtils.getScreenWidth(getContext()) * 2;
            if (mOnCardsRemovedListener != null) mOnCardsRemovedListener.onCardsLeftRemoved(view,getChildCount() - 1);
        }
        View animView = view;
        TimeInterpolator interpolator = null;
        if (isCardsDelete) {
            animView = getMirrorView(view);
            float offsetX = getX() - mDecorView.getX();
            float offsetY = getY() - mDecorView.getY();
            targetY = calculateExitY(mTopViewX + offsetX, mTopViewY + offsetY, animView.getX(), animView.getY(), targetX);
            interpolator = new LinearInterpolator();
        } else {
            interpolator = new OvershootInterpolator();
        }

        ViewPropertyAnimator animator = animView.animate();
        animator.setDuration(mCardsOutDuration);
        animator.x(targetX);
        animator.y(targetY);
        animator.setInterpolator(interpolator);
        animator.setListener(getAnimatorListener(animView));
        animator.setUpdateListener(getAnimatorUpdateListener(animView));
    }

    private float calculateExitY(float x1, float y1, float x2, float y2, float x3) {
        return (y2 - y1) * (x3 - x1) / (x2 - x1) + y1;
    }

    private Animator.AnimatorListener getAnimatorListener(final View view) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(!isCardsDelete) {
                    mAnimatorsMap.put(view, animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(isCardsDelete) {
                    try {
                        mDecorView.removeView(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mAnimatorsMap.remove(view);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private ValueAnimator.AnimatorUpdateListener getAnimatorUpdateListener(final View view) {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateNextCard((float) (Math.abs(view.getX() - mTopViewX) * 0.2 / mCardsOutBorder + 0.8));
                switch (mSwipeCardsAnimatorEffect) {
                    case DEFAULT_EFFECT:
                        break;
                    case SCALE_IN_AND_OUT:
                        setViewScale(view, (float) animation.getAnimatedValue());
                        break;
                    case ALPHA_IN_AND_OUT:
                        setViewAlpha(view, (float)animation.getAnimatedValue());
                        break;
                    case ROTATION_IN_AND_OUT:
                        setViewRotation(view,(float)animation.getAnimatedValue());
                        break;
                    case SCALE_AND_ALPHA:
                        setViewScale(view, (float) animation.getAnimatedValue());
                        setViewAlpha(view, (float)animation.getAnimatedValue());
                        break;
                    case SCALE_AND_ROTATION:
                        setViewScale(view, (float) animation.getAnimatedValue());
                        setViewRotation(view,(float)animation.getAnimatedValue());
                        break;
                    case ALPHA_AND_ROTATION:
                        setViewAlpha(view, (float)animation.getAnimatedValue());
                        setViewRotation(view,(float)animation.getAnimatedValue());
                        break;
                }
            }
        };
    }

    private void setViewScale(View view, float value) {
        view.setScaleX(getHalfValue(value));
        view.setScaleY(getHalfValue(value));
    }

    private void setViewAlpha(View view, float value) {
        view.setAlpha(value);
    }

    private void  setViewRotation(View view, float value) {
        if(isCardsRightDeleted) {
            view.setRotation(value * 360);
        } else {
            view.setRotation(-value * 360);
        }
    }

    private float getHalfValue(float value) {
        return (value > 0.5) ? value : 0.5f;
    }

    private ImageView getMirrorView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        mirrorView.setAlpha(view.getAlpha());
        view.setVisibility(View.GONE);
        ((SwipeCardsAdapter) getAdapter()).deleteTopItem();
        mirrorView.setX(locations[0] - mDecorViewLocation[0]);
        mirrorView.setY(locations[1] - mDecorViewLocation[1]);
        mDecorView.addView(mirrorView, layoutParams);
        return mirrorView;
    }

    public interface OnCardsRemovedListener {
        void onCardsRightRemoved(View itemView, int position);

        void onCardsLeftRemoved(View itemView, int position);
    }

}
