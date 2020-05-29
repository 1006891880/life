/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.android.refreshviewlibrary.inter;

import android.view.View;


/**
 * item中孩子点击监听接口
 */
public interface OnItemChildClickListener {

    /**
     * item中孩子点击监听接口
     * @param view                  view
     * @param position              position索引
     */
    void onItemChildClick(View view, int position);
}
