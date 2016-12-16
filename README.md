# SwipeCardsRecyclerView
an android widget call SwipeCardsRecyclerView which based on RecyclerView.


How to use it?
Just like a stand RecyclerView:

Effect:
 	screenshot.gif
![image](https://github.com/yuyuyu123/SwipeCardsRecyclerView/blob/master/screenshot.gif)

  private void initRecyclerView() {
        mRecyclerView = (SwipeCardsRecyclerView) findViewById(R.id.id_recycler_view);
        mRecyclerView.setLayoutManager(new SwipeCardsLayoutManager());
        mAdapter = new CustomSwipeCardsAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setCardsOutDuration(1000);
        mRecyclerView.setSwipeCardsAnimatorEffect(SwipeCardsAnimatorEffect.ROTATION_IN_AND_OUT);
        mRecyclerView.setOnCardsRemovedListener(this);
    }
    
the SwipeCardsRecyclerView.OnCardsRemovedListener:
  @Override public void onCardsRightRemoved(View itemView, int position) {
        Log.e(TAG, "onCardsRightRemoved:" + position);
    }

    @Override public void onCardsLeftRemoved(View itemView, int position) {
        Log.e(TAG, "onCardsLeftRemoved:"  + position);
    }
