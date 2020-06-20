package com.dopaminequest.mathalgorithmsdatastructures.views.BezierCurves;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import com.dopaminequest.mathalgorithmsdatastructures.tools.BezierCurve;
import com.dopaminequest.mathalgorithmsdatastructures.views.VectorProjection.VectorProjectionView;

import java.util.ArrayList;


public class MainCanvas extends Object {

    final public int PADDING = 1;
    private Paint generalPaint;
    private Paint pathPaint;
    private Paint currentControlPointPaint;

    public boolean resettingMainCanvas = true;
    public int numSquares;
    public Point canvasDimensions;

    private double time = 0;
    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;

    private int CIRCLE_RADIUS = BezierCurvesView.dimensions.x/110;
    private int numLinePoints;
    private float currentAnimationIndex;
    private int animationDirection;
    private int currentTouchControlPointAlpha;
    private int currentTouchControlPointRadius;
    private boolean controlPointPressReset;
    private int currentControlPointIndex;
    private int MAX_SIZE_CONTROL_POINT = CIRCLE_RADIUS*12;

    private ArrayList<Point> controlPoints;
    private Point[] linePoints;
    private boolean pause;
    private boolean animate;
    private Path path;
    private BezierCurve bc;

    Point clampedInput;

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

        time+=.01;

        if(time > 1)
        {
            time = 1;
        }

        if(BezierCurvesView.actionDown)
        {
            clampedInput.x = BezierCurvesView.input.x > BezierCurvesView.dimensions.x ? BezierCurvesView.dimensions.x : Math.max(BezierCurvesView.input.x, 0);
            clampedInput.y = BezierCurvesView.input.y > BezierCurvesView.dimensions.y ? BezierCurvesView.dimensions.y : Math.max(BezierCurvesView.input.y, 0);

            if(controlPointPressReset)
            {

                controlPointPressReset = false;
                currentTouchControlPointAlpha = 34;
            }

            currentTouchControlPointAlpha -= 3;

            if(currentTouchControlPointAlpha < 0)
            {
                currentTouchControlPointAlpha = 0;
            }

            currentTouchControlPointRadius += CIRCLE_RADIUS;

            if(currentTouchControlPointRadius > MAX_SIZE_CONTROL_POINT)
            {
                currentTouchControlPointRadius = MAX_SIZE_CONTROL_POINT;
            }
        }

        int minIndex = -1;
        float minDistance = Float.MAX_VALUE;

        for(int i = 0;  i < controlPoints.size(); i++)
        {
            float tempDist = getDistance(controlPoints.get(i), clampedInput);
            if(tempDist < minDistance)
            {
                minIndex = i;
                minDistance = tempDist;
            }
        }
        currentControlPointIndex = minIndex;

        double xVectorToInput = (clampedInput.x - controlPoints.get(minIndex).x);
        double yVectorToInput = (clampedInput.y - controlPoints.get(minIndex).y);
        double distToInput = (Math.sqrt(Math.pow(xVectorToInput, 2) + Math.pow(yVectorToInput, 2)));

        double directionToFingerVectorX =  (xVectorToInput/distToInput);
        double directionToFingerVectorY =  (yVectorToInput/distToInput);

        if(distToInput < BezierCurvesView.dimensions.x / 45f)
        {
            controlPoints.get(minIndex).x = clampedInput.x;
            controlPoints.get(minIndex).y = clampedInput.y;
        }
        else
        {
            controlPoints.get(minIndex).x += directionToFingerVectorX*distToInput*.5f;
            controlPoints.get(minIndex).y += directionToFingerVectorY*distToInput*.5f;
        }

        if(!BezierCurvesView.actionDown)
        {
            currentTouchControlPointAlpha = 55;
            currentTouchControlPointRadius = CIRCLE_RADIUS*3;
            controlPointPressReset = true;
        }

        if(!pause)
        {
            currentAnimationIndex += animationDirection*(numLinePoints/200f);
        }

