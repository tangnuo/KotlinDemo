package com.kedacom.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kedacom.widget.tablayout.MsgView;
import com.kedacom.widget.tablayout.OnTabSelectListener;
import com.kedacom.widget.tablayout.UnreadMsgUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 滑动TabLayout,对于ViewPager的依赖性强
 */
public class SlidingTabLayout
    extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    /**
     * title
     */
    public static final int TEXT_BOLD_NONE = 0;
    public static final int TEXT_BOLD_WHEN_SELECT = 1;
    public static final int TEXT_BOLD_BOTH = 2;
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    List<TabInfo> nTabInfoList;
    private Context mContext;
    private ViewPager mViewPager;
    private ArrayList<String> mTitles;
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    /**
     * 用于实现滚动居中
     */
    private Rect mTabRect = new Rect();
    private Drawable mIndicatorDrawable;
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private int mIndicatorStyle = STYLE_NORMAL;
    private float mTabPadding;
    private boolean mTabSpaceEqual;
    private float mTabWidth;
    /**
     * indicator
     */
    private Drawable mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private int mIndicatorGravity;
    private boolean mIndicatorWidthEqualTitle;
    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;
    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;
    private float mTextsize;
    private int mTextSelectColor;
    private int mTextUnselectColor;
    private int mTextBold;
    private boolean mTextAllCaps;

    private int mLastScrollX;
    private boolean mSnapOnTabClick;


    private boolean mIconVisible;
    //    private int mIconGravity;
    //图片的宽高
    private float mIconWidth;
    private float mIconHeight;
    private float mIconMarginTop;
    private float mTextViewMarginTop;
    private float margin;
    // show MsgTipView
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();
    private OnTabSelectListener mListener;

    public SlidingTabLayout(Context context) {
        this(context, null, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabsContainer);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingTabLayout);
        obtainAttributes(ta);

        //get layout_height
        String height = attrs
            .getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
