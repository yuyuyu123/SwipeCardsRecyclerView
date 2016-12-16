package com.yulin.swipecardsrecyclerview.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YuLin on 2016/12/16 0016.
 */
public class SwipeCardsLayoutManager extends RecyclerView.LayoutManager {

    @Override public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        for (int i = 0; i < getItemCount(); i++) {
            View childView = recycler.getViewForPosition(i);
            measureChildWithMargins(childView,0,0);
            addView(childView);
            int width = getDecoratedMeasuredWidth(childView);
            int height = getDecoratedMeasuredHeight(childView);
            layoutDecorated(childView, 0,0,width, height);
            if(i < getItemCount() - 1) {
                childView.setScaleX(0.8f);
                childView.setScaleY(0.8f);
            }
        }
    }


}
