package com.example.com.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.com.xrecyclerview.view.XRecyclerView;

import java.util.ArrayList;

public class SwipeRefreshLayoutLinearActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private XRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    private int times = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperefresh_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        View header =   LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
        mRecyclerView.addHeaderView(header);

        mSwipeRefreshLayout.setColorSchemeColors(0xff0D98DE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTime ++;
                times = 0;
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        listData.clear();
                        for(int i = 0; i < 5 ;i++){
                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
                        }
                        mRecyclerView.setHasMore(false);
                        mRecyclerView.restoreFooter();
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }, 1000);            //refresh data here
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {}

            @Override
            public void onLoadMore() {
                if(times < 2){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mRecyclerView.stopLoadMore();
                            for(int i = 0; i < 15 ;i++){
                                listData.add("item" + (i + listData.size()) );
                            }
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.restoreFooter();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.noMoreLoading();
                        }
                    }, 1000);
                }
                times ++;
            }
        });

        listData = new  ArrayList<String>();
        for(int i = 0; i < 5 ;i++){
            listData.add("item" + (i + listData.size()) );
        }
        mAdapter = new MyAdapter(listData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
    }
}