//            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

    }

    /**
     * 从上层的FrgmentViewPagerTabLayout传输进来TypedArray，上层不用重新定义attr
     */
    public void setTypedArray(TypedArray ta) {
        mIndicatorStyle = ta
            .getInt(R.styleable.FragmentViewPagerTabLayout_tl_indicator_style, mIndicatorStyle);
        Drawable drawable = ta
            .getDrawable(R.styleable.FragmentViewPagerTabLayout_tl_indicator_color);
        if (drawable != null) {
            mIndicatorColor = drawable;
        }
//        mIndicatorColor = ta.getColor(R.styleable.FragmentViewPagerTabLayout_tl_indicator_color, mIndicatorColor);
        mIndicatorHeight = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_height,
                mIndicatorHeight);
        mIndicatorWidth = ta.getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_width,
            mIndicatorWidth);
        mIndicatorCornerRadius = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_corner_radius,
                mIndicatorCornerRadius);
        mIndicatorMarginLeft = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_margin_left,
                mIndicatorMarginLeft);
        mIndicatorMarginTop = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_margin_top,
                mIndicatorMarginTop);
        mIndicatorMarginRight = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_margin_right,
                mIndicatorMarginRight);
        mIndicatorMarginBottom = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_indicator_margin_bottom,
                mIndicatorMarginBottom);
        mIndicatorGravity = ta
            .getInt(R.styleable.FragmentViewPagerTabLayout_tl_indicator_gravity, mIndicatorGravity);
        mIndicatorWidthEqualTitle = ta
            .getBoolean(R.styleable.FragmentViewPagerTabLayout_tl_indicator_width_equal_title,
                mIndicatorWidthEqualTitle);

        mUnderlineColor = ta
            .getColor(R.styleable.FragmentViewPagerTabLayout_tl_underline_color, mUnderlineColor);
        mUnderlineHeight = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_underline_height,
                mUnderlineHeight);
        mUnderlineGravity = ta
            .getInt(R.styleable.FragmentViewPagerTabLayout_tl_underline_gravity, mUnderlineGravity);

        mDividerColor = ta
            .getColor(R.styleable.FragmentViewPagerTabLayout_tl_divider_color, mDividerColor);
        mDividerWidth = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_divider_width, mDividerWidth);
        mDividerPadding = ta.getDimension(R.styleable.FragmentViewPagerTabLayout_tl_divider_padding,
            mDividerPadding);

        mTextsize = ta.getDimension(R.styleable.FragmentViewPagerTabLayout_tl_textsize, mTextsize);
        mTextSelectColor = ta
            .getColor(R.styleable.FragmentViewPagerTabLayout_tl_textSelectColor, mTextSelectColor);
        mTextUnselectColor = ta
            .getColor(R.styleable.FragmentViewPagerTabLayout_tl_textUnselectColor,
                mTextUnselectColor);
        mTextBold = ta.getInt(R.styleable.FragmentViewPagerTabLayout_tl_textBold, mTextBold);
        mTextAllCaps = ta
            .getBoolean(R.styleable.FragmentViewPagerTabLayout_tl_textAllCaps, mTextAllCaps);

        mTabSpaceEqual = ta
            .getBoolean(R.styleable.FragmentViewPagerTabLayout_tl_tab_space_equal, mTabSpaceEqual);
        mTabWidth = ta.getDimension(R.styleable.FragmentViewPagerTabLayout_tl_tab_width, mTabWidth);
        mTabPadding = ta.getDimension(R.styleable.FragmentViewPagerTabLayout_tl_tab_padding,
            mTabSpaceEqual || mTabWidth > 0 ? 0 : mTabPadding);

        mIconVisible = ta
            .getBoolean(R.styleable.FragmentViewPagerTabLayout_tl_iconVisible, mIconVisible);
        mIconWidth = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_iconWidth, mIconWidth);
        mIconHeight = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_iconHeight, mIconHeight);
        mTextViewMarginTop = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_textMarginTop,
                mTextViewMarginTop);
        mIconMarginTop = ta
            .getDimension(R.styleable.FragmentViewPagerTabLayout_tl_iconMarginTop, mIconMarginTop);

        int backgroundColor = ta
            .getColor(R.styleable.FragmentViewPagerTabLayout_tl_tab_background_color, Color
                .parseColor("#ffffff"));

        setBackgroundColor(backgroundColor);
        ta.recycle();
    }

    private void obtainAttributes(TypedArray ta) {

        mIndicatorStyle = ta.getInt(R.styleable.SlidingTabLayout_tl_indicator_style, STYLE_NORMAL);

        mIndicatorColor = ta.getDrawable(R.styleable.FragmentViewPagerTabLayout_tl_indicator_color);
        if (mIndicatorColor == null) {
            mIndicatorColor = new GradientDrawable();
            ((GradientDrawable) mIndicatorColor).setColor(
                Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        }
//        mIndicatorColor = ta.getColor(R.styleable.SlidingTabLayout_tl_indicator_color, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_height,
            dp2px(
                mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_width,
            dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_indicator_corner_radius,
                dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_left, dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_top,
            dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_indicator_margin_bottom,
                dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = ta
            .getInt(R.styleable.SlidingTabLayout_tl_indicator_gravity, Gravity.BOTTOM);
        mIndicatorWidthEqualTitle = ta
            .getBoolean(R.styleable.SlidingTabLayout_tl_indicator_width_equal_title, false);

        mUnderlineColor = ta
            .getColor(R.styleable.SlidingTabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_underline_height, dp2px(0));
        mUnderlineGravity = ta
            .getInt(R.styleable.SlidingTabLayout_tl_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta
            .getColor(R.styleable.SlidingTabLayout_tl_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_divider_width, dp2px(0));
        mDividerPadding = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_divider_padding, dp2px(12));

        mTextsize = ta.getDimension(R.styleable.SlidingTabLayout_tl_textsize, sp2px(14));
        mTextSelectColor = ta
            .getColor(R.styleable.SlidingTabLayout_tl_textSelectColor, Color.parseColor("#ffffff"));
        mTextUnselectColor = ta.getColor(R.styleable.SlidingTabLayout_tl_textUnselectColor,
            Color.parseColor("#AAffffff"));
        mTextBold = ta.getInt(R.styleable.SlidingTabLayout_tl_textBold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.SlidingTabLayout_tl_textAllCaps, false);

        mTabSpaceEqual = ta.getBoolean(R.styleable.SlidingTabLayout_tl_tab_space_equal, false);
        mTabWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_width, dp2px(-1));
        mTabPadding = ta.getDimension(R.styleable.SlidingTabLayout_tl_tab_padding,
            mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(20));

        mIconVisible = ta.getBoolean(R.styleable.SlidingTabLayout_tl_iconVisible, false);
        mIconWidth = ta.getDimension(R.styleable.SlidingTabLayout_tl_iconWidth, -1);
        mIconHeight = ta.getDimension(R.styleable.SlidingTabLayout_tl_iconHeight, -1);
        mTextViewMarginTop = ta
            .getDimension(R.styleable.SlidingTabLayout_tl_textMarginTop, dp2px(3));
        mIconMarginTop = ta.getDimension(R.styleable.SlidingTabLayout_tl_iconMarginTop, 0);

        ta.recycle();
    }

    /**
     * 关联ViewPager
     */
    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPager(ViewPager vp, String[] titles) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        if (titles.length != vp.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }

        this.mViewPager = vp;
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles, titles);

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void setTabInfoList(List<TabInfo> tabInfoList) {
        nTabInfoList = tabInfoList;
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    public void setViewPager(
        ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        this.mViewPager = vp;
        this.mViewPager
            .setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.tab_item, null);
            CharSequence pageTitle =
                mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, pageTitle.toString(), tabView);
        }

        updateTabStyles();
    }

    public void addNewTab(String title) {
        View tabView = View.inflate(mContext, R.layout.tab_item, null);
        if (mTitles != null) {
            mTitles.add(title);
        }

        CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(mTabCount)
            : mTitles.get(mTabCount);
        addTab(mTabCount, pageTitle.toString(), tabView);
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        tabView.setClickable(true);
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null) {
            if (title != null) {
                tv_tab_title.setText(title);
            }
            int[][] states = new int[3][];
            states[0] = new int[]{android.R.attr.state_pressed};
            states[1] = new int[]{android.R.attr.state_selected};
            states[2] = new int[]{};
            int[] colors = new int[]{mTextSelectColor, mTextSelectColor, mTextUnselectColor};
            ColorStateList csl = new ColorStateList(states, colors);
            tv_tab_title.setTextColor(csl);
        }

        if (mIconVisible) {
            ImageView imageView = tabView.findViewById(R.id.iv_tab_icon);
            imageView.setImageDrawable(nTabInfoList.get(position).getImgDrawableUnSelect());
            imageView.setVisibility(VISIBLE);

            StateListDrawable imageDrawable = new StateListDrawable();
            imageDrawable.addState(new int[]{android.R.attr.state_pressed},
                nTabInfoList.get(position).getImgDrawableSelect());
            imageDrawable.addState(new int[]{android.R.attr.state_selected},
                nTabInfoList.get(position).getImgDrawableSelect());
            imageDrawable.addState(new int[]{},
                nTabInfoList.get(position).getImgDrawableUnSelect());
            imageView.setImageDrawable(imageDrawable);

        }
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);
                if (position != -1) {
                    if (mViewPager.getCurrentItem() != position) {
                        if (mSnapOnTabClick) {
                            mViewPager.setCurrentItem(position, false);
                        } else {
                            mViewPager.setCurrentItem(position);
                        }

                        if (mListener != null) {
                            mListener.onTabSelect(position);
                        }
                    } else {
                        if (mListener != null) {
                            mListener.onTabReselect(position);
                        }
                    }
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = mTabSpaceEqual ?
            new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
            new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            lp_tab = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }

        mTabsContainer.addView(tabView, position, lp_tab);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
