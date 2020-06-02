package com.android.life.testbannerlib;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bannerlib.snap.ScrollLinearHelper;
import com.android.bannerlib.snap.ScrollPageHelper;
import com.android.bannerlib.snap.ScrollSnapHelper;
import com.android.life.R;

import java.util.ArrayList;


public class SevenBannerBarActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private RecyclerView mRecyclerView4;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.recyclerView3);
        mRecyclerView4 = (RecyclerView) findViewById(R.id.recyclerView4);
        initRecyclerView();
        initRecyclerView2();
        initRecyclerView3();
        initRecyclerView4();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        ScrollLinearHelper snapHelper = new ScrollLinearHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        SnapAdapterBannerBar adapter = new SnapAdapterBannerBar(this);
        mRecyclerView.setAdapter(adapter);
        adapter.addAll(getData());
    }

    private void initRecyclerView2() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(manager);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView2);
        SnapAdapterBannerBar adapter = new SnapAdapterBannerBar(this);
        mRecyclerView2.setAdapter(adapter);
        adapter.addAll(getData());
    }


    private void initRecyclerView3() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView3.setLayoutManager(manager);
        ScrollPageHelper snapHelper = new ScrollPageHelper(Gravity.START,false);
        snapHelper.attachToRecyclerView(mRecyclerView3);
        SnapAdapterBannerBar adapter = new SnapAdapterBannerBar(this);
        mRecyclerView3.setAdapter(adapter);
        adapter.addAll(getData());
    }


    private void initRecyclerView4() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView4.setLayoutManager(manager);
        ScrollSnapHelper snapHelper = new ScrollSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView4);
        SnapAdapterBannerBar adapter = new SnapAdapterBannerBar(this);
        mRecyclerView4.setAdapter(adapter);
        adapter.addAll(getData());
    }



    private ArrayList<String> getData(){
        for (int a=0 ; a<20 ; a++){
            data.add("测试数据"+a);
        }
        return data;
    }


}
