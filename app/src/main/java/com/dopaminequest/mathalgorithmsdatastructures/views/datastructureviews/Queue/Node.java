package com.dopaminequest.mathalgorithmsdatastructures.views.datastructureviews.Queue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;

import com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.InsertionSort.InsertionSortView;

import java.util.Random;

public class Node extends Object{

    public boolean animating;
    private float animatingStep;
    private float speed;
    private Rect mRectSquare;
    private Paint mPaintSquare;
    private Paint mPaintText;
    private int padding;
    private Point[] linePoints;
    private float currentAnimationIndex;
    private int P_SIZE = QueueView.dimensions.x/110;
    private Point centerPosition;
    public boolean destroy;
    private boolean offScreen;
    public boolean front;
    private int randomInt;
    public boolean rotating;
    private Paint shaderGreenPaint;

    Node(int w, int h, int x, int y)
    {
        init(w,h,x - w/2,y - h/2);
    }

    public boolean checkStatus() {
        return offScreen;
    }

    @Override
    public void update() {
        animatingStep++;

        if(currentAnimationIndex > linePoints.length - 1)
        {
            animating = false;
            if(destroy)
            {
                offScreen = true;
            }
            currentAnimationIndex = linePoints.length - 1;
        }
        else
        {
            setCenterPosition(linePoints[(int) currentAnimationIndex].x, linePoints[(int) currentAnimationIndex].y);
            currentAnimationIndex += (linePoints.length/200f)*speed;
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.save();
        if(animating)
        {
            if(destroy)
            {
                canvas.rotate( (currentAnimationIndex/linePoints.length)*90, getCenterPosition().x, getCenterPosition().y );
                mPaintSquare.setAlpha(100);
            }
            else
            {
                canvas.rotate(270 + (currentAnimationIndex/linePoints.length)*90, getCenterPosition().x, getCenterPosition().y );
            }
        }
        else
        {
            canvas.rotate(360, getCenterPosition().x, getCenterPosition().y );
        }

        if(front)
        {
            if(destroy)
            {
                mPaintSquare.setColor(Color.LTGRAY);
                mPaintSquare.setAlpha((int) (255*Math.pow(1-currentAnimationIndex/linePoints.length,3)));
            }
            else
            {
                mPaintSquare.setColor(Color.GREEN + 250);
            }
        }
        else
        {
            //mPaintSquare = shaderGreenPaint;
            mPaintSquare.setColor(Color.GREEN);
        }

        mRectSquare.left = position.x+padding;
        mRectSquare.right = position.x + scale.x-padding;
        mRectSquare.top = position.y+padding;
        mRectSquare.bottom = position.y + scale.y-padding;
        canvas.drawRect(mRectSquare,mPaintSquare);
        canvas.drawText(Integer.toString(randomInt), getCenterPosition().x,getCenterPosition().y,mPaintText);
        canvas.restore();
    }

    public void init(int w, int h, int x, int y) {
        mRectSquare = new Rect();
        mPaintSquare = new Paint();
        mPaintSquare.setStyle(Paint.Style.FILL);
        position = new Point();
        centerPosition = new Point();
        scale = new Point();
        position.x = x;
        position.y = y;
        scale.x = w;
        scale.y = h;
        padding = P_SIZE;
        mPaintSquare.setColor(Color.GREEN);
        animating = false;
        animatingStep = 0;
        speed = 7f;
        currentAnimationIndex = 0f;
        destroy = false;
        offScreen = false;
        Random rand = new Random();
        randomInt = rand.nextInt(1000);
        mPaintText = new Paint();
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(P_SIZE*8);
        front = false;
        mPaintText.setTextAlign(Paint.Align.CENTER);


        shaderGreenPaint = new Paint();
        shaderGreenPaint.setStyle(Paint.Style.FILL);
        shaderGreenPaint.setStrokeCap(Paint.Cap.ROUND);
        shaderGreenPaint.setStrokeWidth(P_SIZE*.75f);
        shaderGreenPaint.setAntiAlias(true);
        shaderGreenPaint.setShader(new LinearGradient(
                QueueView.dimensions.x / 2f,
                QueueView.dimensions.y / 2f,
                QueueView.dimensions.x,
                QueueView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

    }

    void followCurve(Point[] linePoints, boolean animate)
    {
        this.linePoints = linePoints;
        animating = animate;
        currentAnimationIndex = 0f;
    }

    public void animate(float s)
    {
        animating = true;
        animatingStep = (float)scale.x/2f;
        speed = s;
    }

    public Point getCenterPosition()
    {
        centerPosition.x = position.x + scale.x/2;
        centerPosition.y = position.y + scale.y/2;
        return centerPosition;
    }

    public Point setCenterPosition(int x, int y)
    {
        position.x = x - scale.x/2;
        position.y = y - scale.y/2;
        return centerPosition;
    }
}