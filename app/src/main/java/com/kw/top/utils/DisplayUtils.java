package com.kw.top.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.kw.top.app.AppManager;
import com.kw.top.app.BaseApplication;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class DisplayUtils {
    private static final String TAG = "DisplayUtils";

    /*
     * 屏幕尺寸相关操作
     *
     * 在第一个Activity初始化的时候必须设置高宽值，后面才能调用到
     */
    private static int screenHeight = 0;
    private static int screenWidth = 0;
    private static double screenInches = 0;
    private static String deviceId = null;


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }



    /**
     * 获得屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * @param activity
     * @return > 0 success; <= 0 fail
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }

        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;

    }

    public static int getScreenHeight(Activity activity) {
        try {
            if (0 == screenHeight) {
                setScreenSize(activity);
            }
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    public static int getScreenHeight() {
        try {
            if (0 == screenHeight) {
                setScreenSize(AppManager.getAppManager().getCurrentActivity());
            }
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    public static int getScreenWidth(Activity activity) {
        try {
            if (0 == screenWidth) {
                setScreenSize(activity);
            }
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    public static int getScreenWidthDp(Activity activity) {
        try {
            if (0 == screenWidth) {
                setScreenSize(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return px2dip(activity, screenWidth);
    }

//	public static double getScreenInches() {
//		if (screenInches <= 0) {
//			DisplayMetrics dm = new DisplayMetrics();
//			ActivityManager.getInstance().currentActivity().getWindowManager()
//					.getDefaultDisplay().getMetrics(dm);
//			double x = getScreenWidth() * getScreenWidth();
//			double y = getScreenHeight() * getScreenHeight();
//			screenInches = Math.sqrt(x + y) / (dm.density * 160.0);
//			Log.d("SCREEN_INCHES", screenInches + "");
//		}
//		return screenInches;
//	}

    public static void setScreenSize(Activity activity) {
        if (activity == null) {
            return;
        }

        try {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        } catch (ExceptionInInitializerError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化设备ID
     */
    public static void initDeviceId() {
        if (null == deviceId) {

            Context context = BaseApplication.getInstance();
            final TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            UUID deviceUuid;
            try {
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = ""
                        + android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            } catch (Exception e) {
                deviceUuid = UUID.randomUUID();
            }
            deviceId = deviceUuid.toString().replaceAll(" ", "");

        }
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId() {
        if (null == deviceId) {
            initDeviceId();
        }
        return deviceId;
    }


    /**
     * 获取前16位的设备ID
     *
     * @return
     */
    public static String getDeviceId16() {
        String devideID = getDeviceId();
        if (devideID.length() > 16) {
            return devideID.substring(0, 16);
        } else if (devideID.length() < 16) {
            Random random = new Random();
            StringBuilder builder = new StringBuilder(devideID);

            for (int i = 0; i < 16 - devideID.length(); i++) {
                builder.append(random.nextInt(10));
            }
            devideID = builder.toString();
        }
        return devideID;

    }


    /**
     * 获取虚拟按键栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 判断是否meizu手机
     *
     * @return
     */
    public static boolean isMeizu() {
        return Build.BRAND.equals("Meizu");
    }

    /**
     * 获取魅族手机底部虚拟键盘高度
     *
     * @param context
     * @return
     */
    public static int getSmartBarHeight(Context context) {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("mz_action_button_min_height");
            int height = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ViewGroup.LayoutParams covertLp(ViewGroup.LayoutParams lp, int width, int height) {
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        return lp;
    }

    public static int getScreenOrientation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService("window");
        Display display = wm.getDefaultDisplay();
        boolean orientation = false;
        byte orientation1;
        if (display.getWidth() == display.getHeight()) {
            orientation1 = 3;
        } else if (display.getWidth() < display.getHeight()) {
            orientation1 = 1;
        } else {
            orientation1 = 2;
        }

        return orientation1;
    }


}