//            v.setPadding((int) mTabPadding, v.getPaddingTop(), (int) mTabPadding, v.getPaddingBottom());
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            tabView.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);

            if (tv_tab_title != null) {
                if (i == mCurrentTab) {
                    tv_tab_title.setSelected(true);
                } else {
                    tv_tab_title.setSelected(false);
                }
//                tv_tab_title.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                tv_tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextsize);
//                tv_tab_title.setPadding((int) mTabPadding, 0, (int) mTabPadding, 0);
                if (mTextAllCaps) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }

                if (mTextBold == TEXT_BOLD_BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                } else if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    if (i == mCurrentTab) {
                        tv_tab_title.getPaint().setFakeBoldText(true);
                    } else {
                        tv_tab_title.getPaint().setFakeBoldText(false);
                    }
                }
                if (mIconVisible) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_tab_title
                        .getLayoutParams();
                    lp.topMargin = (int) mTextViewMarginTop;
                    tv_tab_title.setLayoutParams(lp);
                }


            }
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            if (mIconVisible) {
                if (i == mCurrentTab) {
                    iv_tab_icon.setSelected(true);
                } else {
                    iv_tab_icon.setSelected(false);
                }

//                iv_tab_icon.setImageDrawable(i == mCurrentTab ? nTabInfoList.get(i).getImgDrawableSelect() : nTabInfoList.get(i).getImgDrawableUnSelect());
                iv_tab_icon.setVisibility(VISIBLE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
                    mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
                lp.topMargin = (int) mIconMarginTop;
                iv_tab_icon.setLayoutParams(lp);
            } else {
                iv_tab_icon.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab)
            .getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);

            if (tab_title != null) {
                tab_title.setSelected(isSelect);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    tab_title.getPaint().setFakeBoldText(isSelect);
                }
            }
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            if (mIconVisible) {
                iv_tab_icon.setSelected(isSelect);
//                iv_tab_icon.setVisibility(VISIBLE);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        mIconWidth <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconWidth,
//                        mIconHeight <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) mIconHeight);
//                lp.topMargin = (int) mIconMarginTop;
//                iv_tab_icon.setLayoutParams(lp);
            } else {
                iv_tab_icon.setVisibility(View.GONE);
            }
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            TextView tab_title = currentTabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextsize);
//            float textWidth = mTextPaint.measureText(tab_title.getTitle().toString());
//            margin = (right - left - textWidth) / 2;
            margin = 0;
        }

        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
