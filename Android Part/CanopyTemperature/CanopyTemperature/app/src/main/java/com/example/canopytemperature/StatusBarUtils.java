package com.example.canopytemperature;

/**
 * 实现沉浸式布局
 * 这个类的作用比较简单，主要是为了让手机的状态栏的颜色与Toolbar的颜色保持一致
 * 具体效果你可以看app，手机状态栏是指手机顶端显示信号，wifi的这一部分
 * 这个类使用的方法都不是常用的方法，但是你只需知道这是设置手机UI界面的即可，你可以相应查一下某个方法的功能
 * 这个类的功能并非主要的，所以可以不用过多介绍
 */
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


public class StatusBarUtils {
    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    //方法的重载
    public static void setColor(@NonNull Window window, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(color);
        }

    }

    public static void setColor(Context context, @ColorInt int color) {
        if (context instanceof Activity) {
            setColor(((Activity) context).getWindow(), color);
        }
    }
}
