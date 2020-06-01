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
import android.view.ViewGroup;
/**
 * 持多种状态切换；支持上拉加载更多，下拉刷新；支持添加头部或底部view
 */
public interface InterItemView {

    /**
     * 创建view
     * @param parent            parent
     * @return                  view
     */
    View onCreateView(ViewGroup parent);

    /**
     * 绑定view
     * @param headerView        headerView
     */
    void onBindView(View headerView);


}