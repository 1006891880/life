package com.android.life.testbaseadapterlib.four;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.baseadapterlibrary.adapter.BaseAdapter;
import com.android.life.R;
import com.android.life.testbaseadapterlib.first.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;


public class FourActivity extends AppCompatActivity {

    private List<FourBean> list;
    private RecyclerView recyclerView;
    private FourAdapter adapter;

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
        for(int a=0 ; a<13 ; a++){
            FourBean bean = new FourBean();
            if(a==0){
                bean.setTitle("这个是头布局"+a);
                bean.setLocation(1);
            }else if(a==1){
                bean.setTitle("文本逻辑处理"+a);
                bean.setLocation(2);
            }else if(a==2){
                bean.setTitle("图片逻辑处理"+a);
                bean.setLocation(3);
            }else if(a==12){
                bean.setTitle("这个是底布局"+a);
                bean.setLocation(4);
            }else {
                bean.setTitle("文本逻辑处理"+a);
                bean.setLocation(2);
            }
            list.add(bean);
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FourAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setData(list);
        RecycleViewItemLine line = new RecycleViewItemLine(this,LinearLayoutManager.HORIZONTAL,2, Color.GRAY);
        recyclerView.addItemDecoration(line);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<FourBean> data = adapter.getData();
                Toast.makeText(FourActivity.this,data.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }
}
