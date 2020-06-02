package com.android.bannerlib;


import android.content.Context;


public class LibUtils {

    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
