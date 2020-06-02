package com.android.life.testbaseadapterlib.second;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.life.R;
import com.android.life.testbaseadapterlib.first.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends AppCompatActivity {

    private List<SecondBean> list;
    private RecyclerView recyclerView;
    private SecondAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_base_adapter);

        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initData();
        initRecycleView();
    }

    private void initData() {
        list = new ArrayList<>();
        for (int a = 0; a < 20; a++) {
            SecondBean bean = new SecondBean();
            bean.setTitle("这个是假数据" + a);
            list.add(bean);
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SecondAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setData(list);
        RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayoutManager.HORIZONTAL, 2, Color.GRAY);
        recyclerView.addItemDecoration(line);
    }


}
