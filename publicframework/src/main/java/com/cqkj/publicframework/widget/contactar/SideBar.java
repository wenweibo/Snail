package com.cqkj.publicframework.widget.contactar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cqkj.publicframework.R;


/**
 * 右侧的字母索引自定义View
 */
public class SideBar extends View {

    //触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    // 26个字母
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"
    };
    //选中
    private int choose = -1;
    private Paint paint ;
    private TextView mTextDialog;

    //属性
    private float radius;//被选中 大字母背景圆角
    private int bgColor;//被选中 大字母背景颜色
    private int bgTextColor;//被选中 大字母颜色
    private float bgTextSize;//被选中 大字母大小
    private int textColor;//字母颜色
    private float textSize;//字母大小

    /**
     * 为SideBar显示字母的TextView  不设置则使用自定义
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        radius = array.getDimension(R.styleable.SideBar_mRadius, 0) == 0 ? 10 : array.getDimension(R.styleable.SideBar_mRadius, 0);
        bgColor = array.getColor(R.styleable.SideBar_mBgColor, 0) == 0 ? 0xaa000000 : array.getColor(R.styleable.SideBar_mBgColor, 0);
        bgTextColor = array.getColor(R.styleable.SideBar_mBgTextColor, 0) == 0 ? 0xffffffff : array.getColor(R.styleable.SideBar_mBgTextColor, 0);
        bgTextSize = array.getDimension(R.styleable.SideBar_mBgTextSize, 0) == 0 ? 70 : array.getDimension(R.styleable.SideBar_mBgTextSize, 0);
        textColor = array.getColor(R.styleable.SideBar_mTextColor, 0) == 0 ? 0xff858c94 : array.getColor(R.styleable.SideBar_mTextColor, 0);
        textSize = array.getDimension(R.styleable.SideBar_mTextSize, 0) == 0 ? 20 : array.getDimension(R.styleable.SideBar_mTextSize, 0);
        array.recycle();

    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    /**
     * 重写的onDraw的方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();//获取对应的高度
        int width = getWidth();//获取对应的宽度
        int singleHeight = height / (b.length); //获取每一个字母的高度
        for (int i = 0; i < b.length; i++) {
            paint.setColor(textColor);  // 所有字母的默认颜色 目前为灰色(右侧字体颜色)
            paint.setTypeface(Typeface.DEFAULT);//(右侧字体样式)
            paint.setAntiAlias(true);
            paint.setTextSize(textSize); //(右侧字体大小)
            //选中的状态
            if (i == choose) {
//                paint.setColor(Color.parseColor("#0195ff")); //选中字母的颜色
                paint.setFakeBoldText(true);//设置是否为粗体文字
                paint.setTextSize((float) (textSize * 1.5)); //(右侧字体大小)
            }
            //x坐标等于=宽度-字符串宽度的一半-固定大小（右边距）
            float xPos = (float) (width - paint.measureText(b[i]) / 2 - paint.measureText(b[0]) * 1.5);
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();//重置画笔
        }

        /**
         * 当外部没有设置textview显示 并且选中角标!=-1时
         */
        if (mTextDialog == null && choose != -1) {
            //画圆角矩形背景
            paint.setColor(bgColor);
            paint.setAntiAlias(true); //抗锯齿
            paint.setTextSize(bgTextSize); //(字体大小)
            RectF rectF = new RectF();
            rectF.top = (float) (height / 2 - 2 * paint.measureText(b[0]));
            rectF.bottom = (float) (height / 2 + 2 * paint.measureText(b[0]));
            rectF.left = (float) (width / 2 - 2 * paint.measureText(b[0]));
            rectF.right = (float) (width / 2 + 2 * paint.measureText(b[0]));
            canvas.drawRoundRect(rectF, radius, radius, paint);
            paint.reset();//重置画笔

            //画中间字体
            paint.setColor(bgTextColor);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(bgTextSize);
            float bgxPos = width / 2;
            float bgyPos = height / 2 + (-paint.getFontMetrics().bottom-paint.getFontMetrics().top)/2;

            canvas.drawText(b[choose], bgxPos, bgyPos, paint);
            paint.reset();//重置画笔
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float x = event.getX();//点击x坐标
        final float y = event.getY();//点击y坐标
        final int oldChoose = choose;

        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        final int c = (int) (y / getHeight() * b.length);//点击y坐标所占高度的比例*b数组的长度就等于点击b中的个数

        switch (action) {
            case MotionEvent.ACTION_UP:
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                //触摸在 字母大小三倍宽度左右 才算点击
                if (x < getWidth() - textSize * 2) {
                    return false;
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    //设置右边索引字体
    public void setTextList(String[] rightTextLists){
        b = rightTextLists;
        setInvalidate();
    }

    //重置状态
    public void setInvalidate(){
        choose = -1;
        invalidate();
        if (mTextDialog != null) {
            mTextDialog.setVisibility(View.INVISIBLE);
        }
    }

    //消除ViewPager 对自定义view的影响
    public void ignoredViewPager(ViewPager pager){
        if(pager != null){
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                    setInvalidate();
                }
            });
        }
    }
    /**
     * 向外松开的方法
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 选中回调接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}
