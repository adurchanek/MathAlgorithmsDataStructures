

package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.HeapSort;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class HeapSortView extends View implements Runnable{

    private Thread mainThread;
    private long lastFrameTime;
    private int fps;
    public static RectangleCanvas rc;
    public static CircleCanvas cc;
    private Boolean initialized;
    private Boolean running;
    public static Point dimensions;
    public static Point input;
    public static Boolean actionDown;
    public boolean paused;
    public boolean modifySortSpeed;
    public boolean modifyNextSort;
    public boolean modifyAutoSort;
    public int progress;

    public static int animationIndex;


    public HeapSortView(Context context) {
        super(context);

        init(null);
    }

    public HeapSortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HeapSortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public HeapSortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void  init(@Nullable AttributeSet set)
    {
        rc = null;
        cc = null;
        paused = false;
        running = true;
        initialized = false;
        modifySortSpeed = false;
        modifyNextSort = false;
        modifyAutoSort = false;
        progress = 0;
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(initialized)
        {
                rc.draw(canvas);
                cc.draw(canvas);
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

                    rc = new RectangleCanvas((Activity) getContext());
                    cc = new CircleCanvas((Activity) getContext());
                    initialized = true;
                    input = new Point();
                    actionDown = false;
                }
            }
            else
            {

                    rc.update();
                    cc.update();
                    controlFPS();
                if(modifySortSpeed)
                {
                    rc.setSortSpeed(progress);
                    cc.setSortSpeed(progress);
                    modifySortSpeed = false;
                }
                if(modifyAutoSort)
                {
                    rc.autoSort();
                    cc.autoSort();
                    modifyAutoSort = false;
                }
                if(modifyNextSort)
                {
                    rc.nextSort();
                    cc.nextSort();
                    modifyNextSort = false;
                }
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
        rc.resettingMainCanvas = true;
        cc.resettingMainCanvas = true;
        modifySortSpeed = false;
        modifyNextSort = false;
        modifyAutoSort = false;
        progress = 0;
        input = new Point();
        actionDown = false;
        rc.init();
        cc.init();
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

    public RectangleCanvas getRectangleCanvas()
    {
        return rc;
    }
    public CircleCanvas getCircleCanvas()
    {
        return cc;
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
    public void pause()
    {
        if(rc == null || cc == null)
        {
            return;
        }
        rc.pause();
        cc.pause();
    }
    public void toggleAnimation()
    {
        if(rc == null || cc == null)
        {
            return;
        }
        rc.toggleAnimation();
        cc.toggleAnimation();
    }

    public void nextSort() {
        modifyNextSort = true;
    }

    public void autoSort() {
        modifyAutoSort = true;
    }

    public void setSortSpeed(int p) {
        progress = p;
        modifySortSpeed = true;

    }

    public void setNumNodes(int progress) {

        if(rc == null || cc == null)
        {
            return;
        }
        rc.setNumNodes(progress);
        cc.setNumNodes(progress);
    }

    public int getNumNodes()
    {
        if(rc == null)
        {
            return 0;
        }
        return rc.getNumNodes();
    }

    public int getSortSpeed()
    {
        if(rc == null)
        {
            return -1;
        }
        return rc.getSortSpeed();
    }

    public static ArrayList<Integer> getRandomIntegerArray()
    {
        if(rc == null)
        {
            return null;
        }
        return rc.getRandomIntegerArray();
    }

}
