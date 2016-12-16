package com.yulin.swipecardsrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yulin.swipecardsrecyclerview.view.SwipeCardsAnimatorEffect;
import com.yulin.swipecardsrecyclerview.view.SwipeCardsLayoutManager;
import com.yulin.swipecardsrecyclerview.view.SwipeCardsRecyclerView;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by YuLin on 2016/12/16 0016.
 */
public class MainActivity extends AppCompatActivity implements SwipeCardsRecyclerView.OnCardsRemovedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeCardsRecyclerView mRecyclerView;
    private List<Integer> mList = new ArrayList<>();
    private CustomSwipeCardsAdapter mAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initList();
    }

    private void initRecyclerView() {
        mRecyclerView = (SwipeCardsRecyclerView) findViewById(R.id.id_recycler_view);
        mRecyclerView.setLayoutManager(new SwipeCardsLayoutManager());
        mAdapter = new CustomSwipeCardsAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setCardsOutDuration(1000);
        mRecyclerView.setSwipeCardsAnimatorEffect(SwipeCardsAnimatorEffect.ALPHA_AND_ROTATION);
        mRecyclerView.setOnCardsRemovedListener(this);
    }

    private void initList() {
        mList.add(R.mipmap.first);
        mList.add(R.mipmap.second);
        mList.add(R.mipmap.third);
        mList.add(R.mipmap.fourth);
        mList.add(R.mipmap.fifth);
        mList.add(R.mipmap.sixth);
        mList.add(R.mipmap.seventh);
        mList.add(R.mipmap.eighth);
        mList.add(R.mipmap.ninth);
        mList.add(R.mipmap.tenth);
        mRecyclerView.getAdapter().notifyDataSetChanged();

    }

    @Override public void onCardsRightRemoved(View itemView, int position) {
        Log.e(TAG, "onCardsRightRemoved:" + position);
    }

    @Override public void onCardsLeftRemoved(View itemView, int position) {
        Log.e(TAG, "onCardsLeftRemoved:"  + position);
    }
}
