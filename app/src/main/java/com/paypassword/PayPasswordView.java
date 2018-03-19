package com.paypassword;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/3/19.
 */

public class PayPasswordView extends EditText {

    //画笔
    Paint paint;
    //每个密码所占的宽度
    int mPasswordItemWidth;
    //密码位数
    int mPasswordNum=6;
    //背景色
    int mBgColor = Color.parseColor("#d1d2d6");
    // 背景边框大小
    private int mBgSize = 1;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 分割线的颜色
    private int mDivisionLineColor = mBgColor;
    // 分割线的大小
    private int mDivisionLineSize = 1;
    // 密码圆点的颜色
    private int mPasswordColor = mDivisionLineColor;
    // 密码圆点的半径大小
    private int mPasswordRadius = 4;

    PayEndListener payEndListener;

    public PayPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttributeSet(context, attrs);
        //设置模式为密码
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        //不显示光标
        setCursorVisible(false);
    }

    public void initAttributeSet(Context context, AttributeSet attrs){
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.PayPasswordView);
        mPasswordRadius = (int) array.getDimension(R.styleable.PayPasswordView_passwordRadius, dip2px(mPasswordRadius));
        mBgCorner= (int) array.getDimension(R.styleable.PayPasswordView_bgCorner,dip2px(mBgCorner));
        mBgSize= (int) array.getDimension(R.styleable.PayPasswordView_bgSize,dip2px(mBgSize));
        mBgColor=array.getColor(R.styleable.PayPasswordView_bgColor,mBgColor);
        mPasswordNum=array.getInt(R.styleable.PayPasswordView_passwordNumber,6);
        mDivisionLineSize= (int) array.getDimension(R.styleable.PayPasswordView_divisionLineSize,dip2px(mDivisionLineSize));
        mDivisionLineColor = array.getColor(R.styleable.PayPasswordView_divisionLineColor, mDivisionLineColor);
        mPasswordColor = array.getColor(R.styleable.PayPasswordView_passwordColor, mDivisionLineColor);
        array.recycle();
    }

    //初始化画笔
    public void initPaint(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

    }
    /**
     * dip 转 px
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int passwordWidth=getWidth()-(mPasswordNum-1)*mDivisionLineSize;
        mPasswordItemWidth=passwordWidth/mPasswordNum;
        //绘制背景；
        drawbg(canvas);
        //绘制分割线
        drawDivisionLine(canvas);
        // 绘制密码
        drawHidePassword(canvas);

    }

    /**
     * 绘制密码
     * @param canvas
     */
    private void drawHidePassword(Canvas canvas) {
        int passwordLength=getText().length();
        paint.setColor(mPasswordColor);
        //设置画笔为实心
        paint.setStyle(Paint.Style.FILL);
        for (int i=0;i<passwordLength;i++){
            int cx=i+mDivisionLineSize+i*mPasswordItemWidth+ mPasswordItemWidth / 2 + mBgSize;

            canvas.drawCircle(cx,getHeight()/2,mPasswordRadius,paint);
        }

        if (passwordLength>=mPasswordNum){
            if (payEndListener!=null){
                payEndListener.doEnd(getText().toString().trim());
            }
        }


    }

    private void drawDivisionLine(Canvas canvas) {
        paint.setColor(mDivisionLineColor);
        paint.setStrokeWidth(mDivisionLineSize);
        for (int i=0;i<mPasswordNum-1;i++){
            int startX=(i + 1) * mDivisionLineSize + (i + 1) * mPasswordItemWidth + mBgSize;
            canvas.drawLine(startX,mBgSize,startX, getHeight() - mBgSize, paint);
        }
    }

    /**
     * 绘制背景
     * @param canvas
     */
    private void drawbg(Canvas canvas) {
        paint.setColor(mBgColor);
        //设置画笔为空心
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mBgSize);
        RectF rectF=new RectF(mBgSize,mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 如果没有设置圆角，就画矩形
        if (mBgCorner == 0) {
            canvas.drawRect(rectF, paint);
        } else {
            // 如果有设置圆角就画圆矩形
            canvas.drawRoundRect(rectF, mBgCorner, mBgCorner, paint);
        }

    }

    public void addPassword(String number){
        number = getText().toString().trim() + number;
        if (number.length() > mPasswordNum) {
            return;
        }
        setText(number);
    }

    /**
     * 删除最后一位密码
     */
    public void delLastPassword(){
        String currentText=getText().toString().trim();
        if (TextUtils.isEmpty(currentText)){
            return;
        }
        currentText=currentText.substring(0,currentText.length()-1);
        setText(currentText);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setPayPasswordEndListener(PayEndListener payEndListener){
        this.payEndListener=payEndListener;
    }

    public interface  PayEndListener{
        void doEnd(String password);
    }
}
