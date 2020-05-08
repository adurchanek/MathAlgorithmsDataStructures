package com.dopaminequest.mathalgorithmsdatastructures.views.Stack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import java.util.ArrayList;


public class MainCanvas extends Object {

    private Paint generalPaint;
    private Paint pathPaint;
    public boolean resettingMainCanvas = true;
    public Point canvasDimensions;
    private final int yDimension = StackView.dimensions.y;
    private final int xDimension = StackView.dimensions.x;
    private boolean pause;
    private boolean animate;

    public MainCanvas()
    {
        init();
    }

    @Override
    public void update() {

        if(resettingMainCanvas)
        {
            return;
        }

    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }


        generalPaint.setColor((Color.RED));
        pathPaint.setColor(Color.GREEN);

        drawBorder(canvas);
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(0,0, xDimension, 0, generalPaint);
        canvas.drawLine(0,0, 0, yDimension, generalPaint);
        canvas.drawLine(0, yDimension, xDimension, yDimension, generalPaint);
        canvas.drawLine(xDimension,0, xDimension, yDimension, generalPaint);
    }

    public void init()
    {
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        pathPaint = new Paint();
        position = new Point();
        scale = new Point();
        resettingMainCanvas = false;
        pause = false;
        animate = true;
        generalPaint.setAntiAlias(true);
    }

    public void reset() {
        init();
    }

    private float getDistance(Point p, Point input)
    {
        return (float) Math.sqrt(Math.pow(input.x - p.x, 2) + Math.pow(input.y - p.y, 2));
    }

    public void pause() {

        pause = !pause;
    }

    public void toggleAnimation() {
        animate = !animate;
    }
}