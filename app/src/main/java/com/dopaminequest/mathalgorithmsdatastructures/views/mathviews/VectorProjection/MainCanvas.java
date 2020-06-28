package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.VectorProjection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import java.util.ArrayList;

public class MainCanvas extends Object {

    private Paint generalPaint;
    private Paint pathPaint;
    private Paint generalPaint2;
    private Paint projectedLinePaint;
    public boolean resettingMainCanvas = true;
    private Point canvasDimensions;
    private final int yDimension = VectorProjectionView.dimensions.y;
    private final int xDimension = VectorProjectionView.dimensions.x;
    private boolean pause;
    private boolean animate;
    private float length;
    private float lineAngle;
    private float distanceToLinePoint;
    private float projectedVector;
    private int projectedX;
    private int projectedY;
    private Point controlPositionStart;
    private Point controlPositionEnd;
    private int P_SIZE = VectorProjectionView.dimensions.x/110;
    private boolean controlPointPressReset;
    private int currentControlPointIndex;
    private int currentInputPointIndex;
    private ArrayList<Point> controlPoints;
    private ArrayList<Point> clampedInputPoints;
    private Point targetPoint;
    private RectF arcRect;
    private float visualMultiplier;
    private float controlPointsLineStartLength;
    private float arcAnimationIncrement;
    private int currentArcState;
    private int arcStateOne;
    private int arcStateTwo;
    private Paint arcPaint;
    private Paint gridTextPaint;
    private Paint generalPaint5;
    private int gridSubDivision;


    public MainCanvas() {
        init();
    }

    @Override
    public void update()
    {

        if (resettingMainCanvas) {
            return;
        }
        handleInput();

        length = calculateDistance(controlPoints.get(0).x, controlPoints.get(0).y, controlPoints.get(1).x, controlPoints.get(1).y);
        lineAngle = calculateAngle(controlPoints.get(0).x, controlPoints.get(0).y, controlPoints.get(1).x, controlPoints.get(1).y);

        distanceToLinePoint = calculateDistanceToLinePoint(targetPoint.x, targetPoint.y);
        projectedVector = calculateProjectedVector(targetPoint.x, targetPoint.y);

        projectedX = calculateProjectedPoints(targetPoint.x, targetPoint.y).x;
        projectedY = calculateProjectedPoints(targetPoint.x, targetPoint.y).y;
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }
        drawGrid(canvas, gridSubDivision,gridSubDivision);

        float targetControlPointsAngle = calculateAngle(controlPositionStart.x, controlPositionStart.y, targetPoint.x, targetPoint.y);
        float angleDifference = -targetControlPointsAngle + lineAngle;

        angleDifference = drawArc(canvas, angleDifference);
        drawRightAngle(canvas, angleDifference);

        pathPaint.setStrokeWidth(P_SIZE*.75f);
        canvas.drawLine(controlPositionStart.x, controlPositionStart.y, controlPositionEnd.x, controlPositionEnd.y, generalPaint);
        pathPaint.setColor(Color.CYAN);
        generalPaint2.setColor((Color.MAGENTA));
        canvas.drawLine(controlPositionStart.x, controlPositionStart.y, targetPoint.x, targetPoint.y, generalPaint2);

        drawProjectedLines(canvas);

        generalPaint2.setStyle(Paint.Style.FILL);
        generalPaint2.setStrokeWidth(P_SIZE*.75f);
        generalPaint2.setColor(Color.CYAN);
        canvas.drawCircle(targetPoint.x,targetPoint.y,P_SIZE*1.8f,generalPaint2);
        generalPaint2.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(controlPositionStart.x, controlPositionStart.y,P_SIZE*1.8f,pathPaint);

