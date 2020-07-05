package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.Derivative;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dopaminequest.mathalgorithmsdatastructures.R;
import java.util.ArrayList;

public class MainCanvas extends Object {

    public boolean resettingMainCanvas = true;
    public Point canvasDimensions;
    private Paint generalPaint;
    private Paint generalPaint2;
    private Paint generalPaint3;
    private Paint generalPaint4;
    private Paint secantPaint;
    private Paint grayPaint;
    private Paint pathPaint;
    private final int yDimension = DerivativeView.dimensions.y;
    private final int xDimension = DerivativeView.dimensions.x;
    private boolean pause;
    private boolean animate;
    private int P_SIZE = DerivativeView.dimensions.x/110;
    private int numRects;
    private int rectLineAlpha;
    private int MIN_NUM_RECTS =  10;
    private int MAX_NUM_RECTS =  250;
    private double derivative;
    private float animateDirection;
    private SeekBar seekBar;
    private TextView derivativeTextView;
    private TextView hValTextView;
    private float ANIMATE_SPEED = .75f;
    private float animateProgress;
    private Path path;
    private Point[] sliders;
    private float SLIDER_RADIUS = P_SIZE*1.75f;
    private float scalar;
    private int functionNum;
    private float gridScaleFactor;
    private boolean controlPointPressReset;
    private int currentControlPointIndex;
    private Point clampedInput;
    private Paint generalPaint5;
    private Paint gridTextPaint;
    private Paint negativePaint;
    private int gridSubDivision;
    private Paint greenLinesPaint;
    private Paint circleOutlinePaint;
    private Paint slopeTrianglePaint;
    private Paint triangleTextPaint;
    public Activity activity;
    public double hVal;

    ArrayList<Point> points;

    public MainCanvas(Activity context)
    {
        activity = context;
        seekBar = (SeekBar) context.findViewById(R.id.seekBarSortSpeed);
        derivativeTextView = (TextView) context.findViewById(R.id.num_points_text);
        hValTextView = (TextView) context.findViewById(R.id.h_val_text);
        init();
    }

    @Override
    public void update() {

        if(resettingMainCanvas)
        {
            return;
        }

        if(DerivativeView.actionDown)
        {
            handleInput();
        }
        else
        {
            controlPointPressReset = true;
        }

        sliders[0].y = getFunctionY(sliders[0].x, scalar,functionNum);
        sliders[1].y = getFunctionY(sliders[1].x, scalar, functionNum);
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        if(animate)
        {
            animate();
        }

        generateFunctionPath();
        derivative = calculateSlope();
        canvas.drawPath(path, generalPaint2);

        drawSecant(canvas);

        sliders[0].x = sliders[0].x > DerivativeView.dimensions.x ? DerivativeView.dimensions.x : Math.max(sliders[0].x, 0);
        sliders[0].y = sliders[0].y > DerivativeView.dimensions.y ? DerivativeView.dimensions.y : Math.max(sliders[0].y, 0);
        sliders[1].x = sliders[1].x > DerivativeView.dimensions.x ? DerivativeView.dimensions.x : Math.max(sliders[1].x, 0);
        sliders[1].y = sliders[1].y > DerivativeView.dimensions.y ? DerivativeView.dimensions.y : Math.max(sliders[1].y, 0);
        RectF r = new RectF(50,50,100,100);
        drawSliders(canvas, r);
        drawSlopeTriangle(canvas);

        double tempDerivative =  derivative;

        if(tempDerivative != 0)
        {
            tempDerivative = -1*tempDerivative;
        }

        derivativeTextView.setText("Derivative: " + String.format("%.2f", tempDerivative));
        hValTextView.setText("h = " + String.format("%.2f", hVal/scalar));

        drawGrid(canvas, gridSubDivision,gridSubDivision);
        drawBorder(canvas);
    }

    private void drawSlopeTriangle(Canvas canvas)
    {
        canvas.drawLine(sliders[0].x, sliders[0].y, sliders[1].x, sliders[0].y,slopeTrianglePaint);
        canvas.drawLine(sliders[1].x, sliders[1].y, sliders[1].x, sliders[0].y,slopeTrianglePaint);

        canvas.drawCircle(sliders[1].x, sliders[0].y,SLIDER_RADIUS/2, secantPaint);
        canvas.drawCircle(sliders[1].x, sliders[0].y,SLIDER_RADIUS/2, circleOutlinePaint);

        float textSize = (sliders[1].x-sliders[0].x)/(float)P_SIZE*1.5f;

        if(textSize > P_SIZE*5)
        {
            triangleTextPaint.setAlpha(255);
            textSize  = P_SIZE*5;
        }
        else if(textSize <= P_SIZE*5/2.5f)
        {
            triangleTextPaint.setAlpha((int) (255f*(textSize/(P_SIZE*5/2.5f))));
            //textSize  = P_SIZE*5/2.5f;
        }

        triangleTextPaint.setTextSize(textSize);

        String changeInXString = activity.getResources().getString(R.string.change_in_x);
        String changeInYString = activity.getResources().getString(R.string.change_in_y);

        if(derivative >= 0)
        {
            canvas.drawText(changeInXString,(sliders[0].x)/1f,sliders[0].y - textSize/2, triangleTextPaint);

        }
        else
        {
            canvas.drawText(changeInXString,sliders[0].x,sliders[0].y + textSize, triangleTextPaint);
        }
        canvas.drawText(changeInYString,sliders[1].x,(sliders[0].y+sliders[1].y)/2f , triangleTextPaint);


    }