//                TextView next_tab_title = (TextView) nextTabView.findViewById(R.id.tv_tab_title);
//                mTextPaint.setTitleSize(mTextsize);
//                float nextTextWidth = mTextPaint.measureText(next_tab_title.getTitle().toString());
//                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
//                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
                margin = 0;
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (int) (left + margin - 1);
            mIndicatorRect.right = (int) (right - margin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        if (mIndicatorWidth < 0) {   //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            if (!mIndicatorWidthEqualTitle) {
                float indicatorLeft =
                    currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

                if (this.mCurrentTab < mTabCount - 1) {
                    View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                    indicatorLeft =
                        indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2
                            + nextTab.getWidth() / 2);
                }

                mIndicatorRect.left = (int) indicatorLeft;
                mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
            }

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding,
                    paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight,
                    mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft,
                    mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line

        calcIndicatorRect();
        if (mIndicatorStyle == STYLE_TRIANGLE) {
            if (mIndicatorHeight > 0) {
                if (mIndicatorColor instanceof ColorDrawable) {
                    mTrianglePaint.setColor(((ColorDrawable) mIndicatorColor).getColor());
                } else if (mIndicatorColor instanceof GradientDrawable) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mTrianglePaint
                            .setColor(((GradientDrawable) mIndicatorColor).getColors()[0]);
                    } else {
                        mTrianglePaint.setColor(Color.parseColor("#4B6A87"));
                    }

                }

                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath
                    .lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2,
                        height - mIndicatorHeight);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (mIndicatorStyle == STYLE_BLOCK) {
            if (mIndicatorHeight < 0) {
                mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
            }
            if (mIndicatorHeight > 0) {
                if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                    mIndicatorCornerRadius = mIndicatorHeight / 2;
                }
                getIndicatorDrawable();

                mIndicatorDrawable
                    .setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                        (int) mIndicatorMarginTop,
                        (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight),
                        (int) (mIndicatorMarginTop + mIndicatorHeight));

                mIndicatorDrawable.draw(canvas);
            }
        } else {
               /* mRectPaint.setColor(mIndicatorColor);
                calcIndicatorRect();
                canvas.drawRect(getPaddingLeft() + mIndicatorRect.left, getHeight() - mIndicatorHeight,
                        mIndicatorRect.right + getPaddingLeft(), getHeight(), mRectPaint);*/

            if (mIndicatorHeight > 0) {
                getIndicatorDrawable();

                if (mIndicatorGravity == Gravity.BOTTOM) {
                    mIndicatorDrawable
                        .setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            height - (int) mIndicatorMarginBottom);
                } else {
                    mIndicatorDrawable
                        .setBounds(paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left,
                            (int) mIndicatorMarginTop,
                            paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight,
                            (int) mIndicatorHeight + (int) mIndicatorMarginTop);
                }

                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    private void getIndicatorDrawable() {
        if (mIndicatorColor instanceof ColorDrawable) {
            mIndicatorDrawable = new GradientDrawable();
            ((GradientDrawable) mIndicatorDrawable)
                .setColor(((ColorDrawable) mIndicatorColor).getColor());
            ((GradientDrawable) mIndicatorDrawable).setCornerRadius(mIndicatorCornerRadius);
        } else {
            mIndicatorDrawable = mIndicatorColor;
        }
    }

    public void setIconVisible(boolean iconVisible) {
        this.mIconVisible = iconVisible;
        updateTabStyles();
    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab, smoothScroll);
    }

    public void setTabPaddingDp(float tabPadding) {
        this.mTabPadding = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabWidthDp(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int color) {
        this.mIndicatorColor = new ColorDrawable(color);
        invalidate();
    }

    public void setIndicatorHeightDp(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidthDp(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadiusDp(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMarginDp(float indicatorMarginLeft, float indicatorMarginTop,
        float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
    }

    public void setUnderlineHeightDp(float underlineHeight) {
        this.mUnderlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerPaddingDp(float dividerPadding) {
        this.mDividerPadding = dp2px(dividerPadding);
        invalidate();
    }

    public void setTextViewMarginTopDp(int textViewMarginTop) {
        mTextViewMarginTop = dp2px(textViewMarginTop);
//        updateTabStyles();
    }

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab);

    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public float getTabPadding() {
        return mTabPadding;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public Drawable getIndicatorColorDrawable() {
        return mIndicatorColor;
    }

    public void setIndicatorColorDrawable(Drawable drawable) {
        this.mIndicatorColor = drawable;
        invalidate();
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

    public float getTextsize() {
        return mTextsize;
    }

    public void setTextsize(float textsize) {
        this.mTextsize = sp2px(textsize);
        updateTabStyles();
    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public void setTextSelectColor(@ColorInt int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
//        updateTabStyles();
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public void setTextUnselectColor(@ColorInt int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
//        updateTabStyles();
    }

    public @TextBold
    int getTextBold() {
        return mTextBold;
    }

    public void setTextBold(@TextBold int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    //setter and getter

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, 0, 4);
            mInitSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showMsg(position, 0);
    }

    /**
     * 隐藏未读消息
     */
    public void hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置未读消息偏移,原点为文字或者图片的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    public void setMsgMargin(int position, float leftPaddingDp, float topPaddingDp) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);

        if (tipView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();
            lp.leftMargin = dp2px(leftPaddingDp);
//            lp.leftMargin =    >= 0 ? (int) (mTabWidth / 2 + textWidth / 2 + dp2px(leftPadding)) : (int) (mTabPadding + textWidth + dp2px(leftPadding));
//            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight - margin - iconH) / 2 - dp2px(topPadding) : 0;

            lp.topMargin = dp2px(topPaddingDp);
//            lp.topMargin =dp2px(topPadding);
            tipView.setLayoutParams(lp);
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public MsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    @IntDef({TEXT_BOLD_WHEN_SELECT, TEXT_BOLD_BOTH, TEXT_BOLD_NONE})
    @Retention(RetentionPolicy.SOURCE)
    @interface TextBold {

    }

    public static class TabInfo {

        String title;
        Class<? extends Fragment> fragmentClass;
        Drawable imgDrawableSelect;
        Drawable imgDrawableUnSelect;
        Fragment fragment;
        Bundle args;

        public TabInfo(String title, Class<? extends Fragment> fragmentClass) {
            this.title = title;
            this.fragmentClass = fragmentClass;
        }

        public Drawable getImgDrawableSelect() {
            return imgDrawableSelect;
        }

        public void setImgDrawableSelect(Drawable imgDrawableSelect) {
            this.imgDrawableSelect = imgDrawableSelect;
        }

        public Drawable getImgDrawableUnSelect() {
            return imgDrawableUnSelect;
        }

        public void setImgDrawableUnSelect(Drawable imgDrawableUnSelect) {
            this.imgDrawableUnSelect = imgDrawableUnSelect;
        }

        public Bundle getArgs() {
            return args;
        }

        public void setArgs(Bundle args) {
            this.args = args;
        }
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
            String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
