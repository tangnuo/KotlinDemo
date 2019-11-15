package com.kedacom.widget.tablayout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by apple on 18/3/31.
 */

public class ScrollableViewPager extends ViewPager {
    private boolean isCanSroll = true;
    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanSroll(boolean canSroll) {
        isCanSroll = canSroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanSroll&&super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return isCanSroll&&super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isCanSroll&&super.onInterceptTouchEvent(event);
    }
}
