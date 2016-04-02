package com.bobomee.android.colorstatus;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * statusbar tools
 * Created by bobomee on 16/2/12.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class StatusBarUtils {

    public enum TYPE {
        FILL, NOMAL
    }

    private TYPE style = TYPE.NOMAL;
    private int color;

    private ViewGroup root;

    private View contentView;
    private View drawer;
    private View content;

    private Activity mActivity;

    private int[] THEME_ATTRS = {
            android.R.attr.colorPrimaryDark,
    };

    private StatusBarUtils(Activity activity) {
        setActivity(activity);

        root = (ViewGroup) activity.findViewById(android.R.id.content);

        contentView = root.getChildAt(0);

        if (contentView instanceof DrawerLayout) {

            initDrawer((ViewGroup) contentView);

        }

        initWindow();
    }

    private void initWindow() {
        if (null == drawer) {
            contentView.setFitsSystemWindows(true);

        } else {
            // is drawerlayout
            contentView.setFitsSystemWindows(false);

        }
    }

    private void initDrawer(ViewGroup contentView) {
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = contentView.getChildAt(i);
            if (isContentView(child)) {
                content = child;
            } else {
                drawer = child;
            }
        }
    }

    private boolean isContentView(View child) {
        return ((DrawerLayout.LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    public static StatusBarUtils instance(Activity activity) {
        return new StatusBarUtils(activity);
    }

    private void setActivity(Activity activity) {
        this.mActivity = activity;

        TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);
        try {
            color = a.getColor(0, Color.TRANSPARENT);
        } finally {
            a.recycle();
        }

    }

    public void clear() {
        this.mActivity = null;
    }

    private boolean upL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean upKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean belowKitKat() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    public void init() {
        initStatusBar();
    }

    public StatusBarUtils setColor(int color) {
        this.color = color;
        return this;
    }

    public StatusBarUtils setStyle(TYPE style) {
        this.style = style;
        return this;
    }

    private void initStatusBar() {

        if (belowKitKat()) return;

        if (null == drawer) {
            if (upL()) {
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mActivity.getWindow().setStatusBarColor(color);
                return;
            }

            if (upKitKat()) {
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                View statusView = createStatusBarView(mActivity, color);
                //Prevent repeated additions
                int c = root.getChildCount() - 1;
                if (setMultipleColor(root, c)) return;
                root.addView(statusView);
            }
        } else {
            // is drawerlayout
            if (upL()) {
                if (style == TYPE.NOMAL) {
                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    mActivity.getWindow().setStatusBarColor(color);
                    return;
                } else {
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
            }

            if (upKitKat()) {
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            if (content instanceof MLinearLayout && style == TYPE.FILL) {
                ((MLinearLayout) content).getChildAt(0).setBackgroundColor(color);
                return;
            }

            View statusView = createStatusBarView(mActivity, color);
            ViewGroup container = new MLinearLayout(mActivity);
            ((LinearLayout) container).setOrientation(LinearLayout.VERTICAL);

            int index = ((ViewGroup) contentView).indexOfChild(content);
            ((ViewGroup) contentView).removeView(content);

            container.addView(content);

            ((ViewGroup) contentView).addView(container, index);

            if (style == TYPE.FILL) {
                container.setFitsSystemWindows(false);
                drawer.setFitsSystemWindows(false);

                container.addView(statusView, 0);

            } else {
                container.setFitsSystemWindows(true);
                drawer.setFitsSystemWindows(true);

                //Prevent repeated additions
                int c = root.getChildCount() - 1;
                if (setMultipleColor(root, c)) return;

                root.addView(statusView);
            }

        }
    }

    private boolean setMultipleColor(ViewGroup view, int index) {
        View temp = view.getChildAt(index);
        if (null != temp && temp.getHeight() == getStatusBarHeight(mActivity)) {
            temp.setBackgroundColor(color);
            return true;
        }
        return false;
    }

    private View createStatusBarView(Activity activity, int color) {
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    protected class MLinearLayout extends LinearLayout {
        private Rect mInsets;

        public MLinearLayout(Context context) {
            this(context, null);
        }

        public MLinearLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initLayout();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public MLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initLayout();
        }

        private void initLayout() {
            setWillNotDraw(true);
            ViewCompat.setOnApplyWindowInsetsListener(this,
                    new android.support.v4.view.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                            if (null == mInsets) {
                                mInsets = new Rect();
                            }
                            mInsets.set(insets.getSystemWindowInsetLeft(),
                                    insets.getSystemWindowInsetTop(),
                                    insets.getSystemWindowInsetRight(),
                                    insets.getSystemWindowInsetBottom());
                            setWillNotDraw(mInsets.isEmpty());
                            ViewCompat.postInvalidateOnAnimation(MLinearLayout.this);
                            return insets.consumeSystemWindowInsets();
                        }
                    });
        }
    }

}
