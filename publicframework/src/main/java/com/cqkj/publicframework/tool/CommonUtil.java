package com.cqkj.publicframework.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqkj.publicframework.R;
import com.cqkj.publicframework.activity.BaseTitleActivity;
import com.cqkj.publicframework.adapter.SpinerAdapter;
import com.cqkj.publicframework.tool.RecordVoice.VoiceUtils;
import com.cqkj.publicframework.widget.SpinerPopWindow;
import com.cqkj.publicframework.widget.data_pick.CustomDatePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CommonUtil {

    /**
     * 设置列表是否显示
     */
    public static <T> void setMainView(List<T> list, LinearLayout lin_no_data, View mainView){
        //如果有列表数据，则无数据显示
        if (list.isEmpty()){
            lin_no_data.setVisibility(View.VISIBLE);
            mainView.setVisibility(View.GONE);
        }else{
            //否则显示主内容视图
            lin_no_data.setVisibility(View.GONE);
            mainView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置页面模块显示
     *
     * @param item 0--显示加载，1--显示主内容，2--显示无数据
     * @param rela_load 加载视图
     * @param sv_main   主体视图
     * @param lin_no_data 无数据视图
     */
    public static void setMain(int item,View rela_load,View sv_main,View lin_no_data) {
        int load_vis = View.VISIBLE;
        int main_vis = View.GONE;
        int no_vis = View.GONE;
        switch (item) {
            case 1:
                load_vis = View.GONE;
                main_vis = View.VISIBLE;
                break;
            case 2:
                load_vis = View.GONE;
                no_vis = View.VISIBLE;
                break;
        }
        rela_load.setVisibility(load_vis);
        sv_main.setVisibility(main_vis);
        lin_no_data.setVisibility(no_vis);
    }

    /**
     * 转换null字符串
     * @param str
     * @return
     */
    public static String changeStringNotNull(String str){
        return str==null?"":str;
    }

    /**
     * 字符串过长，转化为 x...x
     * @param str
     * @return
     */
    public static String changeStringEllipsis(String str){
        str = changeStringNotNull(str);
        if (str.length()>3){
            str = str.substring(0,1)+"..."+str.substring(str.length()-1);
        }
        return str;
    }
    /**
     * 生成随机id
     *
     * @return
     */
    public static String randomId() {
        Random random = new Random();
        int num = random.nextInt(10000);
        String randomId = num + "_" + System.currentTimeMillis();
        return randomId;
    }
    /**
     * 停止播放录音
     *
     * @param voiceUtils
     */
    public static void stopVoice(VoiceUtils voiceUtils) {
        voiceUtils = VoiceUtils.getInstance(new VoiceUtils.NotifyDataInterface() {
            @Override
            public void notify(int index) {
            }
        }, 0);
        if (voiceUtils.getmPlayer() != null) {
            voiceUtils.stop();
            voiceUtils = null;
        }
    }



    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int packageCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeybroad(Context context) {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getNowDay() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static int getPlaySeconds(String str) {
        int time = 0;
        if (str.contains(":")) {
            String[] arr = str.split(":");
            if (arr.length == 2) {
                time += Integer.parseInt(arr[0]) * 60;
                time += Integer.parseInt(arr[1]);
            } else if (arr.length == 3) {
                time += Integer.parseInt(arr[0]) * 60 * 60;
                time += Integer.parseInt(arr[1]) * 60;
                time += Integer.parseInt(arr[2]);
            }

        } else {
            time += Integer.parseInt(str);
        }
        return time;
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDor = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDor = Environment.getExternalStorageDirectory();
            return sdDor.toString();
        } else {
            return "";
        }

    }



    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
//    private static long lastClickTime;

    public static boolean isFastClick(View view) {
        long lastClickTime = 0;
        if (view.getTag() != null) {
            String tag = view.getTag().toString();
            try {
                lastClickTime = Long.parseLong(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        view.setTag(curClickTime);
//        lastClickTime = curClickTime;
        return flag;
    }






    /**
     * 设置顶图
     *
     * @param context
     * @param drawable
     * @param iv
     */
    public static void zoomIMG(Context context, Drawable drawable, ImageView iv, boolean isL) {
        int wid = getScreen(context)[0];
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        float scaleWidth = ((float) wid / width);
        if (isL) {
            ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * scaleWidth));
            iv.setLayoutParams(lp);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * scaleWidth));
            iv.setLayoutParams(lp);
        }
    }

    /**
     * 将秒数转换成时分秒格式
     *
     * @param ms
     * @return
     */
    public static String getFormatHMS(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strHour + " 时 " + strMinute + " 分 " + strSecond + " 秒";
    }









    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Context context, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static int[] getScreen(Context context) {
        // 获取屏幕高宽
        WindowManager manager = ((Activity) context).getWindowManager();
        int[] screen = new int[2];
        screen[0] = manager.getDefaultDisplay().getWidth();
        screen[1] = manager.getDefaultDisplay().getHeight();
        return screen;
    }


    /**
     * 设置开关状态
     *
     * @param ctv
     * @param yesOrNo
     */
    public static void setChechTextViewState(CheckedTextView ctv, String yesOrNo) {
        if (yesOrNo != null && yesOrNo.equals("YESORNO_YES")) {
            ctv.setChecked(true);
        } else {
            ctv.setChecked(false);
        }
    }


    public interface ShowBack {
        void doSome(View v, int flag);
    }



    /**
     * 照片选择对话框
     *
     * @param parent
     * @param tv
     * @param activity
     */
    public static void showPicSpinWindow(final View parent, TextView tv, BaseTitleActivity activity) {
        hideSoftInputFromWindow(activity);
        String[] list = new String[]{"相册", "相机"};
        SpinerAdapter mAdapter = new SpinerAdapter(activity, list);
        // mAdapter.refreshData(mListType, 0);
        SpinerPopWindow mSpinerPopWindow = new SpinerPopWindow(activity, list, tv);
        mSpinerPopWindow.setAdatper(mAdapter);
        // 产生背景变暗效果
        mSpinerPopWindow.showAtLocation(parent, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
//                    mProgressDialog.setMessage("加载照片中");
//                    mProgressDialog.show();
                if (pos == 0) {
                    activity.openAlbum();
                } else {
                    activity.openCamera();
                }

            }
        });
    }


    /**
     * 公用询问提示框
     *
     * @param context
     * @param title
     * @param showBack
     */
    public static void showAsk(Context context, String title, String msg, ShowBack showBack, boolean cancle, View view, int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(com.cqkj.publicframework.R.mipmap.anim_1);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(msg);//提示内容
        builder.setPositiveButton(context.getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(view, 0);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(view, 1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancle);
        dialog.setCanceledOnTouchOutside(cancle);
        dialog.show();
    }

    /**
     * 公用询问提示框
     *
     * @param context
     * @param title
     * @param showBack
     */
    public static void showAsk(Context context, String title, String msg, ShowBack showBack, boolean cancle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(com.cqkj.publicframework.R.mipmap.anim_1);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(msg);//提示内容
        builder.setPositiveButton(context.getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(null, 0);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(null, 1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancle);
        dialog.setCanceledOnTouchOutside(cancle);
        dialog.show();
    }

    /**
     * 公用询问提示框
     *
     * @param context
     * @param title
     * @param showBack
     */
    public static void showAsk(Context context, String title, String msg, ShowBack showBack, boolean cancle, DialogInterface.OnCancelListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(com.cqkj.publicframework.R.mipmap.anim_1);
        if (!TextUtils.isEmpty(title))
            builder.setTitle(title);
        builder.setMessage(msg);//提示内容
        builder.setPositiveButton(context.getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(null, 0);
            }
        });
        if (cancle) {
            builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (showBack != null)
                        showBack.doSome(null, 1);
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancle);
        dialog.setCanceledOnTouchOutside(cancle);
        if (listener != null)
            dialog.setOnCancelListener(listener);
        dialog.show();
    }

    /**
     * 公用询问提示框
     *
     * @param context
     * @param title
     * @param showBack
     */
    public static void showAsk(Context context, String title, ShowBack showBack, boolean cancle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(com.cqkj.publicframework.R.mipmap.anim_1);
        builder.setMessage(title);//提示内容
        builder.setPositiveButton(context.getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(null, 0);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (showBack != null)
                    showBack.doSome(null, 1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancle);
        dialog.setCanceledOnTouchOutside(cancle);
        dialog.show();
    }

    /**
     * 初始化时间选择器:当月
     *
     * @param textView
     * @return
     */
    public static CustomDatePicker initDatePicker(Context context, TextView textView, ShowBack showBack) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String now = sdf.format(new Date());
        textView.setText(now);
        CustomDatePicker customDatePicker = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                textView.setText(time);
                if (showBack != null)
                    showBack.doSome(textView, 0);
            }
        }, getDay(true), getDay(false), "yyyy-MM-dd"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        return customDatePicker;
    }

    public static String getDay(boolean first) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        if (first)
            c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        else
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day = format.format(c.getTime());
        return day;
    }

    /**
     * 初始化时间选择器
     *
     * @param textView
     * @return
     */
    public static CustomDatePicker initDatePicker(Context context, TextView textView, boolean showSpecificTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        textView.setText(now);
        CustomDatePicker customDatePicker = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                textView.setText(time);
            }
        }, now, "2030-12-30 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(showSpecificTime); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        return customDatePicker;
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftInputFromWindow(Context context) {
        InputMethodManager mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = mImm.isActive();
        if (isOpen) {
            if (((Activity) context).getCurrentFocus() != null) {

                mImm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                mImm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
