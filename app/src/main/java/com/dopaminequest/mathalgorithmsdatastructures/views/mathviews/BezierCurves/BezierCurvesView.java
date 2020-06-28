

package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.BezierCurves;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class BezierCurvesView extends View implements Runnable{

    private Thread ourThread = null;
    private long lastFrameTime;
    private int fps;
    public static MainCanvas mc;
    private Boolean initialized = false;
    private Boolean running = true;
    public static Point dimensions;
    public static long downTime;
    public static long eventTime;
    public static int action;
    public static Point input;
    public static Boolean actionDown;
    public boolean paused = false;
    private float time = 0f;
    private int count = 0;

    public BezierCurvesView(Context context) {
        super(context);

        init(null);
    }

    public BezierCurvesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BezierCurvesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public BezierCurvesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void  init(@Nullable AttributeSet set)
    {
        ourThread = new Thread(this);
        ourThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(initialized)
        {
            try
            {
                mc.draw(canvas);
            }
            catch(Exception e)
            {

            }
        }
    }

    @Override
    public void run() {

        while(running)
        {
            if(paused)
            {
                try
                {
                    ourThread.sleep(100);
                }
                catch (InterruptedException e)
                {
                }
                continue;
            }

            if(!initialized)
            {
                if(getWidth() == 0 && getHeight() == 0)
                {
                    continue;
                }
                else
                {
                    dimensions = new Point();
                    dimensions.x = getWidth();
                    dimensions.y = getHeight();

                    mc = new MainCanvas();
                    initialized = true;
                    input = new Point();
                    actionDown = false;
                }
            }
            else
            {
                try
                {
                    mc.update();
                    controlFPS();
//                    invalidate();
                    postInvalidate();
                }
                catch(Exception e)
                {

                }
            }
        }
    }

    public void controlFPS() {
        count++;
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        time += timeThisFrame;

        long timeToSleep = 15 - timeThisFrame;
        if (timeThisFrame > 0) {
            fps = (int) (1000 / timeThisFrame);
        }
        if (timeToSleep > 0) {

            try {
                ourThread.sleep(timeToSleep);
            } catch (InterruptedException e) {
            }
        }
        lastFrameTime = System.currentTimeMillis();
    }

    public void resetX()
    {
        mc.resettingMainCanvas = true;
        input = new Point();
        actionDown = false;
        mc.init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(!initialized)
        {
            return true;
        }

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                actionDown = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                actionDown = false;
                break;
            case MotionEvent.ACTION_DOWN:
                actionDown = true;
                break;
            case MotionEvent.ACTION_UP:
                actionDown = false;
                break;
        }

        int numPoints = motionEvent.getPointerCount();

        for (int n = 0; n < numPoints; n ++)
        {
            input.x  = (int) motionEvent.getX(0);
            input.y  = (int) motionEvent.getY(0);
            break;
        }

        return true;
    }

    public MainCanvas getMainCanvas()
    {
        return mc;
    }

    public void terminateThread()
    {
        running = false;
    }

    public void resumeThread()
    {
        paused = false;
    }
    public void pauseThread()
    {
        paused = true;
    }

    public void pause() {
        mc.pause();
    }

    public void toggleAnimation() {
        mc.toggleAnimation();
    }
}