        if(currentAnimationIndex > numLinePoints - 1 || currentAnimationIndex < 0)
        {
            animationDirection *= -1;
            currentAnimationIndex += animationDirection;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        pathPaint.setColor(Color.GREEN);
        generalPaint.setColor((Color.RED));

        drawBorder(canvas);

        generalPaint.setColor((int) (Color.BLUE));
        generateBezierPath();

        pathPaint.setShader(new LinearGradient(
                BezierCurvesView.dimensions.x / 2f,
                BezierCurvesView.dimensions.y / 2f,
                    BezierCurvesView.dimensions.x,
                    BezierCurvesView.dimensions.y,
                    Color.rgb(180,255,0),
                    Color.rgb(0,255,240),
                    Shader.TileMode.MIRROR));

        canvas.drawPath(path, pathPaint);
        canvas.drawLine(p1.x, p1.y ,p2.x, p2.y, generalPaint);
        canvas.drawLine(p2.x, p2.y ,p3.x, p3.y, generalPaint);
        canvas.drawLine(p3.x, p3.y ,p4.x, p4.y, generalPaint);

        if(animate)
        {
            animateCurves(canvas);
        }

        drawControlPoints(canvas);
    }

    private void drawControlPoints(Canvas canvas) {
        generalPaint.setColor((Color.RED));

        for(int i = 0;  i < controlPoints.size(); i++)
        {
            canvas.drawCircle(controlPoints.get(i).x,controlPoints.get(i).y,CIRCLE_RADIUS,generalPaint);
        }

        generalPaint.setColor((Color.MAGENTA));
        generalPaint.setAlpha(55);

        for(int i = 0;  i < controlPoints.size(); i++)
        {
            if(i == currentControlPointIndex)
            {
                generalPaint.setAlpha(currentTouchControlPointAlpha);
                canvas.drawCircle(controlPoints.get(i).x,controlPoints.get(i).y, currentTouchControlPointRadius, generalPaint);
            }
            else
            {
                generalPaint.setAlpha(55);
                canvas.drawCircle(controlPoints.get(i).x,controlPoints.get(i).y,CIRCLE_RADIUS*3, generalPaint);
            }
        }
    }

    private void generateBezierPath() {
        path.reset();
        boolean firstPoint = true;
        linePoints = bc.calculatePoints(numLinePoints);

        for(int index = 0; index < numLinePoints; index++)
        {
            if(firstPoint)
            {
                path.moveTo(linePoints[index].x,linePoints[index].y);
                firstPoint = false;
            }
            else
            {
                path.lineTo(linePoints[index].x,linePoints[index].y);
            }
        }

        path.lineTo(controlPoints.get(controlPoints.size()-1).x,controlPoints.get(controlPoints.size()-1).y);
    }

