package com.android.life.testbaseadapterlib.five;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.baseadapterlibrary.itemType.RecyclerArrayAdapter;
import com.android.life.R;

import java.util.ArrayList;
import java.util.List;



public class FiveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FiveOneAdapter adapter;
    private FiveSecondAdapter secondAdapter;

    private boolean isEidt = false;
    private boolean isEidtSecond = false;
    private boolean isAllSelect = false;
    private boolean isAllSelectSecond = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_base_adapter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new FiveOneAdapter(this);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<Bean> beans = new ArrayList<>();
        for (int a=0 ; a<5 ; a++){
            Bean bean = new Bean();
            bean.setName("name"+a);
            bean.setState(false);
            beans.add(bean);
        }
        adapter.addAll(beans);
        adapter.setOnChildClickListener(new FiveOneAdapter.OnChildClickListener() {
            @Override
            public void OnChildClick(View v, int position) {
                Bean bean = adapter.getAllData().get(position);
                bean.setState(!bean.isState());
                adapter.notifyItemInserted(position);
            }
        });

        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return null;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        adapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return null;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });

        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View inflate = LayoutInflater.from(FiveActivity.this).inflate(R.layout.header_five1_viewbaseadapter, null);
                return inflate;
            }

            @Override
            public void onBindView(View headerView) {
                TextView tv_1 = (TextView) headerView.findViewById(R.id.tv_1);
                final CheckBox checkBox = (CheckBox) headerView.findViewById(R.id.cb1);
                FrameLayout fl1 = (FrameLayout) headerView.findViewById(R.id.fl1);
                tv_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEidt = !isEidt;
                        secondAdapter.setNotifyEdit(isEidt);
                    }
                });
                fl1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Bean> allData = secondAdapter.getAllData();
                        if(!isAllSelect){
                            for(int a=0 ; a< allData.size() ; a++){
                                allData.get(a).setState(true);
                            }
                            isAllSelect = true;
                            checkBox.setChecked(true);
                        }else {
                            for(int a=0 ; a<allData.size() ; a++){
                                allData.get(a).setState(false);
                            }
                            isAllSelect = false;
                            checkBox.setChecked(false);
                        }
                        secondAdapter.setNotifyAll();
                    }
                });
            }
        });
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View inflate = LayoutInflater.from(FiveActivity.this).inflate(R.layout.recycler_viewbasedapter, null);
                return inflate;
            }

            @Override
            public void onBindView(View headerView) {
                RecyclerView recyclerView = (RecyclerView) headerView.findViewById(R.id.recyclerView);
                LinearLayoutManager manager = new LinearLayoutManager(FiveActivity.this);
                recyclerView.setLayoutManager(manager);
                secondAdapter = new FiveSecondAdapter(FiveActivity.this);
                recyclerView.setAdapter(secondAdapter);
                ArrayList<Bean> beans = new ArrayList<>();
                for (int a=0 ; a<7 ; a++){
                    Bean bean = new Bean();
                    bean.setName("name"+a);
                    bean.setState(false);
                    beans.add(bean);
                }
                secondAdapter.addAll(beans);
            }
        });
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View inflate = LayoutInflater.from(FiveActivity.this).inflate(R.layout.header_five2_viewnbaseadapter, null);
                return inflate;
            }

            @Override
            public void onBindView(View headerView) {
                TextView tv_2 = (TextView) headerView.findViewById(R.id.tv_2);
                final CheckBox checkBox = (CheckBox) headerView.findViewById(R.id.cb2);
                FrameLayout fl2 = (FrameLayout) headerView.findViewById(R.id.fl2);
                tv_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEidtSecond = !isEidtSecond;
                        adapter.setNotifyEdit(isEidtSecond);
                    }
                });
                fl2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Bean> allData = adapter.getAllData();
                        if(!isAllSelectSecond){
                            for(int a=0 ; a<allData.size() ; a++){
                                allData.get(a).setState(true);
                            }
                            isAllSelectSecond = true;
                            checkBox.setChecked(true);
                        }else {
                            for(int a=0 ; a<allData.size() ; a++){
                                allData.get(a).setState(false);
                            }
                            isAllSelectSecond = false;
                            checkBox.setChecked(false);
                        }
                        adapter.setNotifyAll();
                    }
                });
            }
        });
    }


}
