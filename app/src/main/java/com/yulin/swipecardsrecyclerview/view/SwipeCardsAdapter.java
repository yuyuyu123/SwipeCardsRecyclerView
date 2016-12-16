package com.yulin.swipecardsrecyclerview.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by YuLin on 2016/12/16 0016.
 */
public abstract class SwipeCardsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    protected List mList;

    public  SwipeCardsAdapter(List list) {
        mList = list;
    }

    public void deleteTopItem() {
        int position = this.getItemCount() - 1;
        this.mList.remove(position);
        this.notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }
}
