

package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.InsertionSort;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class InsertionSortView extends View implements Runnable{

    private Thread mainThread;
    private long lastFrameTime;
    private int fps;
    public static MainCanvas mc;
    private Boolean initialized;
    private Boolean running;
    public static Point dimensions;
    public static Point input;
    public static Boolean actionDown;
    public boolean paused;

    public InsertionSortView(Context context) {
        super(context);

        init(null);
    }

    public InsertionSortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public InsertionSortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public InsertionSortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                mc.draw(canvas);
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
                    mc.update();
                    controlFPS();
                    postInvalidate();

            }
        }
    }

    public void controlFPS()
    {
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

    public void nextSort() {
        mc.nextSort();
    }

    public void autoSort() {
        mc.autoSort();

    }

    public void setSortSpeed(int progress) {
        mc.setSortSpeed(progress);
    }

    public void setNumNodes(int progress) {
        mc.setNumNodes(progress);
    }

    public int getNumNodes() {
        return mc.getNumNodes();
    }

    public int getSortSpeed() {
        return mc.getSortSpeed();
    }

}
