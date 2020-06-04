package com.dopaminequest.mathalgorithmsdatastructures.views.VectorProjection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class VectorProjectionView extends View implements Runnable{

    private Thread mainThread;
    private long lastFrameTime;
    private int fps;
    private MainCanvas mc;
    private Boolean initialized;
    private Boolean running;
    public static Point dimensions;
    public static Point input;
    public static Boolean actionDown;
    public static Boolean actionUp;
    public boolean paused;
    public static Point[] points;
    public static int numPoints;
    public static boolean initialTouchDown;

    public VectorProjectionView(Context context) {
        super(context);

        init(null);
    }

    public VectorProjectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VectorProjectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public VectorProjectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                    points=new Point[2];
                    points[0] = new Point( 0,0);
                    points[1] = new Point( 0,0);
                    input = new Point();
                    actionDown = false;
                    actionUp = true;
                    initialTouchDown = false;

                    mc = new MainCanvas();
                    initialized = true;




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
        actionUp  = true;
        initialTouchDown = false;
        points=new Point[2];
        points[0] = new Point( 0,0);
        points[1] = new Point( 0,0);
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
                //actionDown = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //actionDown = false;
                break;
            case MotionEvent.ACTION_DOWN:
                if(!actionDown)
                {
                    initialTouchDown = true;
                }

                actionDown = true;
                actionUp = false;

                break;
            case MotionEvent.ACTION_UP:
                actionDown = false;
                actionUp = true;
                break;

        }

        numPoints = motionEvent.getPointerCount();

        if (numPoints > 2)
        {
            numPoints = 2;

        }
//        if(numPoints > 0 )
//        {
//            actionDown = true;
//        }
//        else
//        {
//            actionDown = false;
//        }

        for (int n = 0; n < numPoints; n ++)
        {
            input.x  = (int) motionEvent.getX(0);
            input.y  = (int) motionEvent.getY(0);

            points[n].x = (int) motionEvent.getX(n);
            points[n].y = (int) motionEvent.getY(n);

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
}
