package com.android.dialoglibrary.dialogMenu;

import android.content.Context;

import androidx.recyclerview.widget.OrientationHelper;

import java.util.List;

public class CustomBottomDialog {

    private CustomDialog customDialog;
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    public CustomBottomDialog(Context context) {
        customDialog = new CustomDialog(context);
    }

    public CustomBottomDialog title(String title) {
        customDialog.title(title);
        return this;
    }

    public CustomBottomDialog title(int title) {
        customDialog.title(title);
        return this;
    }

    public CustomBottomDialog setCancel(boolean isShow , String text) {
        customDialog.setCancel(isShow, text);
        return this;
    }

    public CustomBottomDialog background(int res) {
        customDialog.background(res);
        return this;
    }

    public CustomBottomDialog inflateMenu(int menu, OnItemClickListener onItemClickListener) {
        customDialog.inflateMenu(menu, onItemClickListener);
        return this;
    }

    public CustomBottomDialog layout(int layout) {
        customDialog.layout(layout);
        return this;
    }

    public CustomBottomDialog orientation(int orientation) {
        customDialog.orientation(orientation);
        return this;
    }

    public CustomBottomDialog addItems(List<CustomItem> items, OnItemClickListener onItemClickListener) {
        customDialog.addItems(items, onItemClickListener);
        return this;
    }

    public CustomBottomDialog itemClick(OnItemClickListener listener) {
        customDialog.setItemClick(listener);
        return this;
    }

    public void show() {
        customDialog.show();
    }

}
