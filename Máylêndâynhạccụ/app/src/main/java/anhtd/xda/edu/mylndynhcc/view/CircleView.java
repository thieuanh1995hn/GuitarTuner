package anhtd.xda.edu.mylndynhcc.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import anhtd.xda.edu.mylndynhcc.R;


public class CircleView extends View {
    private Paint paint;
    private Paint textPaint;
    private Rect textBounds;

    private int color;
    private int textColor;

    private float textSize;
    private int diameter;
    private float circleCenterX;
    private float circleCenterY;
    private String text;



    public CircleView(Context context){
        super(context);
        init(null);
    }

    public CircleView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(attrs);
    }



    private void init(AttributeSet attrs){

        text = "A3";
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //set defaults
        paint.setStyle(Paint.Style.FILL);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if(attrs != null){
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TunerView, 0, 0);
            try{
                color = a.getColor(R.styleable.TunerView_innerCircleColor, CircleTunerView.DEFAULT_CIRCLE_COLOR);
                paint.setColor(color);
                textColor = CircleTunerView.DEFAULT_TEXT_COLOR;
                textPaint.setColor(textColor);

            }catch(Exception e){
                e.printStackTrace();
            }finally{
                a.recycle();
            }
        }else{
            color = CircleTunerView.DEFAULT_CIRCLE_COLOR;
            textColor = CircleTunerView.DEFAULT_TEXT_COLOR;
            paint.setColor(color);
            textPaint.setColor(textColor);

        }

    }


    private void setElevation(){
        setElevation(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        switch(wMode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                width = (width > height) ? height : width;
                break;
        }
        switch(hMode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = (height > width) ? width : height;
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        float xPad = (float) (getPaddingLeft() + getPaddingRight());
        float yPad = (float) (getPaddingTop() + getPaddingBottom());
        //width minus the padding
        float xWidth = width - xPad;
        //height minus the padding
        float yHeight = height - yPad;
        int outerCircleDiameter = (int) Math.min(xWidth, yHeight);
        diameter = outerCircleDiameter / 2;

        circleCenterX = (int) xWidth / 2;
        circleCenterY = (int) yHeight / 2;

        textSize = (float)(diameter / 2.3);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //draw the inner circle
        canvas.drawCircle(circleCenterX, circleCenterY, diameter / 2, paint);
        textBounds = new Rect();

        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, circleCenterX, circleCenterY + (textBounds.height() / 2), textPaint);
    }


    public String getText(){
        return text;
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public int getColor(){
        return color;
    }

    public void setColor(@ColorInt int color){
        this.color = color;
        this.paint.setColor(color);
        invalidate();
    }
    public void setTextColor(@ColorInt int color){
        this.textColor = color ;
        this.textPaint.setColor(color);
        invalidate();
    }




}
