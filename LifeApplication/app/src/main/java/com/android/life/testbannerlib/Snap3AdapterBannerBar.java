package com.android.life.testbannerlib;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.life.R;

import java.util.ArrayList;
import java.util.List;

public class Snap3AdapterBannerBar extends RecyclerView.Adapter <Snap3AdapterBannerBar.MyViewHolder>{


    private Context mContext;
    Snap3AdapterBannerBar(Context context){
        this.mContext =context;
    }

    private List<Integer> urlList = new ArrayList<>();
    public void setData(List<Integer> list) {
        urlList.clear();
        this.urlList = list;
    }

    public List<Integer> getData() {
        return urlList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_snap2_banner, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        Integer url = urlList.get(position%urlList.size());
//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), url);
//        Bitmap bitmap1 = ImageBitmapUtils.compressByQuality(bitmap, 60,false);
//        holder.imageView.setImageBitmap(bitmap1);

        holder.imageView.setBackgroundResource(url);
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
        }

    }

    @Override
    public int getItemCount() {
        if (urlList.size() != 1) {
            Log.e("getItemCount","getItemCount---------");
            return Integer.MAX_VALUE; // 无限轮播
        } else {
            Log.e("getItemCount","getItemCount++++----");
            return urlList.size();
        }
    }

}
