package com.dopaminequest.mathalgorithmsdatastructures.views.Stack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;

import com.dopaminequest.mathalgorithmsdatastructures.tools.BezierCurve;
import java.util.ArrayList;


public class MainCanvas extends Object {

    private Paint generalPaint;
    private Paint pathPaint;
    public boolean resettingMainCanvas = true;
    private  Point canvasDimensions;
    private final int yDimension = StackView.dimensions.y;
    private final int xDimension = StackView.dimensions.x;
    private int P_SIZE = StackView.dimensions.x/110;
    private boolean pause;
    private boolean animate;
    private ArrayList<Node> activeNodes;
    private ArrayList<Node> inactiveNodes;
    private ArrayList<Point> popControlPoints;
    private ArrayList<Point> pushControlPoints;
    private Path path;
    private BezierCurve bc;
    private Paint stackPaint;
    private Point topPosition;
    private Point bottomPosition;
    private Rect mRectSquare;
    private int MAX_CAPACITY = 5;
    private int blockHeight;
    private int stackHeight;
    private int blockWidth;
    private int size;
    private Paint paintText;

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

        int removeIndex = -1;
        for(int i = 0; i < inactiveNodes.size(); i++)
        {
            inactiveNodes.get(i).update();

            if(inactiveNodes.get(i).checkStatus())
            {
                removeIndex = i;
            }
        }
        if(removeIndex != -1)
        {
            inactiveNodes.remove(inactiveNodes.get(removeIndex));
        }

        for(int i = 0; i < activeNodes.size(); i++)
        {
            activeNodes.get(i).update();
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

        if(size > 0)
        {
            canvas.drawText("Top ->", activeNodes.get(size-1).getCenterPosition().x - blockWidth * .75f, activeNodes.get(size-1).getCenterPosition().y, paintText);
        }

        mRectSquare.left = StackView.dimensions.x/2 - blockWidth/2;
        mRectSquare.right = StackView.dimensions.x/2 + blockWidth/2;
        mRectSquare.top = (int) (((StackView.dimensions.y) - (StackView.dimensions.y*.75f))/2);
        mRectSquare.bottom = (int) (((StackView.dimensions.y) - (StackView.dimensions.y*.75f))/2) + stackHeight ;
        canvas.drawRect(mRectSquare, pathPaint);

        for(int i = 0; i < activeNodes.size(); i++)
        {
            activeNodes.get(i).draw(canvas);
        }

        for(int i = 0; i < inactiveNodes.size(); i++)
        {
            inactiveNodes.get(i).draw(canvas);
        }

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
        topPosition = new Point();
        bottomPosition = new Point();
        bc = new BezierCurve();
        stackHeight = (int) ((StackView.dimensions.y*.75f));
        stackPaint = new Paint();
        stackPaint.setColor(Color.GREEN);
        stackPaint.setAlpha(55);
        mRectSquare = new Rect();
        topPosition.x = (int) (StackView.dimensions.x*.5f);
        topPosition.y = (int) (stackHeight  + ((StackView.dimensions.y) - (StackView.dimensions.y*.75f))/2);
        blockHeight = stackHeight/MAX_CAPACITY;
        blockWidth = blockHeight*3;
        size = 0;
        activeNodes = new ArrayList<Node>();
        inactiveNodes = new ArrayList<Node>();

        pushControlPoints = new ArrayList<Point>();
        pushControlPoints.add(new Point(0, 0));
        pushControlPoints.add(new Point(0, 0));
        pushControlPoints.add(new Point(0, 0));
        pushControlPoints.add(new Point(0, 0));

        popControlPoints = new ArrayList<Point>();
        popControlPoints.add(new Point(0, 0));
        popControlPoints.add(new Point(0, 0));
        popControlPoints.add(new Point(0, 0));
        popControlPoints.add(new Point(0, 0));

        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(P_SIZE);
        pathPaint.setAntiAlias(true);

        paintText = new TextPaint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(P_SIZE*8);
        paintText.setAlpha(155);
        paintText.setTextAlign(Paint.Align.CENTER);



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

    public void createNode()
    {
        if(size < MAX_CAPACITY)
        {
            if(size != 0)
            {
                activeNodes.get(size-1).top = false;
            }
            Node node;
            node = new Node(blockWidth, blockHeight , (int) (0), (int) (0));
            pushControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
            pushControlPoints.set(1, new Point(node.getCenterPosition().x, (int) (StackView.dimensions.y*.5f)));
            pushControlPoints.set(2, new Point(StackView.dimensions.x/2, 0));
            pushControlPoints.set(3, new Point(topPosition.x, topPosition.y - blockHeight/2));
            node.followCurve(bc.calculatePoints(200, pushControlPoints));
            activeNodes.add(node);
            topPosition.y -= blockHeight;
            size++;
            node.top = true;
        }
    }

    public void destroyNode()
    {
        if(size > 0)
        {
            Node node = activeNodes.get(size-1);
            popControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
            popControlPoints.set(1, new Point(node.getCenterPosition().x, 0));
            popControlPoints.set(2, new Point(StackView.dimensions.x, StackView.dimensions.y/2));
            popControlPoints.set(3, new Point(StackView.dimensions.x, -StackView.dimensions.y));
            node.followCurve(bc.calculatePoints(200, popControlPoints));
            node.destroy = true;
            topPosition.y += blockHeight;
            inactiveNodes.add(node);
            activeNodes.remove(node);
            size--;

            if(size != 0)
            {
                activeNodes.get(size-1).top = true;
            }
        }
    }

    public int getSize()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return (size == 0);
    }

    public Node getTop()
    {
        if(size == 0)
        {
            throw new IllegalStateException("Stack Empty");
        }
        return activeNodes.get(size-1);
    }
}