        drawBorder(canvas);
    }

    public void init()
    {
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        generalPaint2 = new Paint();
        pathPaint = new Paint();
        position = new Point();
        scale = new Point();
        resettingMainCanvas = false;
        pause = false;
        animate = true;
        length = 0f;
        lineAngle = 0f;
        distanceToLinePoint = 0;
        projectedVector = 0f;
        projectedX = 0;
        projectedY = 0;
        this.controlPositionStart = new Point();
        this.controlPositionEnd = new Point();
        setControlPositionStart(P_SIZE*20,VectorProjectionView.dimensions.y - P_SIZE*20);
        setControlPositionEnd(VectorProjectionView.dimensions.x -P_SIZE*20, VectorProjectionView.dimensions.y - P_SIZE*20);
        controlPointPressReset = true;
        currentControlPointIndex = 0;
        currentInputPointIndex = -1;
        controlPoints = new ArrayList<Point>();
        controlPoints.add(controlPositionStart);
        controlPoints.add(controlPositionEnd);
        targetPoint = new Point();
        targetPoint.x = VectorProjectionView.dimensions.x/2;
        targetPoint.y = (int) (VectorProjectionView.dimensions.y*.25f);
        arcRect = new RectF();

        VectorProjectionView.points[0].x = controlPositionStart.x;
        VectorProjectionView.points[0].y = controlPositionStart.y;
        VectorProjectionView.points[1].x = controlPositionEnd.x;
        VectorProjectionView.points[1].y = controlPositionEnd.y;

        clampedInputPoints = new ArrayList<Point>();
        clampedInputPoints.add(new Point());
        clampedInputPoints.add(new Point());
        clampedInputPoints.get(0).x = controlPositionStart.x;
        clampedInputPoints.get(0).y = controlPositionStart.y;
        clampedInputPoints.get(1).x = controlPositionEnd.x;
        clampedInputPoints.get(1).y = controlPositionEnd.y;

        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(P_SIZE*.75f);
        pathPaint.setAntiAlias(true);
        pathPaint.setShader(new LinearGradient(
                VectorProjectionView.dimensions.x / 2f,
                VectorProjectionView.dimensions.y / 2f,
                VectorProjectionView.dimensions.x,
                VectorProjectionView.dimensions.y,
                Color.rgb(20,220,70),
                Color.rgb(20,220,20),
                Shader.TileMode.MIRROR));

        generalPaint.setStyle(Paint.Style.STROKE);
        generalPaint.setStrokeCap(Paint.Cap.ROUND);
        generalPaint.setStrokeWidth(P_SIZE*.75f);
        generalPaint.setAntiAlias(true);
        generalPaint.setShader(new LinearGradient(
                VectorProjectionView.dimensions.x / 2f,
                VectorProjectionView.dimensions.y / 2f,
                VectorProjectionView.dimensions.x,
                VectorProjectionView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

        gridTextPaint = new Paint();
        gridTextPaint.setAntiAlias(true);
        gridTextPaint.setColor(Color.LTGRAY);
        gridTextPaint.setAlpha(255);
        gridTextPaint.setTextSize(P_SIZE*3);

        generalPaint5 = new Paint();
        generalPaint5.setStyle(Paint.Style.STROKE);
        generalPaint5.setAntiAlias(true);
        generalPaint5.setColor(Color.LTGRAY);
        generalPaint5.setAlpha(100);

        generalPaint2.setStyle(Paint.Style.STROKE);
        generalPaint2.setStrokeCap(Paint.Cap.ROUND);
        generalPaint2.setAntiAlias(true);
        projectedLinePaint = new Paint();

        projectedLinePaint.setStyle(Paint.Style.STROKE);
        projectedLinePaint.setStrokeCap(Paint.Cap.ROUND);
        projectedLinePaint.setAntiAlias(true);
        projectedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 20, 10, 20}, 0));
        projectedLinePaint.setColor((Color.CYAN));

        length = calculateDistance(controlPositionEnd.x, controlPositionEnd.y, controlPositionStart.x, controlPositionStart.y);
        lineAngle = -calculateAngle(controlPositionStart.x, controlPositionStart.y, controlPositionEnd.x, controlPositionEnd.y);
        distanceToLinePoint = (int) calculateDistanceToLinePoint(targetPoint.x, targetPoint.y);
        projectedVector = calculateProjectedVector(targetPoint.x, targetPoint.y);
        projectedX = calculateProjectedPoints(targetPoint.x, targetPoint.y).x;
        projectedY = calculateProjectedPoints(targetPoint.x, targetPoint.y).y;
        controlPointsLineStartLength = length;
        visualMultiplier = 1f;
        arcAnimationIncrement = 0f;
        currentArcState = -1;
        arcStateOne = 1;
        arcStateTwo = -1;
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setAntiAlias(true);
        arcPaint.setPathEffect(new DashPathEffect(new float[]{10, 20, 10, 20}, 0));
        arcPaint.setColor((Color.CYAN));

        gridSubDivision = 6;
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

    public float calculateDistance(int originX, int originY, int targetX, int targetY ) {
        float deltaX = targetX - originX;
        float deltaY = (targetY - originY);

        float distanceToPoint;
        distanceToPoint = (float) Math.sqrt((deltaX) * (deltaX) + (deltaY) * (deltaY));

        return distanceToPoint;
    }

    public float calculateAngle(int startX, int startY, int endX, int endY ) {

        float radAngle;
        float degAngle;

        float deltaY;
        float deltaX;

        deltaX = endX - startX;
        deltaY = (endY - startY);

        radAngle = (float) Math.atan2(-1*deltaY, deltaX);
        degAngle =  ((57.2957795f * radAngle));

        return degAngle;
    }

    private float calculateDistanceToLinePoint(int targetX, int targetY) {

        Point projectedPoint;
        projectedPoint = new Point();
        float projectedVec;
        float distanceToPoint;

        projectedVec = calculateProjectedVector(targetX, targetY);

        projectedPoint.x = (int) (controlPositionStart.x + ((projectedVec) * ( controlPositionEnd.x - controlPositionStart.x)));
        projectedPoint.y = (int) (controlPositionStart.y + ((projectedVec) * ( controlPositionEnd.y - controlPositionStart.y)));
        distanceToPoint = (int) calculateDistance(projectedPoint.x, projectedPoint.y, targetX, targetY);

        return distanceToPoint;
    }

    public float calculateProjectedVector(int targetX, int targetY) {
        float projectedVec;

        projectedVec = (float) (((controlPositionEnd.x - controlPositionStart.x) * (targetX - controlPositionStart.x) +
                        (controlPositionEnd.y - controlPositionStart.y) * (targetY - controlPositionStart.y) )
                        / (Math.pow(length, 2)));

        return projectedVec;
    }

    public Point calculateProjectedPoints(int targetX, int targetY) {

        Point projectedPoint;
        projectedPoint = new Point();

        float projectedVec;

        projectedVec = calculateProjectedVector(targetX, targetY);

        projectedPoint.x = (int) (controlPositionStart.x + ((projectedVec) * (controlPositionEnd.x - controlPositionStart.x)));
        projectedPoint.y = (int) (controlPositionStart.y + ((projectedVec) * (controlPositionEnd.y - controlPositionStart.y)));

        return projectedPoint;
    }

    public void setControlPositionStart(int x, int y)
    {
        this.controlPositionStart.x = x;
        this.controlPositionStart.y = y;
    }

    public void setControlPositionEnd(int x, int y)
    {
        this.controlPositionEnd.x = x;
        this.controlPositionEnd.y = y;
    }

    private void drawGrid(Canvas canvas, int xDim, int yDim) {
        generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
        canvas.drawLine(VectorProjectionView.dimensions.x/2,0,VectorProjectionView.dimensions.x/2, VectorProjectionView.dimensions.y, generalPaint5);
        canvas.drawLine(0,VectorProjectionView.dimensions.y/2,VectorProjectionView.dimensions.x, VectorProjectionView.dimensions.y/2, generalPaint5);

        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 0; x < VectorProjectionView.dimensions.x/xDim; x++)
        {
            int currentX = x*(VectorProjectionView.dimensions.x/xDim);
            canvas.drawLine(currentX,0,currentX, VectorProjectionView.dimensions.y, generalPaint5);
            canvas.drawText(String.valueOf(x-xDim/2), currentX,VectorProjectionView.dimensions.y/2,gridTextPaint);
        }

        for(int y = 0; y < VectorProjectionView.dimensions.y/yDim; y++)
        {
            int currentY = y*(VectorProjectionView.dimensions.y/yDim);
            canvas.drawLine(0,currentY,VectorProjectionView.dimensions.x, currentY, generalPaint5);
            canvas.drawText(String.valueOf(-1*(y-yDim/2)), VectorProjectionView.dimensions.x/2,currentY,gridTextPaint);
        }
    }

    private void handleInput() {
        //if(VectorProjectionView.actionDown)
        {
            clampedInputPoints.get(0).x = VectorProjectionView.points[0].x >= VectorProjectionView.dimensions.x ? VectorProjectionView.dimensions.x : Math.max(VectorProjectionView.points[0].x, 0);
            clampedInputPoints.get(0).y = VectorProjectionView.points[0].y >= VectorProjectionView.dimensions.y ? VectorProjectionView.dimensions.y : Math.max(VectorProjectionView.points[0].y, 0);

            clampedInputPoints.get(1).x = VectorProjectionView.points[1].x >= VectorProjectionView.dimensions.x ? VectorProjectionView.dimensions.x : Math.max(VectorProjectionView.points[1].x, 0);
            clampedInputPoints.get(1).y = VectorProjectionView.points[1].y >= VectorProjectionView.dimensions.y ? VectorProjectionView.dimensions.y : Math.max(VectorProjectionView.points[1].y, 0);
        }

        int minControlPointIndex = currentControlPointIndex;
        int minInputPointIndex = -1;
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < controlPoints.size(); i++) {
            float tempDist = getDistance(controlPoints.get(i), clampedInputPoints.get(0));
            if (tempDist < minDistance) {
                minControlPointIndex = i;
                minDistance = tempDist;
            }
        }

        currentControlPointIndex = minControlPointIndex;
        controlPointPressReset = false;

        double xVectorToInput = (clampedInputPoints.get(0).x - controlPoints.get(minControlPointIndex).x);
        double yVectorToInput = (clampedInputPoints.get(0).y - controlPoints.get(minControlPointIndex).y);
        double distToInput = (Math.sqrt(Math.pow(xVectorToInput, 2) + Math.pow(yVectorToInput,2)));

        double directionToFingerVectorX =  (xVectorToInput/distToInput);
        double directionToFingerVectorY =  (yVectorToInput/distToInput);

        if(distToInput < VectorProjectionView.dimensions.x / 45f)
        {
            controlPoints.get(minControlPointIndex).x = clampedInputPoints.get(0).x;
            controlPoints.get(minControlPointIndex).y = clampedInputPoints.get(0).y;
        }
        else
        {
            controlPoints.get(minControlPointIndex).x += directionToFingerVectorX*distToInput*.35f;
            controlPoints.get(minControlPointIndex).y += directionToFingerVectorY*distToInput*.35f;
        }

        if(VectorProjectionView.numPoints > 1)
        {
            minInputPointIndex = 1 - minInputPointIndex;
            xVectorToInput = (clampedInputPoints.get(1).x - controlPoints.get(1 - minControlPointIndex).x);
            yVectorToInput = (clampedInputPoints.get(1).y - controlPoints.get(1 - minControlPointIndex).y);
            distToInput = (Math.sqrt(Math.pow(xVectorToInput, 2) + Math.pow(yVectorToInput,2)));

            directionToFingerVectorX =  (xVectorToInput/distToInput);
            directionToFingerVectorY =  (yVectorToInput/distToInput);

            if(distToInput < VectorProjectionView.dimensions.x / 45f)
            {
                controlPoints.get(1-minControlPointIndex).x = clampedInputPoints.get(1).x;
                controlPoints.get(1-minControlPointIndex).y = clampedInputPoints.get(1).y;
            }
            else
            {
                controlPoints.get(1 - minControlPointIndex).x += directionToFingerVectorX*distToInput * .35f;
                controlPoints.get(1 - minControlPointIndex).y += directionToFingerVectorY*distToInput * .35f;
            }
        }
    }

    private void drawProjectedLines(Canvas canvas) {
        if(projectedVector >= 0 && projectedVector <= 1)
        {
            generalPaint.setColor((Color.CYAN));
            generalPaint2.setColor((Color.CYAN));
            generalPaint2.setStrokeWidth(P_SIZE*.75f);
            projectedLinePaint.setStrokeWidth(P_SIZE*.75f);
            canvas.drawLine(projectedX, projectedY, targetPoint.x, targetPoint.y, projectedLinePaint);
            pathPaint.setStyle(Paint.Style.FILL);
            generalPaint2.setStyle(Paint.Style.FILL);
            generalPaint2.setColor(Color.YELLOW);
            canvas.drawCircle(projectedX,projectedY,P_SIZE*1.8f,generalPaint2);
            generalPaint2.setStyle(Paint.Style.STROKE);
            pathPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(controlPositionEnd.x, controlPositionEnd.y,P_SIZE*1.8f,pathPaint);
        }
        else
        {
            generalPaint2.setStrokeWidth(P_SIZE*.75f);
            generalPaint2.setStyle(Paint.Style.FILL);
            generalPaint2.setColor((Color.YELLOW));
            canvas.drawCircle(projectedX, projectedY,P_SIZE*1.3f,generalPaint2);
            canvas.drawCircle(projectedX, projectedY,P_SIZE*1.3f,pathPaint);
            generalPaint2.setStrokeWidth(P_SIZE/5);
            generalPaint2.setColor((Color.GREEN));
            canvas.drawLine(projectedX, projectedY, controlPositionEnd.x, controlPositionEnd.y, generalPaint2);
            generalPaint2.setColor((Color.CYAN));
            generalPaint2.setColor((Color.CYAN));
            projectedLinePaint.setStrokeWidth(P_SIZE/5);
            canvas.drawLine(projectedX, projectedY, targetPoint.x, targetPoint.y, projectedLinePaint);
            generalPaint2.setColor((Color.YELLOW));
            generalPaint2.setStyle(Paint.Style.FILL);

            if(projectedVector <= 1)
            {
                canvas.drawCircle(controlPositionEnd.x, controlPositionEnd.y,P_SIZE*1.8f,pathPaint);
            }
            else
            {
                canvas.drawCircle(controlPositionEnd.x, controlPositionEnd.y,P_SIZE*1.8f,generalPaint2);
            }

            generalPaint2.setStyle(Paint.Style.STROKE);
            pathPaint.setStyle(Paint.Style.STROKE);
        }
    }

    private void drawRightAngle(Canvas canvas, float angleDifference) {
        float targetStartPointAngle = angleDifference + 180;

        canvas.save();
        int xSign;
        int ySign;

        if(targetStartPointAngle >= 0 && targetStartPointAngle < 90)
        {
            xSign = 1;
            ySign = -1;
        }
        else if(targetStartPointAngle >= 90 && targetStartPointAngle < 180)
        {
            xSign = -1;
            ySign = -1;
        }
        else if(targetStartPointAngle >= 180 && targetStartPointAngle < 270)
        {
            xSign = -1;
            ySign = 1;
        }
        else
        {
            xSign = 1;
            ySign = 1;
        }

        canvas.rotate(-lineAngle, projectedX,projectedY);

        if(P_SIZE*4 > Math.abs(projectedVector*length))
        {
            visualMultiplier = Math.abs(projectedVector*length/((float)P_SIZE*4f));
        }
        else  if(P_SIZE*4 > distanceToLinePoint)
        {
            visualMultiplier = Math.abs(distanceToLinePoint/((float)P_SIZE*4f));
        }
        else
        {
            visualMultiplier = 1f;
        }

        generalPaint2.setColor((Color.MAGENTA));
        generalPaint2.setStrokeWidth(P_SIZE*.25f);
        canvas.drawRect(projectedX,projectedY,projectedX + visualMultiplier*xSign*P_SIZE*4,projectedY + visualMultiplier*ySign*P_SIZE*4, generalPaint2);
        generalPaint2.setStrokeWidth(P_SIZE*.75f);
        canvas.restore();
    }

    private float drawArc(Canvas canvas, float angleDifference) {
        visualMultiplier = length/controlPointsLineStartLength;
        arcRect.set(controlPositionStart.x - visualMultiplier * P_SIZE*4 - P_SIZE*3,
                controlPositionStart.y - visualMultiplier * P_SIZE*4 - P_SIZE*3,
                controlPositionStart.x + visualMultiplier * P_SIZE*4 + P_SIZE*3,
                controlPositionStart.y + visualMultiplier * P_SIZE*4 + P_SIZE*3);

        arcAnimationIncrement -= .1f;

        if(arcAnimationIncrement < 0)
        {
            arcAnimationIncrement = 0;
        }

        if(angleDifference > 180f)
        {
            angleDifference  = angleDifference - 360;
        }
        else if(angleDifference < -180)
        {
            angleDifference  = angleDifference + 360;
        }

        if(180 + angleDifference > 0 && 180 + angleDifference <= 180   && currentArcState == -1 )
        {
            currentArcState = 1;
            arcAnimationIncrement = 1f;
        }
        else if(180 + angleDifference <= 360 &&  180 + angleDifference > 180  && currentArcState == 1)
        {
            currentArcState = -1;
            arcAnimationIncrement = 1f;
        }

        pathPaint.setStrokeWidth(P_SIZE * .25f);
        canvas.drawArc(arcRect, -lineAngle + angleDifference*arcAnimationIncrement / 2, angleDifference -  angleDifference*arcAnimationIncrement, false, pathPaint);
        return angleDifference;
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(0,0, xDimension, 0, generalPaint);
        canvas.drawLine(0,0, 0, yDimension, generalPaint);
        canvas.drawLine(0, yDimension, xDimension, yDimension, generalPaint);
        canvas.drawLine(xDimension,0, xDimension, yDimension, generalPaint);
    }
}