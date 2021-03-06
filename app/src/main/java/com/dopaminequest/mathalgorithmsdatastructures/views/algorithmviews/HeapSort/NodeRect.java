package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.HeapSort;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;

public class NodeRect extends Object{

    private boolean animating;
    private float animatingStep;
    private float speed;
    private Rect mRectSquare;
    private Rect textSquare;
    private Paint mPaintSquare;
    private Paint minRectOutlinePaint;
    private Paint mPaintText;
    private Paint mPaintLeftRightText;
    private Paint mPaintLeftRightTextRect;
    private int padding;
    private Point[] linePoints;
    private float currentAnimationIndex;
    private int P_SIZE = HeapSortView.dimensions.x/110;
    private Point centerPosition;
    public boolean top;
    public int val;
    private Paint shaderGreenPaint;
    private RectangleCanvas mc;
    private float ANIMATION_SPEED = 14f;
    private boolean currentNodeSelected;
    public static NodeRect maxNode;
    public static NodeRect leftNode;
    public static NodeRect rightNode;
    boolean isSorted;
    private Point destinationPosition;
    private Point centerDestinationPosition;
    public boolean isComplete;
    private Paint rectOutlinePaint;
    private float animationCompleteIncrementAlpha;
    private float animationCompleteIncrement;
    private Paint animationCompleteIncrementPaint;

    NodeRect(int w, int h, int x, int y, int nNumber, RectangleCanvas rectangleCanvas)
    {
        mc = rectangleCanvas;
        init(w, h,x - w/2,y - h/2, nNumber);
    }

    @Override
    public void update() {

        animatingStep++;
        if(HeapSortView.rc.getProgressBarPercentage() == 0f)
        {
            speed = ANIMATION_SPEED/2f;
        }
        else
        {
            float minPercentage = .2f;
            float oneMinusMinPercentage = (1f-minPercentage);
            float oneMinusSeekbarPercentage = (1f-oneMinusMinPercentage*mc.getProgressBarPercentage());

            speed = minPercentage*ANIMATION_SPEED + (float) (ANIMATION_SPEED*oneMinusSeekbarPercentage);
        }

        if(!animating)
        {
            return;
        }

        if(currentAnimationIndex >= linePoints.length - 1)
        {
            animating = false;

            if(HeapSortView.rc.numberAnimating > 0)
            {
                HeapSortView.rc.numberAnimating--;
            }

            currentAnimationIndex = linePoints.length - 1;
            setCenterPosition(linePoints[(int) currentAnimationIndex].x, linePoints[(int) currentAnimationIndex].y);
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

        mRectSquare.left = position.x+padding;
        mRectSquare.right = position.x + scale.x-padding;
        mRectSquare.top = position.y;
        mRectSquare.bottom = position.y + scale.y;

        if(isComplete)
        {
            drawCompletionAnimation(canvas);

            mPaintSquare.setColor(Color.CYAN);
            canvas.drawRect(mRectSquare,shaderGreenPaint);


        }
        else if(currentNodeSelected && !isSorted)
        {
            mPaintSquare.setColor(Color.RED);
            canvas.drawRect(mRectSquare, mPaintSquare);

            if(maxNode == this)
            {
                canvas.drawRect(mRectSquare,minRectOutlinePaint);
            }
        }
        else if(maxNode == this && !isSorted)
        {
            mPaintSquare.setColor(Color.CYAN);
            canvas.drawRect(mRectSquare, mPaintSquare);

        }
        else if(animating)
        {

            mPaintSquare.setColor(Color.LTGRAY);
            mPaintSquare.setAlpha((int) (mPaintSquare.getAlpha()*.15f));
            canvas.drawRect(mRectSquare,mPaintSquare);
        }
        else if(isSorted)
        {
            mPaintSquare.setColor(Color.GRAY);
            mPaintSquare.setAlpha((int) (mPaintSquare.getAlpha()*.55f));
            canvas.drawRect(mRectSquare,mPaintSquare);
        }
        else
        {
            canvas.drawRect(mRectSquare,shaderGreenPaint);
        }

        textSquare.left = position.x + padding;
        textSquare.right = position.x + scale.x - padding;
        textSquare.top = position.y + scale.y + scale.x/2;
        textSquare.bottom = (int) (position.y + scale.y  + (2.25f*scale.x/2));

        if(leftNode == this)
        {
            canvas.drawText("L", getCenterPosition().x, getCenterPosition().y + scale.y/2 + 2*scale.x/2, mPaintLeftRightText);
            canvas.drawRect(textSquare,mPaintLeftRightTextRect);
        }
        else if(rightNode == this)
        {
            canvas.drawText("R", getCenterPosition().x, getCenterPosition().y + scale.y/2 + 2*scale.x/2, mPaintLeftRightText);
            canvas.drawRect(textSquare,mPaintLeftRightTextRect);
        }

        canvas.drawRect(mRectSquare,rectOutlinePaint);
        canvas.drawText(Integer.toString(val), getCenterPosition().x,getCenterPosition().y - scale.y/2 - scale.x/4,mPaintText);
        canvas.restore();
    }

    public void init(int w, int h, int x, int y, int nNumber) {
        mRectSquare = new Rect();
        textSquare = new Rect();
        mPaintSquare = new Paint();
        mPaintSquare.setStyle(Paint.Style.FILL);
        position = new Point();
        centerPosition = new Point();
        scale = new Point();
        position.x = x;
        position.y = y;
        scale.x = w;
        scale.y = h;
        padding = (int) (P_SIZE*.55f);
        mPaintSquare.setColor(Color.GREEN);
        animating = false;
        animatingStep = 0;
        speed = ANIMATION_SPEED;
        currentAnimationIndex = 0f;
        val = nNumber;

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(w/2);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mPaintLeftRightText = new Paint();
        mPaintLeftRightText.setColor(Color.BLACK);
        mPaintLeftRightText.setTextSize(w/2);
        mPaintLeftRightText.setFakeBoldText(true);
        mPaintLeftRightText.setTextAlign(Paint.Align.CENTER);

        mPaintLeftRightTextRect = new Paint();
        mPaintLeftRightTextRect.setColor(Color.RED);
        mPaintLeftRightTextRect.setStyle(Paint.Style.STROKE);
        top = false;

        destinationPosition = new Point();
        destinationPosition.x = x;
        destinationPosition.y = y;

        centerDestinationPosition = new Point();

        shaderGreenPaint = new Paint();
        shaderGreenPaint.setStyle(Paint.Style.FILL);
        shaderGreenPaint.setStrokeCap(Paint.Cap.ROUND);
        shaderGreenPaint.setStrokeWidth(P_SIZE*.75f);
        shaderGreenPaint.setAntiAlias(true);
        shaderGreenPaint.setShader(new LinearGradient(
                HeapSortView.dimensions.x / 2f,
                HeapSortView.dimensions.y / 2f,
                HeapSortView.dimensions.x,
                HeapSortView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,200),
                Shader.TileMode.MIRROR));

        rectOutlinePaint = new Paint();
        rectOutlinePaint.setStyle(Paint.Style.STROKE);
        rectOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
        rectOutlinePaint.setAntiAlias(true);
        rectOutlinePaint.setColor(Color.BLACK);
        rectOutlinePaint.setAlpha(155);

        minRectOutlinePaint = new Paint();
        minRectOutlinePaint.setStyle(Paint.Style.STROKE);
        minRectOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
        minRectOutlinePaint.setStrokeWidth(P_SIZE*.5f);
        minRectOutlinePaint.setAntiAlias(true);
        minRectOutlinePaint.setColor(Color.CYAN);

        animationCompleteIncrementPaint = new Paint();
        animationCompleteIncrementPaint.setStyle(Paint.Style.FILL);
        animationCompleteIncrementPaint.setStrokeCap(Paint.Cap.ROUND);
        animationCompleteIncrementPaint.setAntiAlias(true);
        animationCompleteIncrementPaint.setColor(Color.GREEN);


        currentNodeSelected = false;
        maxNode = null;
        leftNode = null;
        rightNode = null;
        isSorted = false;
        isComplete = false;
        animationCompleteIncrementAlpha = .05f;
        animationCompleteIncrement = 0f;
    }

