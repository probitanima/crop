package com.example.das.imagecrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class CustomImageView extends ImageView {
    private static final int touchRange = 30;
    private float lastX;
    private float lastY;
    private Rect maskRect;
    private Paint paint;
    private TouchStatus touchStatus;
    public CustomImageView(Context context) {super(context);}
    public CustomImageView(Context context, AttributeSet attrs) {super(context, attrs);}
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);}

    public void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.RED);
        maskRect = new Rect(0, 0, getWidth()/ 2, getHeight()/ 2);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint == null) {
            init();
        }
        canvas.drawRect(maskRect, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                figureTouchStatus(lastX, lastY);
                Log.i("touch", "down");
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchStatus == TouchStatus.Outside) {
                    break;
                }
                changeCropMask(event.getX() - lastX, event.getY() - lastY);
                lastX = event.getX();
                lastY = event.getY();
                invalidate();
                Log.i("touch", "move");
                break;
            case MotionEvent.ACTION_UP:
                Log.i("touch", "up");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("touch", "point_down");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i("touch", "point_up");
                break;
        }
        return true;
    }
    private void figureTouchStatus(float x, float y) {
        // Edge
        if(maskRect.top - touchRange < y && maskRect.top + touchRange > y && maskRect.left - touchRange < x && maskRect.left + touchRange > x) {
            touchStatus = TouchStatus.EdgeLeftTop;
            Log.i("touch", "EdgeLeftTop");
        }
        else if(maskRect.top - touchRange < y && maskRect.top + touchRange > y && maskRect.right - touchRange < x && maskRect.right + touchRange > x) {
            touchStatus = TouchStatus.EdgeRightTop;
            Log.i("touch", "EdgeRightTop");
        }
        else if(maskRect.bottom - touchRange < y && maskRect.bottom + touchRange > y && maskRect.left - touchRange < x && maskRect.left + touchRange > x) {
            touchStatus = TouchStatus.EdgeLeftBottom;
            Log.i("touch", "EdgeLeftBottom");
        }
        else if(maskRect.bottom - touchRange < y && maskRect.bottom + touchRange > y && maskRect.right - touchRange < x && maskRect.right + touchRange > x) {
            touchStatus = TouchStatus.EdgeRightBottom;
            Log.i("touch", "EdgeRightBottom");
        }

        // Line
        else if(maskRect.top- touchRange < y && maskRect.top+ touchRange > y && maskRect.left < x && maskRect.right > x) {
            touchStatus = TouchStatus.LineTop;
            Log.i("touch", "LineTop");
        }
        else if(maskRect.bottom- touchRange < y && maskRect.bottom+ touchRange > y && maskRect.left < x && maskRect.right > x) {
            touchStatus = TouchStatus.LineBottom;
            Log.i("touch", "LineBottom");
        }
        else if(maskRect.left- touchRange < x && maskRect.left+ touchRange > x && maskRect.top < y && maskRect.bottom > y) {
            touchStatus = TouchStatus.LineLeft;
            Log.i("touch", "LineLeft");
        }
        else if(maskRect.right- touchRange < x && maskRect.right+ touchRange > x && maskRect.top < y && maskRect.bottom > y) {
            touchStatus = TouchStatus.LineRight;
            Log.i("touch", "LineRight");
        }
        // Inside
        else if(maskRect.top < y && maskRect.bottom > y && maskRect.left < x && maskRect.right > x) {
            touchStatus = TouchStatus.Inside;
            Log.i("touch", "Inside");
        }
        else {
            touchStatus = TouchStatus.Outside;
            Log.i("touch", "Outside");
        }
    }

    private void changeCropMask(float deltaX, float deltaY) {
        int changedTop = maskRect.top + (int)deltaY;
        int changedBottom = maskRect.bottom+ (int)deltaY;
        int changedLeft = maskRect.left + (int)deltaX;
        int changedRight = maskRect.right + (int)deltaX;
        switch (touchStatus) {
            case Inside:
                if(getTop()<changedTop && getBottom()>changedBottom) {
                    maskRect.top = changedTop;
                    maskRect.bottom = changedBottom;
                }
                if(getLeft()<changedLeft && getRight()>changedRight) {
                    maskRect.left = changedLeft;
                    maskRect.right = changedRight;
                }
                break;

            case LineTop:
                resizeTop(changedTop);
                break;
            case LineBottom:
                resizeBottom(changedBottom);
                break;
            case LineLeft:
                resizeLeft(changedLeft);
                break;
            case LineRight:
                resizeRight(changedRight);
                break;

            case EdgeLeftTop:
                resizeLeft(changedLeft);
                resizeTop(changedTop);
                break;
            case EdgeRightTop:
                resizeRight(changedRight);
                resizeTop(changedTop);
                break;
            case EdgeLeftBottom:
                resizeLeft(changedLeft);
                resizeBottom(changedBottom);
                break;
            case EdgeRightBottom:
                resizeRight(changedRight);
                resizeBottom(changedBottom);
                break;
        }

    }
    private void resizeTop(int changedTop){
        if(getTop() < changedTop && maskRect.bottom - touchRange > changedTop)
            maskRect.top = changedTop;
    }
    private void resizeBottom(int changedBottom){
        if(getBottom() > changedBottom && maskRect.top + touchRange< changedBottom)
            maskRect.bottom = changedBottom;
    }
    private void resizeLeft(int changedLeft){
        if(getLeft() < changedLeft && maskRect.right - touchRange > changedLeft)
            maskRect.left = changedLeft;
    }
    private void resizeRight(int changedRight){
        if(getRight() > changedRight && maskRect.left + touchRange < changedRight)
            maskRect.right = changedRight;
    }

}
