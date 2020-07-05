package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.Integral;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dopaminequest.mathalgorithmsdatastructures.R;

public class MainCanvas extends Object {

    public boolean resettingMainCanvas = true;
    public Point canvasDimensions;
    private Paint generalPaint;
    private Paint generalPaint2;
    private Paint generalPaint3;
    private Paint generalPaint4;
    private Paint grayPaint;
    private Paint pathPaint;
    private final int yDimension = IntegralView.dimensions.y;
    private final int xDimension = IntegralView.dimensions.x;
    private boolean pause;
    private boolean animate;
    private int P_SIZE = IntegralView.dimensions.x/110;
    private int numRects;
    private int rectLineAlpha;
    private int MIN_NUM_RECTS =  10;
    private int MAX_NUM_RECTS =  250;
    private float area;
    private float animateDirection;
    private SeekBar seekBar;
    private TextView areaTextView;
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

    public MainCanvas(Activity context)
    {
        seekBar = (SeekBar) context.findViewById(R.id.seekBarSortSpeed);
        areaTextView = (TextView) context.findViewById(R.id.num_points_text);
        init();
    }

    @Override
    public void update() {

        if(resettingMainCanvas)
        {
            return;
        }

        if(IntegralView.actionDown)
        {
            handleInput();
        }
        else
        {
            controlPointPressReset = true;
        }

        sliders[0].y = getFunctionY(sliders[0].x, scalar,functionNum);
        sliders[1].y = getFunctionY(sliders[1].x, scalar, functionNum);

        sliders[0].x = sliders[0].x > IntegralView.dimensions.x ? IntegralView.dimensions.x : Math.max(sliders[0].x, 0);
        sliders[0].y = sliders[0].y > IntegralView.dimensions.y ? IntegralView.dimensions.y : Math.max(sliders[0].y, 0);
        sliders[1].x = sliders[1].x > IntegralView.dimensions.x ? IntegralView.dimensions.x : Math.max(sliders[1].x, 0);
        sliders[1].y = sliders[1].y > IntegralView.dimensions.y ? IntegralView.dimensions.y : Math.max(sliders[1].y, 0);
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        int functionWidth = IntegralView.dimensions.x;
        int rectWidth = (int) (functionWidth/numRects);

        if(animate)
        {
            animate();
        }

        generateFunctionPath();
        canvas.drawPath(path, generalPaint2);

        area = 0f;
        RectF r = new RectF(rectWidth/2f,50,100,100);

        drawIntegralRectangles(canvas, rectWidth, r);

        drawSliders(canvas, r);

        areaTextView.setText("Estimated Area: " + String.format("%.2f", area/gridScaleFactor));
        drawGrid(canvas, gridSubDivision,gridSubDivision);
        drawBorder(canvas);
    }

    private void drawSliders(Canvas canvas, RectF r) {
        generalPaint4.setColor(Color.MAGENTA);

        r.left = sliders[0].x - P_SIZE*.45f;
        r.right = sliders[0].x + P_SIZE*.45f;
        r.bottom = IntegralView.dimensions.y;
        r.top = sliders[0].y;
        canvas.drawRect(r, generalPaint4);

        r.left = sliders[1].x - P_SIZE*.45f;
        r.right = sliders[1].x + P_SIZE*.45f;
        r.bottom = IntegralView.dimensions.y;
        r.top = sliders[1].y;
        canvas.drawRect(r, generalPaint4);

        generalPaint4.setColor(Color.CYAN);

        canvas.drawCircle(sliders[0].x, sliders[0].y, SLIDER_RADIUS, generalPaint4);
        canvas.drawCircle(sliders[1].x, sliders[1].y, SLIDER_RADIUS, generalPaint4);

        generalPaint.setColor((Color.RED));
        pathPaint.setColor(Color.GREEN);

        canvas.drawCircle(sliders[0].x, sliders[0].y, SLIDER_RADIUS, circleOutlinePaint);
        canvas.drawCircle(sliders[1].x, sliders[1].y, SLIDER_RADIUS, circleOutlinePaint);
    }

    private void drawIntegralRectangles(Canvas canvas, int rectWidth, RectF r) {
        for(int i = 0; i < IntegralView.dimensions.x; i += rectWidth)
        {
            int yVal = getFunctionY((int) (i + rectWidth/2f), scalar,functionNum);

            r.left = i;
            r.right = i+rectWidth;
            r.bottom = IntegralView.dimensions.y/2;
            r.top = yVal;

            if(i+rectWidth/2 < sliders[0].x || i+rectWidth/2 > sliders[1].x)
            {
                grayPaint.setAlpha(100);
                canvas.drawRect(r, grayPaint);
                grayPaint.setStyle(Paint.Style.STROKE);
                grayPaint.setAlpha(200);
                canvas.drawRect(r, grayPaint);
                grayPaint.setStyle(Paint.Style.FILL);
            }
            else
            {
                if(yVal > IntegralView.dimensions.y/2)
                {
                    canvas.drawRect(r, negativePaint);
                    generalPaint3.setAlpha((55 + 200-200*numRects/MAX_NUM_RECTS));
                    canvas.drawRect(r, generalPaint3);
                    area += rectWidth*(IntegralView.dimensions.y/2 - yVal);
                }
                else
                {
                    canvas.drawRect(r, generalPaint);
                    generalPaint3.setAlpha((55 + 200-200*numRects/MAX_NUM_RECTS));
                    canvas.drawRect(r, generalPaint3);
                    area += rectWidth*(IntegralView.dimensions.y/2 - yVal);
                }
            }
        }
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

        for(int i = 0; i < IntegralView.dimensions.x + P_SIZE*1f; i += P_SIZE*1f)
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
        canvas.drawLine(IntegralView.dimensions.x/2,0,IntegralView.dimensions.x/2, IntegralView.dimensions.y, generalPaint5);
        canvas.drawLine(0,IntegralView.dimensions.y/2,IntegralView.dimensions.x, IntegralView.dimensions.y/2, generalPaint5);
        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 0; x < IntegralView.dimensions.x/xDim; x++)
        {

            int currentX = x*(IntegralView.dimensions.x/xDim);
            canvas.drawLine(currentX,0,currentX, IntegralView.dimensions.y, generalPaint5);
            canvas.drawText(String.valueOf(x-xDim/2), currentX,IntegralView.dimensions.y/2,gridTextPaint);
        }