    void followCurve(Point[] linePoints)
    {
        this.linePoints = linePoints;
        animating = true;
        currentAnimationIndex = 0f;
    }

    public void animate(float s)
    {
        animating = true;
        animatingStep = (float)scale.x/2f;
        speed = s;
    }

    public Point getCenterDestinationPosition()
    {
        centerDestinationPosition.x = destinationPosition.x + scale.x/2;
        centerDestinationPosition.y = destinationPosition.y + scale.y/2;
        return centerDestinationPosition;
    }

    public void setCenterDestinationPosition(int x, int y)
    {
        destinationPosition.x = x - scale.x/2;
        destinationPosition.y = y - scale.y/2;
    }

    public Point getCenterPosition()
    {
        centerPosition.x = position.x + scale.x/2;
        centerPosition.y = position.y + scale.y/2;
        return centerPosition;
    }

    public void setCenterPosition(int x, int y)
    {
        position.x = x - scale.x/2;
        position.y = y - scale.y/2;
    }

    public boolean isCurrentNodeSelected() {
        return currentNodeSelected;
    }

    public void setCurrentNodeSelected(boolean currentNodeSelected) {
        this.currentNodeSelected = currentNodeSelected;
    }

    public boolean isAnimating() {
        return animating;
    }

    public void drawCompletionAnimation(Canvas canvas) {
        mRectSquare.left = position.x+padding;
        mRectSquare.right = position.x + scale.x-padding;
        mRectSquare.top = (int) (position.y - animationCompleteIncrement);
        mRectSquare.bottom = (int) (position.y + scale.y + animationCompleteIncrement);


        animationCompleteIncrementPaint.setAlpha((int) (255f* animationCompleteIncrementAlpha));
        canvas.drawRect(mRectSquare,animationCompleteIncrementPaint);
        animationCompleteIncrementAlpha *=.85f;

        if(animationCompleteIncrementAlpha < 0)
        {
            animationCompleteIncrementAlpha = 0f;
        }
        animationCompleteIncrement += (scale.y/ HeapSortView.rc.minBlockHeight)*P_SIZE*5;

        mRectSquare.left = position.x+padding;
        mRectSquare.right = position.x + scale.x-padding;
        mRectSquare.top = position.y;
        mRectSquare.bottom = position.y + scale.y;
    }

}