    private void drawSecant(Canvas canvas)
    {
        double x1;
        double x0;
        double b;

        if(derivative!=0f)
        {
            b = sliders[1].y-derivative*sliders[1].x;
            x1 = 0-b/derivative;
            x0 = (DerivativeView.dimensions.y-b)/(derivative);

            canvas.drawLine((float)x0, DerivativeView.dimensions.y, (float)x1, 0,secantPaint);
        }
        else
        {
            canvas.drawLine(0, sliders[0].y, DerivativeView.dimensions.x, sliders[1].y,secantPaint);
        }
    }

    public double calculateSlope()
    {
        double changeInY = sliders[1].y - sliders[0].y;
        double changeInX = sliders[1].x - sliders[0].x;
        hVal = changeInX;
        return changeInY/changeInX;
    }

    private void drawSliders(Canvas canvas, RectF r) {
        generalPaint4.setColor(Color.MAGENTA);

        r.left = sliders[0].x - (P_SIZE*.45f)/3;
        r.right = sliders[0].x + (P_SIZE*.45f)/3;
        r.bottom = DerivativeView.dimensions.y;
        r.top = sliders[0].y;
        canvas.drawRect(r, generalPaint4);

        if(derivative >= 0)
        {
            r.bottom = DerivativeView.dimensions.y;
            r.top = sliders[1].y;
        }
        else
        {
            r.bottom = DerivativeView.dimensions.y;
            r.top = sliders[0].y;
        }

        r.left = sliders[1].x - (P_SIZE*.45f)/3;
        r.right = sliders[1].x + (P_SIZE*.45f)/3;


        canvas.drawRect(r, generalPaint4);

        generalPaint4.setColor(Color.CYAN);

        canvas.drawCircle(sliders[0].x, sliders[0].y, SLIDER_RADIUS/2, secantPaint);
        canvas.drawCircle(sliders[1].x, sliders[1].y, SLIDER_RADIUS/2, secantPaint);

        generalPaint.setColor((Color.RED));
        pathPaint.setColor(Color.GREEN);

        canvas.drawCircle(sliders[0].x, sliders[0].y, SLIDER_RADIUS/2, circleOutlinePaint);
        canvas.drawCircle(sliders[1].x, sliders[1].y, SLIDER_RADIUS/2, circleOutlinePaint);
    }

    private void animate() {
        animateProgress +=  (animateDirection * ANIMATE_SPEED);

        if(animateProgress > MAX_NUM_RECTS)
        {
            animateProgress  = MAX_NUM_RECTS;
            animateDirection *=-1;
        }
        else if(animateProgress < MIN_NUM_RECTS)
        {
            animateProgress  = MIN_NUM_RECTS;
            animateDirection *=-1;
        }

        seekBar.setProgress((int)animateProgress - MIN_NUM_RECTS);
    }

    private void generateFunctionPath() {
        boolean firstPoint = true;
        path.reset();

        for(int i = 0; i < DerivativeView.dimensions.x + P_SIZE*.5f; i += P_SIZE*.5f)
        {
            int yVal = getFunctionY(i, scalar,functionNum);

            if(firstPoint)
            {
                path.moveTo(i,yVal);
                firstPoint = false;
            }
            else
            {
                path.lineTo(i,yVal);
            }
        }
    }

    private void drawGrid(Canvas canvas, int xDim, int yDim) {

        generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
        canvas.drawLine(DerivativeView.dimensions.x/2,0,DerivativeView.dimensions.x/2, DerivativeView.dimensions.y, generalPaint5);
        canvas.drawLine(0,DerivativeView.dimensions.y/2,DerivativeView.dimensions.x, DerivativeView.dimensions.y/2, generalPaint5);
        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 0; x < DerivativeView.dimensions.x/xDim; x++)
        {

            int currentX = x*(DerivativeView.dimensions.x/xDim);
            canvas.drawLine(currentX,0,currentX, DerivativeView.dimensions.y, generalPaint5);
            canvas.drawText(String.valueOf(x-xDim/2), currentX,DerivativeView.dimensions.y/2,gridTextPaint);
        }