    private void animateCurves(Canvas canvas) {
        generalPaint.setColor((Color.RED));

        float animationPercentage = (float)currentAnimationIndex/numLinePoints;

        Point midP1toP2 = new Point((int) (p1.x + (p2.x - p1.x) * animationPercentage), (int) (p1.y + (p2.y - p1.y) * animationPercentage));
        Point midP2toP3 = new Point((int) (p2.x + (p3.x - p2.x) * animationPercentage), (int) (p2.y + (p3.y - p2.y) * animationPercentage));
        Point midP3toP4 = new Point((int) (p3.x + (p4.x - p3.x) * animationPercentage), (int) (p3.y + (p4.y - p3.y) * animationPercentage));

        Point midPointLine1 = new Point((int) (midP1toP2.x + (midP2toP3.x - midP1toP2.x) * animationPercentage), (int) (midP1toP2.y + (midP2toP3.y - midP1toP2.y) * animationPercentage));
        Point midPointLine2 = new Point((int) (midP2toP3.x + (midP3toP4.x - midP2toP3.x) * animationPercentage), (int) (midP2toP3.y + (midP3toP4.y - midP2toP3.y) * animationPercentage));

        canvas.drawLine(midP1toP2.x, midP1toP2.y, midP2toP3.x, midP2toP3.y, generalPaint);
        canvas.drawLine(midP2toP3.x, midP2toP3.y, midP3toP4.x, midP3toP4.y, generalPaint);
        canvas.drawLine(midP2toP3.x, midP2toP3.y, midP3toP4.x, midP3toP4.y, generalPaint);
        canvas.drawLine(midP2toP3.x, midP2toP3.y, midP3toP4.x, midP3toP4.y, generalPaint);
        canvas.drawLine(midPointLine1.x, midPointLine1.y, midPointLine2.x, midPointLine2.y, generalPaint);

        generalPaint.setColor((Color.BLUE));
        canvas.drawCircle(midP1toP2.x, midP1toP2.y, CIRCLE_RADIUS / 2f, generalPaint);
        canvas.drawCircle(midP2toP3.x, midP2toP3.y, CIRCLE_RADIUS / 2f, generalPaint);
        canvas.drawCircle(midP3toP4.x, midP3toP4.y, CIRCLE_RADIUS / 2f, generalPaint);
        canvas.drawCircle(midPointLine1.x, midPointLine1.y, CIRCLE_RADIUS / 2f, generalPaint);
        canvas.drawCircle(midPointLine2.x, midPointLine2.y, CIRCLE_RADIUS / 2f, generalPaint);

        generalPaint.setAlpha(255);
        generalPaint.setColor((Color.YELLOW));
        canvas.drawCircle(linePoints[(int) currentAnimationIndex].x, linePoints[(int) currentAnimationIndex].y, CIRCLE_RADIUS, generalPaint);
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(0,0, BezierCurvesView.dimensions.x, 0, generalPaint);
        canvas.drawLine(0,0, 0, BezierCurvesView.dimensions.y, generalPaint);
        canvas.drawLine(0,BezierCurvesView.dimensions.y, BezierCurvesView.dimensions.x, BezierCurvesView.dimensions.y, generalPaint);
        canvas.drawLine(BezierCurvesView.dimensions.x,0, BezierCurvesView.dimensions.x, BezierCurvesView.dimensions.y, generalPaint);
    }

    public void init()
    {
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        pathPaint = new Paint();
        currentControlPointPaint = new Paint();
        createMainCanvas(numSquares);
        position = new Point();
        scale = new Point();
        resettingMainCanvas = false;
        int widthOffset = BezierCurvesView.dimensions.x / 13;

        p1 = new Point(widthOffset,BezierCurvesView.dimensions.x-widthOffset*3);
        p2 = new Point(widthOffset, widthOffset);
        p3 = new Point(BezierCurvesView.dimensions.x - widthOffset, widthOffset);
        p4 = new Point(BezierCurvesView.dimensions.x - widthOffset,BezierCurvesView.dimensions.x - widthOffset * 3);
        clampedInput = new Point(0,0);
        clampedInput.x = p4.x;
        clampedInput.y = p4.y;

        controlPoints = new ArrayList<Point>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        controlPoints.add(p3);
        controlPoints.add(p4);
        bc = new BezierCurve(controlPoints);
        numLinePoints = 200;
        linePoints = new Point[numLinePoints];
        initializeLinePoints();
        controlPointPressReset = true;
        currentControlPointIndex = -1;
        pause = false;
        animate = true;
        path = new Path();

        currentAnimationIndex = 0f;
        animationDirection = 1;
        currentTouchControlPointAlpha = 0;
        currentTouchControlPointRadius = CIRCLE_RADIUS*3;
        currentControlPointPaint.setColor((Color.MAGENTA));
        currentControlPointPaint.setAntiAlias(true);
        generalPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(CIRCLE_RADIUS);
        pathPaint.setAntiAlias(true);
        pathPaint.setShader(new LinearGradient(
                BezierCurvesView.dimensions.x / 2f,
                BezierCurvesView.dimensions.y / 2f,
                    BezierCurvesView.dimensions.x,
                    BezierCurvesView.dimensions.y,
                    Color.rgb(180,255,0),
                    Color.rgb(0,255,240),
                    Shader.TileMode.MIRROR));
    }

    private void initializeLinePoints()
    {
        for(int i = 0; i < numLinePoints; i++)
        {
            linePoints[i] = new Point(0,0);
        }
    }

    public void reset() {
        init();
    }

    public void changeState() {
        createMainCanvas(numSquares);
    }

    private void createMainCanvas(int numSquares)
    {

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