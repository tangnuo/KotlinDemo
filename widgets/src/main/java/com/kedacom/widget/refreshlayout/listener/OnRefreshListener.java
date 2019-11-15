package com.kedacom.widget.refreshlayout.listener;

import android.support.annotation.NonNull;
import com.kedacom.widget.refreshlayout.api.RefreshLayout;

/**
 * 刷新监听器
 */

public interface OnRefreshListener {

    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
