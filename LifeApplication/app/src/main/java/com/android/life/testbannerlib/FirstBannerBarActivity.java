package com.android.life.testbannerlib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.bannerlib.banner.adapter.AbsLoopPagerAdapter;
import com.android.bannerlib.banner.view.BannerView;
import com.android.life.R;




public class FirstBannerBarActivity extends AppCompatActivity {


    private int[] imgs = {
            R.drawable.bg_kites_min,
            R.drawable.bg_autumn_tree_min,
            R.drawable.bg_lake_min,
            R.drawable.bg_leaves_min,
            R.drawable.bg_magnolia_trees_min,
    };
    private BannerView banner;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(banner!=null){
            //停止轮播
            banner.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(banner!=null){
            //开始轮播
            banner.resume();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_first);

        initBanner();
    }

    private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        //设置轮播时间
        banner.setPlayDelay(2000);
        //设置轮播图适配器，必须
        banner.setAdapter(new ImageNormalAdapter(banner));
        //设置位置
        banner.setHintGravity(1);
        banner.setHintPadding(20,0, 20,20);
        //判断轮播是否进行
        boolean playing = banner.isPlaying();
        //轮播图点击事件
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(FirstBannerBarActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
            }
        });
        //轮播图滑动事件
        banner.setOnPageListener(new BannerView.OnPageListener() {
            @Override
            public void onPageChange(int position) {

            }
        });
    }


    private class ImageNormalAdapter extends AbsLoopPagerAdapter {

        public ImageNormalAdapter(BannerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs[position]);
            Bitmap bitmap1 = ImageBitmapUtils.compressByQuality(bitmap, 50, false);
            view.setImageBitmap(bitmap1);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }

}