        for(int y = 0; y < DerivativeView.dimensions.y/yDim; y++)
        {
            int currentY = y*(DerivativeView.dimensions.y/yDim);
            canvas.drawLine(0,currentY,DerivativeView.dimensions.x, currentY, generalPaint5);
            canvas.drawText(String.valueOf(-1*(y-yDim/2)), DerivativeView.dimensions.x/2,currentY,gridTextPaint);
        }
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
        generalPaint2 = new Paint();
        generalPaint3 = new Paint();
        generalPaint4 = new Paint();
        pathPaint = new Paint();
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
                DerivativeView.dimensions.x / 2f,
                DerivativeView.dimensions.y / 2f,
                DerivativeView.dimensions.x,
                DerivativeView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

        negativePaint = new Paint();

        negativePaint.setStyle(Paint.Style.FILL);
        negativePaint.setStrokeCap(Paint.Cap.ROUND);
        negativePaint.setStrokeWidth(P_SIZE*.75f);
        negativePaint.setAntiAlias(true);
        negativePaint.setShader(new LinearGradient(
                DerivativeView.dimensions.x / 2f,
                DerivativeView.dimensions.y / 2f,
                DerivativeView.dimensions.x,
                DerivativeView.dimensions.y,
                Color.rgb(255,220,30),
                Color.rgb(255,40,100),
                Shader.TileMode.MIRROR));


        grayPaint = new Paint();
        grayPaint.setStyle(Paint.Style.FILL);
        grayPaint.setAntiAlias(true);
        grayPaint.setColor(Color.LTGRAY);

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

        generalPaint3.setStyle(Paint.Style.STROKE);
        generalPaint3.setAntiAlias(true);
        generalPaint3.setColor(Color.rgb(255,100,0));

        generalPaint4.setStyle(Paint.Style.FILL);
        generalPaint4.setStrokeCap(Paint.Cap.ROUND);
        generalPaint4.setAntiAlias(true);
        generalPaint4.setStrokeWidth(P_SIZE*.75f);
        generalPaint4.setColor(Color.CYAN);

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

        numRects = MIN_NUM_RECTS;
        seekBar.setProgress(0);
        seekBar.setMax(MAX_NUM_RECTS - MIN_NUM_RECTS);
        rectLineAlpha = 0;
        derivative = 0f;
        animateDirection = 1;
        animate = false;
        animateProgress = numRects;
        path = new Path();

        sliders = new Point[2];
        sliders[0] = new Point((int) SLIDER_RADIUS*3, (int) (DerivativeView.dimensions.y*.25f));
        sliders[1] = new Point((int) (DerivativeView.dimensions.x-SLIDER_RADIUS*3), (int) (DerivativeView.dimensions.y*.25f));

        controlPointPressReset = true;
        currentControlPointIndex = 0;

        clampedInput = new Point(0,0);
        clampedInput.x =  sliders[0].x;
        clampedInput.y =  sliders[0].y;

        functionNum = 0;

        gridSubDivision = 6;

        scalar = (float) (DerivativeView.dimensions.x/gridSubDivision);

        gridScaleFactor = (DerivativeView.dimensions.x*DerivativeView.dimensions.x/(float)(gridSubDivision*gridSubDivision));

        secantPaint = new Paint();
        secantPaint.setStyle(Paint.Style.FILL);
        secantPaint.setAntiAlias(true);
        secantPaint.setStrokeWidth(P_SIZE*.75f);
        secantPaint.setColor(Color.GREEN);

        slopeTrianglePaint = new Paint();
        slopeTrianglePaint.setStyle(Paint.Style.FILL);
        slopeTrianglePaint.setAntiAlias(true);
        slopeTrianglePaint.setStrokeWidth(P_SIZE*.5f);
        slopeTrianglePaint.setColor(Color.BLUE);
        slopeTrianglePaint.setPathEffect(new DashPathEffect(new float[]{10, 20, 10, 20}, 0));

        triangleTextPaint = new Paint();
        triangleTextPaint.setAntiAlias(true);
        triangleTextPaint.setColor(Color.BLACK);
        triangleTextPaint.setAlpha(255);
        triangleTextPaint.setTextSize(P_SIZE*4);

