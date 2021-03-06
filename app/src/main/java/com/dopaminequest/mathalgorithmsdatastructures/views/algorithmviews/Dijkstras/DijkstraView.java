

package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.Dijkstras;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DijkstraView extends View implements Runnable{

    enum State
    {
        Start, End, Blocked, Explored, Unexplored, Path, Found;
    }

    private Thread ourThread = null;
    private long lastFrameTime;
    private int fps;
    public static Grid g;
    private int numTiles;
    private Boolean initialized = false;
    private Boolean running = true;
    public static Point dimensions;
    public static Point input;
    public static Boolean actionDown;
    public static State editState;
    public boolean paused = false;
    private float time = 0f;
    private int count = 0;

    public DijkstraView(Context context) {
        super(context);

        init(null);
    }

    public DijkstraView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DijkstraView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public DijkstraView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                g.draw(canvas);
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
                    numTiles = 24;
                    g = new Grid(numTiles);
                    initialized = true;
                    input = new Point();
                    editState = State.Blocked;
                    actionDown = false;
                }
            }
            else
            {
                try
                {
                    g.update();
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

        //time = time/count;

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
        g.resettingGrid = true;
        g.init();
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

        if(input.x < 0)
        {
            input.x = 0;
        }
        else if(input.x > (dimensions.x))
        {
            input.x = (dimensions.x/numTiles)*numTiles;
        }

        if(input.y < 0)
        {
            input.y = 0;
        }
        else if(input.y > (dimensions.y))
        {
            input.y = (dimensions.y/numTiles)*numTiles;
        }

        if(!actionDown)
        {
            input.x = -1;
            input.y = -1;
        }
        return true;
    }

    public void editStart()
    {
        editState = State.Start;
    }

    public void editEnd()
    {
        editState = State.End;
    }

    public void editExplored()
    {
        editState = State.Explored;
    }

    public void editUnexplored()
    {
        editState = State.Unexplored;
    }

    public void editBlocked()
    {
        editState = State.Blocked;
    }

    public void findShortestPath()
    {
        boolean returnVal = g.findShortestPath();
    }

    public Grid getGrid()
    {
        return g;
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
}
