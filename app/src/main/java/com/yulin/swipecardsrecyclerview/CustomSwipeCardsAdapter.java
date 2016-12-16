package com.yulin.swipecardsrecyclerview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulin.swipecardsrecyclerview.view.SwipeCardsAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16 0016.
 */

public class CustomSwipeCardsAdapter extends SwipeCardsAdapter<CustomSwipeCardsAdapter.CustomSwipeCardsViewHolder> {
    private Context mContext;
    private List<Integer> mList;
    private LayoutInflater mInflater;

    public CustomSwipeCardsAdapter(Context context,List<Integer> list) {
        super(list);
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public CustomSwipeCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View currentView = mInflater.inflate(R.layout.item, parent, false);
        return new CustomSwipeCardsViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(CustomSwipeCardsViewHolder holder, int position) {
        holder.setImg(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CustomSwipeCardsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImg;

        public CustomSwipeCardsViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView)  itemView.findViewById(R.id.id_img);
        }

        public void setImg(int res) {
            mImg.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), res));
        }
    }
}