        hVal = 0d;
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
        numRects = progress + MIN_NUM_RECTS;
        if(!animate)
        {
            animateProgress = numRects;
        }
        System.out.println("progress: "+ progress);
    }

    public double getHVal() {
        return hVal/scalar;
    }

    public int getMaxNumRects() {
        return MAX_NUM_RECTS;
    }

    public void setSeekBar(SeekBar seekBar) {
    }

    public int getFunctionY(int x, float scalar, int functionNum)
    {
        switch (functionNum)
        {
            case 0:
                float fun0 = (float) ((float) 2f*Math.sin((DerivativeView.dimensions.x/2f-x-(DerivativeView.dimensions.x/2f-3))/ scalar));
                return (int) (DerivativeView.dimensions.y*.5f + scalar/1f*fun0);

            case 1:
                float fun1 = (float) ((float) 2 + -1*(Math.pow((DerivativeView.dimensions.x/2f-x)/scalar, 2)/3));
                return (int) (DerivativeView.dimensions.y*.5f + scalar/1f*fun1);

            case 2:
                float fun3 = .25f*(float) Math.sinh((DerivativeView.dimensions.x/2f-x)/ scalar);
                return (int) (DerivativeView.dimensions.y*.5f + scalar/1f*fun3);
            case 3:
                float fun4 = 2f*(float)Math.cbrt((DerivativeView.dimensions.x/2f-x)/ scalar);
                return (int) (DerivativeView.dimensions.y*.5f + scalar/1f*fun4);
            case 4:
                float fun5 = (float) ((float) 3f*Math.sin(Math.pow((DerivativeView.dimensions.x/2f-x)/ scalar, 3)/3));
                return (int) (DerivativeView.dimensions.y*.5f + scalar/1f*fun5);
        }
        return -1;
    }

    private void handleInput() {
        clampedInput.x = DerivativeView.input.x > DerivativeView.dimensions.x ? DerivativeView.dimensions.x : Math.max(DerivativeView.input.x, 0);
        clampedInput.y = DerivativeView.input.y > DerivativeView.dimensions.y ? DerivativeView.dimensions.y : Math.max(DerivativeView.input.y, 0);
        clampedInput.y = getFunctionY(clampedInput.x,scalar, functionNum);

        int minIndex = -1;
        float minDistance = Float.MAX_VALUE;

        if(controlPointPressReset)
        {
            for(int i = 0;  i < sliders.length; i++)
            {
                float tempDist = Math.abs(sliders[i].x - clampedInput.x);
                if(tempDist < minDistance)
                {
                    minIndex = i;
                    minDistance = tempDist;
                }
            }
            currentControlPointIndex = minIndex;
            controlPointPressReset = false;
        }
        else
        {
            minIndex = currentControlPointIndex;
        }

        double xVectorToInput = (clampedInput.x - sliders[minIndex].x);
        double yVectorToInput = (clampedInput.y - sliders[minIndex].y);
        double distToInput = (Math.sqrt(Math.pow(xVectorToInput,2) + Math.pow(yVectorToInput,2)));

        double directionToFingerVectorX =  (xVectorToInput/distToInput);
        double directionToFingerVectorY =  (yVectorToInput/distToInput);

        if(distToInput < DerivativeView.dimensions.x / 35f)
        {
            sliders[minIndex].x = clampedInput.x;
            sliders[minIndex].y = clampedInput.y;
        }
        else
        {
            sliders[minIndex].x += directionToFingerVectorX*distToInput*.5f;
            sliders[minIndex].y += directionToFingerVectorY*distToInput*.5f;
        }

        float distToPoint =  Math.abs(sliders[0].x - sliders[1].x);
        float minDist = P_SIZE*2f;
        if(distToPoint < minDist || sliders[0].x >=sliders[1].x )
        {
            if(minIndex == 0)
            {
                sliders[1].x = (int) (sliders[0].x + minDist);
                if(sliders[1].x > DerivativeView.dimensions.x)
                {
                    sliders[1].x = DerivativeView.dimensions.x;
                    sliders[0].x = (int) (sliders[1].x - minDist);
                }
            }
            else
            {
                sliders[0].x = (int) (sliders[1].x - minDist);

                if(sliders[0].x < 0)
                {
                    sliders[0].x = 0;
                    sliders[1].x = (int) minDist;
                }
            }
        }
    }

    public PointF convertPixelToCoords(Point p)
    {
        return new PointF(((float)p.x/((float)DerivativeView.dimensions.x/(float)gridSubDivision) - gridSubDivision/2), -1*((float)p.y/((float)DerivativeView.dimensions.y/(float)gridSubDivision) - gridSubDivision/2f));
    }

    public void setFunctionNum(int num)
    {
        functionNum = num;
    }

    public float sV(float x)
    {

        return x/scalar;

    }

    public float oV(float x)
    {

        return (DerivativeView.dimensions.x/2f-x);

    }
}


