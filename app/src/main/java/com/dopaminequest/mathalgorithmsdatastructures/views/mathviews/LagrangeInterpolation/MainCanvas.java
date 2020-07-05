package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.LagrangeInterpolation;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Shader;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dopaminequest.mathalgorithmsdatastructures.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MainCanvas extends Object {

    public boolean resettingMainCanvas = true;
    public Point canvasDimensions;
    private Paint generalPaint;
    private Paint generalPaint2;
    private final int yDimension = LagrangeInterpolationView.dimensions.y;
    private final int xDimension = LagrangeInterpolationView.dimensions.x;
    private boolean pause;
    private boolean animate;
    private int P_SIZE = LagrangeInterpolationView.dimensions.x/110;
    private final int CONTROL_POINT_RADIUS = 2 * P_SIZE;
    private TextView numPointsView;
    private Path path;
    private float SLIDER_RADIUS = P_SIZE*1.75f;
    private float scalar;
    private float gridScaleFactor;
    private boolean controlPointPressReset;
    private int currentControlPointIndex;
    private Point clampedInput;
    private Paint generalPaint5;
    private Paint gridTextPaint;
    private int gridSubDivision;
    private Paint greenLinesPaint;
    private Paint circleOutlinePaint;
    public Activity activity;
    public double hVal;
    public Stack<Integer> stack;
    ArrayList<PointF> convertedPoints;
    ArrayList<Point> points;
    private int MAX_POINTS = 10;

    HashMap<Integer, Boolean> duplicateValuesChecker;

    public MainCanvas(Activity context)
    {
        activity = context;
        numPointsView = (TextView) context.findViewById(R.id.lagrange_interpolation_text);
        init();
    }

    @Override
    public void update()
    {
        if(resettingMainCanvas)
        {
            return;
        }

        if(LagrangeInterpolationView.actionDown)
        {
            handleInput();
        }
        else
        {
            controlPointPressReset = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        generateFunctionPath();
        canvas.drawPath(path, generalPaint2);

        for(int i = 0; i < points.size(); i++)
        {

            canvas.drawCircle(points.get(i).x, points.get(i).y, CONTROL_POINT_RADIUS, generalPaint);
            canvas.drawCircle(points.get(i).x, points.get(i).y, CONTROL_POINT_RADIUS, circleOutlinePaint);
        }

        drawGrid(canvas, gridSubDivision,gridSubDivision);
        drawBorder(canvas);
    }

    private void generateFunctionPath() {
        boolean firstPoint1 = true;
        path.reset();

        convertedPoints.clear();
        for(int j = 0; j <  points.size(); j++)
        {
            convertedPoints.add(convertPixelToCoords(points.get(j)));
        }

        for(int i = 0; i < LagrangeInterpolationView.dimensions.x; i += P_SIZE*.25f)
        {
            double functionY = 0f;

            float cI = ((float)i-(float)LagrangeInterpolationView.dimensions.x/2f)/((float)LagrangeInterpolationView.dimensions.x/(float)gridSubDivision);

            for(int pointsIndex = 0; pointsIndex < convertedPoints.size(); pointsIndex++)
            {
                float tempTop = convertedPoints.get(pointsIndex).y;
                float tempBottom = 1f;

                for (int index = 0; index < convertedPoints.size(); index++)
                {
                    if (index != pointsIndex)
                    {
                        tempTop = tempTop * (cI - convertedPoints.get(index).x);

                    }
                    if (index != pointsIndex)
                    {
                        tempBottom = tempBottom * (convertedPoints.get(pointsIndex).x - convertedPoints.get(index).x);
                    }
                }
                functionY += (tempTop/tempBottom);
            }

            functionY =  (LagrangeInterpolationView.dimensions.y*.5f - scalar/1f*functionY);

            if(firstPoint1)
            {
                path.moveTo(i, (float) functionY);
                firstPoint1 = false;
            }
            else
            {
                path.lineTo(i, (float) functionY);
            }
        }
    }

    public void init()
    {
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        generalPaint2 = new Paint();

        position = new Point();
        scale = new Point();
        resettingMainCanvas = false;
        pause = false;
        animate = true;
        generalPaint.setStyle(Paint.Style.FILL);
        generalPaint.setStrokeCap(Paint.Cap.ROUND);
        generalPaint.setStrokeWidth(P_SIZE*.75f);
        generalPaint.setAntiAlias(true);
        generalPaint.setShader(new LinearGradient(
                LagrangeInterpolationView.dimensions.x / 2f,
                LagrangeInterpolationView.dimensions.y / 2f,
                LagrangeInterpolationView.dimensions.x,
                LagrangeInterpolationView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

        generalPaint2.setStyle(Paint.Style.STROKE);
        generalPaint2.setAntiAlias(true);
        generalPaint2.setStrokeWidth(P_SIZE*.5f);
        generalPaint2.setColor(Color.RED);
        generalPaint2.setAlpha(100);

        circleOutlinePaint = new Paint();
        circleOutlinePaint.setStyle(Paint.Style.STROKE);
        circleOutlinePaint.setAntiAlias(true);
        circleOutlinePaint.setStrokeWidth(P_SIZE*.5f);
        circleOutlinePaint.setColor(Color.MAGENTA);

        generalPaint5 = new Paint();
        generalPaint5.setStyle(Paint.Style.STROKE);
        generalPaint5.setAntiAlias(true);
        generalPaint5.setColor(Color.LTGRAY);
        generalPaint5.setAlpha(100);

        greenLinesPaint = new Paint();
        greenLinesPaint.setStyle(Paint.Style.STROKE);
        greenLinesPaint.setAntiAlias(true);
        greenLinesPaint.setColor(Color.rgb(240,255,0));
        greenLinesPaint.setColor(Color.rgb(0,255,255));

        gridTextPaint = new Paint();
        gridTextPaint.setAntiAlias(true);
        gridTextPaint.setColor(Color.LTGRAY);
        gridTextPaint.setAlpha(255);
        gridTextPaint.setTextSize(P_SIZE*3);

        animate = false;
        path = new Path();

        controlPointPressReset = true;
        currentControlPointIndex = 0;

        clampedInput = new Point(0,0);

        gridSubDivision = 6;

        scalar = (float) (LagrangeInterpolationView.dimensions.x/gridSubDivision);

        gridScaleFactor = (LagrangeInterpolationView.dimensions.x*LagrangeInterpolationView.dimensions.x/(float)(gridSubDivision*gridSubDivision));

        hVal = 0d;
        duplicateValuesChecker = new HashMap<>();
        points= new ArrayList<Point>();
        points.add(new Point((int) (LagrangeInterpolationView.dimensions.x/2 + scalar*-2.5f), (int) (LagrangeInterpolationView.dimensions.y/2 - scalar*-2.5f)));
        points.add(new Point((int) (LagrangeInterpolationView.dimensions.x/2 + scalar*2.5f), (int) (LagrangeInterpolationView.dimensions.y/2 - scalar*2.5f)));

        duplicateValuesChecker.put(points.get(0).x, true);
        duplicateValuesChecker.put(points.get(1).x, true);
        convertedPoints = new ArrayList<PointF>();

        String text = "# Points: " + points.size() + "/"+ MAX_POINTS;
        numPointsView.setText(text);
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

    public void setAnimation(boolean animate) {
        this.animate = animate;
    }

    public void setPosition(int progress) {

    }

    public double getHVal() {
        return hVal/scalar;
    }

    public void setSeekBar(SeekBar seekBar) {
    }

    private void handleInput()
    {
        clampedInput.x = LagrangeInterpolationView.input.x > LagrangeInterpolationView.dimensions.x ? LagrangeInterpolationView.dimensions.x : Math.max(LagrangeInterpolationView.input.x, 0);
        clampedInput.y = LagrangeInterpolationView.input.y > LagrangeInterpolationView.dimensions.y ? LagrangeInterpolationView.dimensions.y : Math.max(LagrangeInterpolationView.input.y, 0);

        if(controlPointPressReset)
        {
            int minIndex = -1;
            float minDistance = Float.MAX_VALUE;

            for(int i = 0;  i < points.size(); i++)
            {
                float tempDist = getDistance(points.get(i), clampedInput);
                if(tempDist < minDistance)
                {
                    minIndex = i;
                    minDistance = tempDist;
                }
            }

            if(minIndex != -1 && getDistance(points.get(minIndex), clampedInput) < CONTROL_POINT_RADIUS*4)
            {
                currentControlPointIndex = minIndex;
//                points.get(currentControlPointIndex).x = clampedInput.x;
//                points.get(currentControlPointIndex).y = clampedInput.y;
            }
            else if(points.size() < MAX_POINTS)
            {
                points.add(new Point(clampedInput.x, clampedInput.y));
                currentControlPointIndex = points.size()-1;
                String text = "# Points: " + points.size() + "/"+ MAX_POINTS;
                numPointsView.setText(text);

                return;
                //duplicateValuesChecker.put(points.get(currentControlPointIndex).x, true);
            }
            else
            {
                currentControlPointIndex = minIndex;
//                points.get(currentControlPointIndex).x = clampedInput.x;
//                points.get(currentControlPointIndex).y = clampedInput.y;
            }

            controlPointPressReset = false;
        }
        //else
        {
            points.get(currentControlPointIndex).x = clampedInput.x;
            points.get(currentControlPointIndex).y = clampedInput.y;
        }
    }

    public PointF convertPixelToCoords(Point p)
    {
        return new PointF(((float)p.x/((float)LagrangeInterpolationView.dimensions.x/(float)gridSubDivision) - gridSubDivision/2), -1*((float)p.y/((float)LagrangeInterpolationView.dimensions.y/(float)gridSubDivision) - gridSubDivision/2f));
    }

    private void drawGrid(Canvas canvas, int xDim, int yDim) {

        generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
        canvas.drawLine(LagrangeInterpolationView.dimensions.x/2,0,LagrangeInterpolationView.dimensions.x/2, LagrangeInterpolationView.dimensions.y, generalPaint5);
        canvas.drawLine(0,LagrangeInterpolationView.dimensions.y/2,LagrangeInterpolationView.dimensions.x, LagrangeInterpolationView.dimensions.y/2, generalPaint5);
        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 0; x < LagrangeInterpolationView.dimensions.x/xDim; x++)
        {

            int currentX = x*(LagrangeInterpolationView.dimensions.x/xDim);
            canvas.drawLine(currentX,0,currentX, LagrangeInterpolationView.dimensions.y, generalPaint5);
            canvas.drawText(String.valueOf(x-xDim/2), currentX,LagrangeInterpolationView.dimensions.y/2,gridTextPaint);
        }

        for(int y = 0; y < LagrangeInterpolationView.dimensions.y/yDim; y++)
        {
            int currentY = y*(LagrangeInterpolationView.dimensions.y/yDim);
            canvas.drawLine(0,currentY,LagrangeInterpolationView.dimensions.x, currentY, generalPaint5);
            canvas.drawText(String.valueOf(-1*(y-yDim/2)), LagrangeInterpolationView.dimensions.x/2,currentY,gridTextPaint);
        }
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(0,0, xDimension, 0, generalPaint);
        canvas.drawLine(0,0, 0, yDimension, generalPaint);
        canvas.drawLine(0, yDimension, xDimension, yDimension, generalPaint);
        canvas.drawLine(xDimension,0, xDimension, yDimension, generalPaint);
    }

    public void remove()
    {
        if(points.size() > 0)
        {
            int xToRemove = points.get(points.size()-1).x;
            duplicateValuesChecker.remove(xToRemove);
            points.remove(points.size()-1);
        }
        String text = "# Points: " + points.size() + "/"+ MAX_POINTS;
        numPointsView.setText(text);
    }
}