        for(int y = 0; y < IntegralView.dimensions.y/yDim; y++)
        {
            int currentY = y*(IntegralView.dimensions.y/yDim);
            canvas.drawLine(0,currentY,IntegralView.dimensions.x, currentY, generalPaint5);
            canvas.drawText(String.valueOf(-1*(y-yDim/2)), IntegralView.dimensions.x/2,currentY,gridTextPaint);
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
                IntegralView.dimensions.x / 2f,
                IntegralView.dimensions.y / 2f,
                IntegralView.dimensions.x,
                IntegralView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

        negativePaint = new Paint();

        negativePaint.setStyle(Paint.Style.FILL);
        negativePaint.setStrokeCap(Paint.Cap.ROUND);
        negativePaint.setStrokeWidth(P_SIZE*.75f);
        negativePaint.setAntiAlias(true);
        negativePaint.setShader(new LinearGradient(
                IntegralView.dimensions.x / 2f,
                IntegralView.dimensions.y / 2f,
                IntegralView.dimensions.x,
                IntegralView.dimensions.y,
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
        area = 0f;
        animateDirection = 1;
        animate = false;
        animateProgress = numRects;
        path = new Path();

        sliders = new Point[2];
        sliders[0] = new Point((int) SLIDER_RADIUS*3, (int) (IntegralView.dimensions.y*.25f));
        sliders[1] = new Point((int) (IntegralView.dimensions.x-SLIDER_RADIUS*3), (int) (IntegralView.dimensions.y*.25f));

        controlPointPressReset = true;
        currentControlPointIndex = 0;

        clampedInput = new Point(0,0);
        clampedInput.x =  sliders[0].x;
        clampedInput.y =  sliders[0].y;

        functionNum = 0;

        gridSubDivision = 6;
        scalar = (float) (IntegralView.dimensions.x/gridSubDivision);

        gridScaleFactor = (IntegralView.dimensions.x*IntegralView.dimensions.x/(float)(gridSubDivision*gridSubDivision));
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

    public int getNumRects() {
        return numRects;
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
                float fun0 = (float) ((float) 3f*Math.sin((IntegralView.dimensions.x/2f-x)/ scalar));
                return (int) (IntegralView.dimensions.y*.5f + scalar/1f*fun0);

            case 1:
                float fun1 = (float) (Math.pow((IntegralView.dimensions.x/2f-x)/scalar, 3) - ((IntegralView.dimensions.x/2-x)/scalar));
                return (int) (IntegralView.dimensions.y*.5f + scalar/1f*fun1);

            case 2:
                float fun3 = .25f*(float) Math.sinh((IntegralView.dimensions.x/2f-x)/ scalar);
                return (int) (IntegralView.dimensions.y*.5f + scalar/1f*fun3);
            case 3:
                float fun4 = 3f*(float)Math.cbrt((IntegralView.dimensions.x/2f-x)/ scalar);
                return (int) (IntegralView.dimensions.y*.5f + scalar/1f*fun4);
            case 4:
                float fun5 = (float) ((float) 3f*Math.sin(Math.pow((IntegralView.dimensions.x/2f-x)/ scalar, 3)/3));
                return (int) (IntegralView.dimensions.y*.5f + scalar/1f*fun5);
        }
        return -1;
    }

    private void handleInput() {
        clampedInput.x = IntegralView.input.x > IntegralView.dimensions.x ? IntegralView.dimensions.x : Math.max(IntegralView.input.x, 0);
        clampedInput.y = IntegralView.input.y > IntegralView.dimensions.y ? IntegralView.dimensions.y : Math.max(IntegralView.input.y, 0);
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

        if(distToInput < IntegralView.dimensions.x / 35f)
        {
            sliders[minIndex].x = clampedInput.x;
            sliders[minIndex].y = clampedInput.y;
        }
        else
        {
            sliders[minIndex].x += directionToFingerVectorX*distToInput*.5f;
            sliders[minIndex].y += directionToFingerVectorY*distToInput*.5f;
        }

        if(Math.abs(sliders[0].x - sliders[1].x) < P_SIZE*4 || sliders[0].x >=sliders[1].x )
        {
            if(minIndex == 0)
            {
                sliders[1].x = (int) (sliders[0].x + P_SIZE*4);
                if(sliders[1].x > IntegralView.dimensions.x)
                {
                    sliders[1].x = IntegralView.dimensions.x;
                    sliders[0].x =  sliders[1].x - P_SIZE*4;
                }
            }
            else
            {
                sliders[0].x = (int) (sliders[1].x - P_SIZE*4);

                if(sliders[0].x < 0)
                {
                    sliders[0].x = 0;
                    sliders[1].x =   P_SIZE*4;
                }
            }
        }
    }

    public void setFunctionNum(int num)
    {
        functionNum = num;
    }
}