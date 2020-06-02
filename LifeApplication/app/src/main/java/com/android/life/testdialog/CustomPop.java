package com.android.life.testdialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.dialoglibrary.popupWindow.BasePopDialog;
import com.android.dialoglibrary.toast.ToastUtils;
import com.android.life.R;

public class CustomPop extends BasePopDialog {

    public CustomPop(Context context) {
        super(context);
    }

    @Override
    public int getViewResId() {
        return R.layout.view_pop_custom_dialog;
    }

    @Override
    public void initData(View contentView) {
        TextView tv_pop = (TextView) contentView.findViewById(R.id.tv_pop);
        tv_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("滚犊子吧");
            }
        });
    }
}
