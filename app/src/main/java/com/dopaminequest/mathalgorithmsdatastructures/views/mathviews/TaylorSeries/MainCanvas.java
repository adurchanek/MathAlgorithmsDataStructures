package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.TaylorSeries;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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
    private Paint pointAPaint;
    private Paint grayPaint;
    private Paint pathPaint;
    private final int yDimension = TaylorSeriesView.dimensions.y;
    private final int xDimension = TaylorSeriesView.dimensions.x;
    private boolean pause;
    private boolean animate;
    private int P_SIZE = TaylorSeriesView.dimensions.x/110;
    private int numRects;
    private int rectLineAlpha;
    private float SLIDER_SCALE_FACTOR = 100f;
    private int MIN_TERMS = (int) (1* SLIDER_SCALE_FACTOR);
    private int MAX_TERMS = (int) (30* SLIDER_SCALE_FACTOR);
    private float area;
    private float animateDirection;
    private SeekBar termSeekBar;
    private SeekBar pointASeekBar;
    private TextView areaTextView;
    private float ANIMATE_SPEED = 3.5f;
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
    private float gridSubDivision;
    private Paint greenLinesPaint;
    private Paint circleOutlinePaint;
    private double termNum;
    private double pointAOffset;
    private double previousPointAOffset;
    private boolean maclaurin;

    public MainCanvas(Activity context)
    {
        termSeekBar = (SeekBar) context.findViewById(R.id.term_seek_bar);
        pointASeekBar = (SeekBar) context.findViewById(R.id.point_a_seek_bar);
        areaTextView = (TextView) context.findViewById(R.id.area_text);
        init();
    }

    @Override
    public void update() {

        if(resettingMainCanvas)
        {
            return;
        }

        sliders[0].y = getFunctionY(sliders[0].x, scalar,functionNum);
        sliders[1].y = getFunctionY(sliders[1].x, scalar, functionNum);

        sliders[0].x = sliders[0].x > TaylorSeriesView.dimensions.x ? TaylorSeriesView.dimensions.x : Math.max(sliders[0].x, 0);
        sliders[0].y = sliders[0].y > TaylorSeriesView.dimensions.y ? TaylorSeriesView.dimensions.y : Math.max(sliders[0].y, 0);
        sliders[1].x = sliders[1].x > TaylorSeriesView.dimensions.x ? TaylorSeriesView.dimensions.x : Math.max(sliders[1].x, 0);
        sliders[1].y = sliders[1].y > TaylorSeriesView.dimensions.y ? TaylorSeriesView.dimensions.y : Math.max(sliders[1].y, 0);

    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        animate();
        generateFunctionPath();
        canvas.drawPath(path, generalPaint2);

        generateTaylorSeriesPath();
        generalPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, generalPaint);

        int clampedPointAValueY = getFunctionY((int) (TaylorSeriesView.dimensions.x/2 + pointAOffset), scalar, functionNum);
        clampedPointAValueY = clampedPointAValueY > TaylorSeriesView.dimensions.y ? TaylorSeriesView.dimensions.y : Math.max(clampedPointAValueY, 0);

        canvas.drawCircle((float) (TaylorSeriesView.dimensions.x/2 + pointAOffset), clampedPointAValueY, SLIDER_RADIUS/2, pointAPaint);
        pointAPaint.setColor(Color.MAGENTA);
        pointAPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) (TaylorSeriesView.dimensions.x/2 + pointAOffset), clampedPointAValueY , SLIDER_RADIUS/2, pointAPaint);
        pointAPaint.setColor(Color.CYAN);
        pointAPaint.setStyle(Paint.Style.FILL);

        drawGrid(canvas, gridSubDivision,gridSubDivision);
        drawBorder(canvas);
    }

    private void generateTaylorSeriesPath() {
        boolean firstPoint2 = true;
        path.reset();

        for(int i = 0; i < TaylorSeriesView.dimensions.x + P_SIZE*1f; i += P_SIZE*1f)
        {
            int yVal = getTaylorSeriesY(i, scalar, functionNum, termNum, pointAOffset);

            if(firstPoint2)
            {
                path.moveTo(i,yVal);
                firstPoint2 = false;
            }
            else
            {
                path.lineTo(i,yVal);
            }
        }
    }

    private void generateFunctionPath() {
        boolean firstPoint1 = true;
        path.reset();

        for(int i = 0; i < TaylorSeriesView.dimensions.x + P_SIZE*1f; i += P_SIZE*1f)
        {
            int yVal = getFunctionY(i, scalar,functionNum);

            if(firstPoint1)
            {
                path.moveTo(i,yVal);
                firstPoint1 = false;
            }
            else
            {
                path.lineTo(i,yVal);
            }
        }
    }

    private void animate() {
        if(animate)
        {
            animateProgress +=  (animateDirection * ANIMATE_SPEED);

            if(animateProgress > MAX_TERMS)
            {
                animateProgress  = MAX_TERMS;
                animateDirection *=-1;
            }
            else if(animateProgress < MIN_TERMS)
            {
                animateProgress  = MIN_TERMS;
                animateDirection *=-1;
            }
            termSeekBar.setProgress((int)animateProgress - MIN_TERMS);
        }
    }

    private void drawGrid(Canvas canvas, float xDim, float yDim) {

        generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
        canvas.drawLine(TaylorSeriesView.dimensions.x/2,0,TaylorSeriesView.dimensions.x/2, TaylorSeriesView.dimensions.y, generalPaint5);
        canvas.drawLine(0,TaylorSeriesView.dimensions.y/2, TaylorSeriesView.dimensions.x, TaylorSeriesView.dimensions.y/2, generalPaint5);
        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 0; x < xDim; x+=1)
        {
            int currentX = (int)  (x*TaylorSeriesView.dimensions.x/xDim);

            //2*number of divisions
            if(xDim > 2*5 )
            {
                if(x%5 == 0)
                {
                    canvas.drawText(String.valueOf((x)), TaylorSeriesView.dimensions.x/2 + currentX,TaylorSeriesView.dimensions.y/2,gridTextPaint);
                    canvas.drawText(String.valueOf((-x)), TaylorSeriesView.dimensions.x/2 - currentX,TaylorSeriesView.dimensions.y/2,gridTextPaint);
                    generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
                    canvas.drawLine(TaylorSeriesView.dimensions.x/2 + currentX,0,TaylorSeriesView.dimensions.x/2 + currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                    canvas.drawLine(TaylorSeriesView.dimensions.x/2 - currentX,0,TaylorSeriesView.dimensions.x/2 - currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                    generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);
                }
                else
                {
                    canvas.drawLine(TaylorSeriesView.dimensions.x/2 + currentX,0,TaylorSeriesView.dimensions.x/2 + currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                    canvas.drawLine(TaylorSeriesView.dimensions.x/2 - currentX,0,TaylorSeriesView.dimensions.x/2 - currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                }
            }
            else
            {
                canvas.drawLine(TaylorSeriesView.dimensions.x/2 + currentX,0,TaylorSeriesView.dimensions.x/2 + currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                canvas.drawLine(TaylorSeriesView.dimensions.x/2 - currentX,0,TaylorSeriesView.dimensions.x/2 - currentX, TaylorSeriesView.dimensions.y, generalPaint5);
                canvas.drawText(String.valueOf((x)), TaylorSeriesView.dimensions.x/2 + currentX,TaylorSeriesView.dimensions.y/2,gridTextPaint);
                canvas.drawText(String.valueOf((-x)), TaylorSeriesView.dimensions.x/2 - currentX,TaylorSeriesView.dimensions.y/2,gridTextPaint);
            }
        }

        for(int y = 0; y < yDim; y+=1)
        {
            int currentY = (int)  (y*TaylorSeriesView.dimensions.y/yDim);

            //2*number of divisions
            if(yDim > 2*5)
            {
                if(y%5 == 0)
                {
                    canvas.drawText(String.valueOf((y)), TaylorSeriesView.dimensions.x/2,TaylorSeriesView.dimensions.y/2 + currentY,gridTextPaint);
                    canvas.drawText(String.valueOf((-y)), TaylorSeriesView.dimensions.x/2,TaylorSeriesView.dimensions.y/2 - currentY,gridTextPaint);
                    generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
                    canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 + currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 + currentY, generalPaint5);
                    canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 - currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 - currentY, generalPaint5);
                    generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);
                }
                else
                {
                    canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 + currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 + currentY, generalPaint5);
                    canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 - currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 - currentY, generalPaint5);
                }
            }
            else
            {
                canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 + currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 + currentY, generalPaint5);
                canvas.drawLine(0,TaylorSeriesView.dimensions.y/2 - currentY, TaylorSeriesView.dimensions.x,TaylorSeriesView.dimensions.y/2 - currentY, generalPaint5);
                canvas.drawText(String.valueOf((y)), TaylorSeriesView.dimensions.x/2,TaylorSeriesView.dimensions.y/2 + currentY,gridTextPaint);
                canvas.drawText(String.valueOf((-y)), TaylorSeriesView.dimensions.x/2,TaylorSeriesView.dimensions.y/2 - currentY,gridTextPaint);
            }
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
                TaylorSeriesView.dimensions.x / 2f,
                TaylorSeriesView.dimensions.y / 2f,
                TaylorSeriesView.dimensions.x,
                TaylorSeriesView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));

        negativePaint = new Paint();

        negativePaint.setStyle(Paint.Style.FILL);
        negativePaint.setStrokeCap(Paint.Cap.ROUND);
        negativePaint.setStrokeWidth(P_SIZE*.75f);
        negativePaint.setAntiAlias(true);
        negativePaint.setShader(new LinearGradient(
                TaylorSeriesView.dimensions.x / 2f,
                TaylorSeriesView.dimensions.y / 2f,
                TaylorSeriesView.dimensions.x,
                TaylorSeriesView.dimensions.y,
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

        pointAPaint = new Paint();
        pointAPaint.setStyle(Paint.Style.FILL);
        pointAPaint.setAntiAlias(true);
        pointAPaint.setStrokeWidth(P_SIZE*.5f);
        pointAPaint.setColor(Color.CYAN);

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

        numRects = MIN_TERMS;
        termSeekBar.setMax(MAX_TERMS - MIN_TERMS);
        termSeekBar.setProgress(0);

        rectLineAlpha = 0;
        area = 0f;
        animateDirection = 1;
        animate = false;
        animateProgress = MIN_TERMS;
        path = new Path();

        sliders = new Point[2];
        sliders[0] = new Point((int) SLIDER_RADIUS*3, (int) (TaylorSeriesView.dimensions.y*.25f));
        sliders[1] = new Point((int) (TaylorSeriesView.dimensions.x-SLIDER_RADIUS*3), (int) (TaylorSeriesView.dimensions.y*.25f));

        controlPointPressReset = true;
        currentControlPointIndex = 0;

        clampedInput = new Point(0,0);
        clampedInput.x =  sliders[0].x;
        clampedInput.y =  sliders[0].y;

        functionNum = 0;

        gridSubDivision = 6f;
        scalar = (float) (TaylorSeriesView.dimensions.x/gridSubDivision);

        termNum = MIN_TERMS/SLIDER_SCALE_FACTOR;

        pointASeekBar.setMax((int) TaylorSeriesView.dimensions.x);
        pointASeekBar.setProgress((int)(TaylorSeriesView.dimensions.x/2f));

        pointAOffset = 0f;
        maclaurin = false;
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

    public void setTermPosition(double progress) {
        progress = progress/SLIDER_SCALE_FACTOR;
        if(!animate)
        {
            animateProgress = (float) progress* SLIDER_SCALE_FACTOR + MIN_TERMS;
        }

        gridSubDivision = 6f + (float)progress;
        scalar = (float) (TaylorSeriesView.dimensions.x/gridSubDivision);
        termNum = progress + MIN_TERMS/SLIDER_SCALE_FACTOR;
    }

    public void setPointAPosition(double progress) {
        pointAOffset = (double) (progress- TaylorSeriesView.dimensions.x/2);
    }

    public int getNumRects() {
        return numRects;
    }

    public double getPointAOffset() {

        if(maclaurin)
        {
            return 0f;
        }

        return pointAOffset/(TaylorSeriesView.dimensions.x/2f)*gridSubDivision/2f;
    }

    public int getMaxNumRects() {
        return MAX_TERMS;
    }

    public void setTermSeekBar(SeekBar termSeekBar) {
    }

    public int getFunctionY(int x, float scalar, int functionNum)
    {
        switch (functionNum)
        {
            case 0:
                float fun0 = (float) (-Math.cos((TaylorSeriesView.dimensions.x/2f-x)/ scalar));
                return (int) (TaylorSeriesView.dimensions.y*.5f + fun0*scalar);
            case 1:
                float fun1 = (float) (Math.sin((TaylorSeriesView.dimensions.x/2f-x)/ scalar));
                return (int) (TaylorSeriesView.dimensions.y*.5f + fun1*scalar);
            case 2:
                float fun2 = (float) -Math.pow(Math.E,-(TaylorSeriesView.dimensions.x/2f-x)/ scalar);
                return (int) (TaylorSeriesView.dimensions.y*.5f + fun2*scalar);
        }
        return -1;
    }

    // cos and sin swapped because of -y canvas y-axis
    public double trigDerivative(String s, double a, int degree)
    {
        switch (s) {
            case "cos":
                switch ((degree-1)%4 + 1) {
                    case (1):
                        return -Math.sin(a);
                    case (2):
                        return Math.cos(a);
                    case (3):
                        return Math.sin(a);
                    case (4):
                        return -Math.cos(a);
                }
            case "sin":
                switch ((degree-1)%4 + 1) {
                    case (1):
                        return Math.cos(a);
                    case (2):
                        return Math.sin(a);
                    case (3):
                        return -Math.cos(a);
                    case (4):
                        return -Math.sin(a);
                }
        }
        return 0;
    }

    public int getTaylorSeriesY(int x, double scalar, int functionNum, double n, double pointA)
    {
        double translatedX = (TaylorSeriesView.dimensions.x/2f-x + pointA)/ scalar;
        switch (functionNum)
        {
            case 0:
                double sum0 = -1*Math.cos(pointA/scalar);

                for(int i = 1; i < (int)n; i+=1)
                {
                    sum0 += trigDerivative("cos",pointA/scalar, i)*Math.pow(translatedX, i)/factorial(i);
                }

                double perc0 = (n - (int)n);
                sum0 += perc0*trigDerivative("cos",pointA/scalar, (int)n)*Math.pow(translatedX, (int)n)/factorial((int)n);

                if((TaylorSeriesView.dimensions.y*.5f + sum0*scalar) > TaylorSeriesView.dimensions.y)
                {
                    return (int) TaylorSeriesView.dimensions.y+1;
                }
                else if((TaylorSeriesView.dimensions.y*.5f + sum0*scalar) < 0)
                {
                    return -1;
                }
                return (int) (TaylorSeriesView.dimensions.y*.5f + sum0*scalar);

            case 1:
                double sum1 = -1*Math.sin(pointA/scalar);

                for(int i = 1; i < (int)n; i+=1)
                {
                    sum1 += trigDerivative("sin",pointA/scalar, i)*Math.pow(translatedX, i)/factorial(i);
                }

                double perc1 = (n - (int)n);
                sum1 += perc1*trigDerivative("sin",pointA/scalar, (int)n)*Math.pow(translatedX, (int)n)/factorial((int)n);

                if((TaylorSeriesView.dimensions.y*.5f + sum1*scalar) > TaylorSeriesView.dimensions.y)
                {
                    return (int) TaylorSeriesView.dimensions.y+1;
                }
                else if((TaylorSeriesView.dimensions.y*.5f + sum1*scalar) < 0)
                {
                    return -1;
                }
                return (int) (TaylorSeriesView.dimensions.y*.5f + sum1*scalar);

            case 2:
                double sum2 =  -Math.pow(Math.E,pointA/scalar);

                for(int i = 1; i < (int)n; i+=1)
                {
                    sum2 += -Math.pow(Math.E,pointA/scalar)*Math.pow(-translatedX, i)/factorial(i);
                }

                double perc2 = (n - (int)n);
                sum2 += perc2*-Math.pow(Math.E,pointA/scalar)*Math.pow(-translatedX, (int)n)/factorial((int)n);

                if((TaylorSeriesView.dimensions.y*.5f + sum2*scalar) > TaylorSeriesView.dimensions.y)
                {
                    return (int) TaylorSeriesView.dimensions.y+1;
                }
                else if((TaylorSeriesView.dimensions.y*.5f + sum2*scalar) < 0)
                {
                    return -1;
                }
                return (int) (TaylorSeriesView.dimensions.y*.5f + sum2*scalar);
        }
        return -1;
    }

    public void setFunctionNum(int num)
    {
        functionNum = num;
    }

    public double factorial(long n)
    {
        if(n == 0)
        {
            return 1f;
        }
        return factorial(n-1)*n;
    }

    public double getCurrentTermNum() {
        return termNum;
    }

    public void toggleMaclaurinSeries() {
        maclaurin = !maclaurin;
        if(maclaurin)
        {
            pointASeekBar.setEnabled(false);
            setPointAPosition(TaylorSeriesView.dimensions.x/2f);
        }
        else
        {
            pointASeekBar.setEnabled(true);
            setPointAPosition(pointASeekBar.getProgress());

        }
    }
}