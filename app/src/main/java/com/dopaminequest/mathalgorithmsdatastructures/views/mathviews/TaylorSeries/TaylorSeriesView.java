package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.TaylorSeries;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import androidx.annotation.Nullable;

public class TaylorSeriesView extends View implements Runnable{

    private Thread mainThread;
    long lastFrameTime;
    int fps;
    private MainCanvas mc;
    private Boolean initialized;
    private Boolean running;
    public static Point dimensions;
    public static Point input;
    public static Boolean actionDown;
    public boolean paused;

    public TaylorSeriesView(Context context) {
        super(context);

        init(null);
    }

    public TaylorSeriesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TaylorSeriesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TaylorSeriesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void  init(@Nullable AttributeSet set)
    {
        paused = false;
        running = true;
        initialized = false;
        mainThread = new Thread(this);
        mainThread.start();
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
                    mainThread.sleep(100);
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

                    mc = new MainCanvas((Activity) getContext());
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
                    postInvalidate();
                }
                catch(Exception e)
                {

                }
            }
        }
    }

    public void controlFPS() {
        long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
        long timeToSleep = 15 - timeThisFrame;

        if (timeThisFrame > 0) {
            fps = (int) (1000 / timeThisFrame);
        }
        if (timeToSleep > 0) {

            try {
                mainThread.sleep(timeToSleep);
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

        if(!actionDown)
        {
            input.x = -1;
            input.y = -1;
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

    public void setTermPosition(double progress) {
        mc.setTermPosition(progress);
    }

    public void setPointAPosition(double progress) {
        mc.setPointAPosition(progress);
    }

    public int getNumRects() {
        return mc.getNumRects();
    }
    public double getPointAOffset() {
        return mc.getPointAOffset();
    }

    public double getCurrentTermNum() {
        return mc.getCurrentTermNum();
    }

    public void setSeekBar(SeekBar seekBar) {
    }
    public void setFunctionNum(int num)
    {
     mc.setFunctionNum(num);
    }

    public void toggleMaclaurinSeries() {
        mc.toggleMaclaurinSeries();
    }
}
