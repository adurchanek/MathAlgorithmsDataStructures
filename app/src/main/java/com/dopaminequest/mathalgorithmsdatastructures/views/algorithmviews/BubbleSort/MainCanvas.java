package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.BubbleSort;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.text.TextPaint;
import android.widget.SeekBar;

import com.dopaminequest.mathalgorithmsdatastructures.R;
import com.dopaminequest.mathalgorithmsdatastructures.tools.BezierCurve;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class MainCanvas extends Object {

    private SeekBar seekBarSortSpeed;
    private SeekBar seekBarNumNodes;
    private Paint generalPaint;


    float maxBlockHeight;
    float minBlockHeight;
    boolean nodesInitialized;

    private Paint pathPaint;
    public boolean resettingMainCanvas = true;
    private  Point canvasDimensions;
    private final int yDimension = BubbleSortView.dimensions.y;
    private final int xDimension = BubbleSortView.dimensions.x;
    private int P_SIZE = BubbleSortView.dimensions.x/110;
    private boolean pause;
    private boolean animate;

    private float SLIDER_SCALE_FACTOR = 100f;
    private int MIN_SPEED = (int) ( SLIDER_SCALE_FACTOR*.4);
    private int MAX_SPEED = (int) (30* SLIDER_SCALE_FACTOR);

    private int MIN_NODES = 10;
    private int MAX_NODES = 40;

    private ArrayList<Point> curveControlPoints;
    private Path path;
    private BezierCurve bc;
    private Paint stackPaint;

    private int blockWidth;
    private int size;
    private Paint paintText;
    private int textAlpha;
    private Node[] nodes;
    private Node[] nodesCopyPreSort;
    private int numNodes;

    private int NUM_BEZIER_POINTS = 60;
    private boolean autoSort;

    private int iSort;
    private int jSort;

    public int numberAnimating;
    private boolean nextSort;
    int animationIndex;

    float animationSpeedCounter;
    int animationSpeed;

    private float animationSpeedIncrement;
    Node currentlyAnimatingNode;

    private Point[] swapCurvePoints;
    Node swapCurveStartNode;
    Node swapCurveEndNode;

    HashMap<Integer, Integer> nodeConverter;
    ArrayList<MoveInfo> animationSequence;

    private class MoveInfo
    {
        public boolean moveNodeToPosition;

        public int indexVal;
        public Point endPoint;
        public int direction;
        public int numberNodesToAnimate;
        public boolean currentlySelectedNode;
        public boolean isSorted;
        public boolean isComplete;

        public MoveInfo(int iVal, Point end, int dir, int nNodesToAnimate, boolean cSelectedNode, boolean moveNode, boolean sorted, boolean complete)
        {

           endPoint = end;
           direction = dir;
           indexVal = iVal;
           numberNodesToAnimate  = nNodesToAnimate;
           currentlySelectedNode = cSelectedNode;
           moveNodeToPosition = moveNode;
           isSorted = sorted;
           isComplete = complete;
        }
    }


    public MainCanvas(Activity context)
    {
        seekBarSortSpeed = (SeekBar) context.findViewById(R.id.seekBarSortSpeed);
        seekBarNumNodes = (SeekBar) context.findViewById(R.id.seekBarNumNodes);
        init();
    }

    @Override
    public void update() {

        if(resettingMainCanvas)
        {
            return;
        }

        if(!nodesInitialized)
        {
            return;
        }

        if(numberAnimating <= 0 || getProgressBarPercentage() == 0f || nextSort)
        {
            if (autoSort || nextSort)
            {
                if (animationIndex < animationSequence.size())
                {
                    if(getProgressBarPercentage() == 0f)
                    {
                        animationSpeedCounter += animationSpeedIncrement*.75f;
                    }

                    if (animationSpeedCounter >= animationSpeed || nextSort)
                    {
                        processNextMoveSequence();
                    }
                    else
                    {
                        animationSpeedCounter += animationSpeedIncrement;
                    }
                }
                else
                {
                    autoSort = false;
                }

                if(nextSort)
                {
                    nextSort = false;
                }
            }
        }

        for(int i = 0; i < nodes.length; i++)
        {
            nodes[i].update();
        }
    }

    private void processNextMoveSequence() {
        MoveInfo moveInfo = animationSequence.get(animationIndex);
        int numberNodesToAnimate = moveInfo.numberNodesToAnimate;

        for (int nodeIndex = 0; nodeIndex < numberNodesToAnimate; nodeIndex++)
        {
            int val = moveInfo.indexVal;
            moveInfo = animationSequence.get(animationIndex + nodeIndex);
            processMoveInfo(moveInfo);
            if(val == moveInfo.indexVal && numberNodesToAnimate > 1 && animationIndex < animationSequence.size()-1)
            {
                numberNodesToAnimate++;
            }
        }
        animationIndex += numberNodesToAnimate;
        animationSpeedCounter = 0f;
    }

    private void processMoveInfo(MoveInfo moveInfo) {

        Node currentNode = nodes[nodeConverter.get(moveInfo.indexVal)];

        if(moveInfo.isComplete)
        {
            currentNode.isComplete = true;
        }
        else if (moveInfo.isSorted)
        {
            currentNode.isSorted = true;
        }

        if (moveInfo.moveNodeToPosition) {
            moveNodeToPosition(currentNode, moveInfo.endPoint, moveInfo.direction);
        }

        if (moveInfo.currentlySelectedNode)
        {
            if (currentlyAnimatingNode == null)
            {
                currentlyAnimatingNode = currentNode;
                currentlyAnimatingNode.setCurrentNodeSelected(true);

            }
            else
            {
                currentlyAnimatingNode.setCurrentNodeSelected(false);
                currentlyAnimatingNode = currentNode;
                currentlyAnimatingNode.setCurrentNodeSelected(true);
            }
        }
    }

    private void sort() {

        iSort = 0;

        while(iSort < numNodes)
        {
            jSort = 1;
            while(jSort < numNodes -iSort)
            {
                boolean isSorted = false;
                if(jSort >= numNodes -iSort-1)
                {
                    isSorted = true;
                }
                animationSequence.add(new MoveInfo(nodes[jSort-1].val,  new Point(convertIndexToPosition(jSort)), 1,1, true, false, false, false));
                if(nodes[jSort-1].val > nodes[jSort].val)
                {

                    if(jSort >= numNodes -iSort-1)
                    {
                        isSorted = true;
                    }
                    animationSequence.add(new MoveInfo(nodes[jSort - 1].val,  new Point(convertIndexToPosition(jSort)), 1,2, true, true,isSorted, false));
                    animationSequence.add(new MoveInfo(nodes[jSort].val,  new Point(convertIndexToPosition(jSort-1)), -1,1,false, true,false, false));

                    Node temp = nodes[jSort - 1];
                    nodes[jSort-1] = nodes[jSort];
                    nodes[jSort] = temp;
                }
                else
                {
                    if(jSort >= numNodes -iSort-1)
                    {
                        isSorted = true;
                        animationSequence.add(new MoveInfo(nodes[jSort].val,  new Point(convertIndexToPosition(jSort)), 1,1, true, false, isSorted, false));
                    }
                }
                jSort++;
            }
            iSort++;
        }

        for(int i = 0; i < numNodes; i++)
        {
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,1, true, false, false, true));
        }
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingMainCanvas)
        {
            return;
        }

        for(int i = 0; i < nodes.length; i++)
        {
            nodes[i].draw(canvas);
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

        nodesInitialized = false;
        seekBarNumNodes.setEnabled(true);
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        position = new Point();
        scale = new Point();
        pause = false;
        animate = true;

        path = new Path();
        numNodes = 30;

        bc = new BezierCurve();
        stackPaint = new Paint();
        stackPaint.setColor(Color.GREEN);
        stackPaint.setAlpha(55);

        maxBlockHeight = BubbleSortView.dimensions.y*.5f;
        minBlockHeight = BubbleSortView.dimensions.y*.1f;
        size = 0;

        curveControlPoints = new ArrayList<Point>();
        curveControlPoints.add(new Point(0, 0));
        curveControlPoints.add(new Point(0, 0));
        curveControlPoints.add(new Point(0, 0));
        curveControlPoints.add(new Point(0, 0));


        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(P_SIZE/2f);
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(Color.CYAN);

        paintText = new TextPaint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(P_SIZE*8);
        textAlpha = 0;
        paintText.setAlpha(textAlpha);
        paintText.setTextAlign(Paint.Align.CENTER);

        generalPaint.setStyle(Paint.Style.FILL);
        generalPaint.setStrokeCap(Paint.Cap.ROUND);
        generalPaint.setStrokeWidth(P_SIZE*.75f);
        generalPaint.setAntiAlias(true);
        generalPaint.setShader(new LinearGradient(
                BubbleSortView.dimensions.x / 2f,
                BubbleSortView.dimensions.y / 2f,
                BubbleSortView.dimensions.x,
                BubbleSortView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,240),
                Shader.TileMode.MIRROR));


        numberAnimating = 0;

        seekBarSortSpeed.setMax(MAX_SPEED);
        seekBarSortSpeed.setProgress((int) (MAX_SPEED*.75f));

        seekBarNumNodes.setMax(MAX_NODES-MIN_NODES);
        seekBarNumNodes.setProgress(numNodes-MIN_NODES);

        animationIndex = 0;

        animationSpeedCounter = 1;
        animationSpeed = (int) (MAX_SPEED/SLIDER_SCALE_FACTOR);
        animationSpeedIncrement = MAX_SPEED;

        currentlyAnimatingNode = null;
        autoSort = false;
        nextSort = false;

        initNodes();

        resettingMainCanvas = false;
    }

    public void initNodes() {
        animationSequence = new ArrayList<MoveInfo>();
        blockWidth = BubbleSortView.dimensions.x/ numNodes;
        nodes = new Node[numNodes];
        nodesCopyPreSort = new Node[numNodes];
        createSortedList();
        sort();
        nodes = nodesCopyPreSort;

        nodeConverter = new HashMap<Integer, Integer>();

        for(int i = 0; i < numNodes; i++)
        {
            nodeConverter.put(nodes[i].val, i);
        }
    }

    public void createSortedList()
    {
        float blockHeight =  (maxBlockHeight - minBlockHeight)/(float) numNodes;

        for(int i = 0; i < numNodes; i++)
        {
            Point p = convertIndexToPosition(i);
            nodes[i] = new Node(blockWidth, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, i+1, this);
            nodesCopyPreSort[i] = new Node(blockWidth, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, i+1, this);
        }

        shuffleArray();

        for(int i = 0; i < numNodes; i++)
        {
            nodesCopyPreSort[i] = new Node(nodes[i].scale.x, nodes[i].scale.y, nodes[i].getCenterPosition().x, nodes[i].getCenterPosition().y, nodes[i].val, this);
        }
    }

    private void shuffleArray() {

        Random rand = new Random();

        for(int i = 0; i < numNodes; i++)
        {
            int randomIndex = rand.nextInt(numNodes);
            Node temp = nodes[i];
            Node randomTemp = nodes[randomIndex];
            nodes[i] = randomTemp;
            nodes[randomIndex] = temp;
            Point iTempPosition = convertIndexToPosition(i);
            Point randomPosition = convertIndexToPosition(randomIndex);

            nodes[i].setCenterPosition(iTempPosition.x, iTempPosition.y);
            nodes[randomIndex].setCenterPosition(randomPosition.x, randomPosition.y);

            nodes[i].setCenterDestinationPosition(iTempPosition.x, iTempPosition.y);
            nodes[randomIndex].setCenterDestinationPosition(randomPosition.x, randomPosition.y);
        }
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

    public int getSize()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return (size == 0);
    }

    public void autoSort()
    {
        autoSort = true;
        nextSort = false;
        nodesInitialized = true;
        if(seekBarNumNodes.isEnabled())
        {
            seekBarNumNodes.setEnabled(false);
        }
    }

    public void nextSort()
    {
        nextSort = true;
        autoSort = false;
        nodesInitialized = true;

        if(seekBarNumNodes.isEnabled())
        {
            seekBarNumNodes.setEnabled(false);
        }
    }

    public void moveNode(Node node, Point start,  Point end, int direction)
    {
        if(!node.isAnimating())
        {
            numberAnimating++;
        }

        curveControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
        curveControlPoints.set(1, new Point(end.x + (start.x - end.x)/2, (int) (BubbleSortView.dimensions.y*.5f + direction*(BubbleSortView.dimensions.y*.35f + 3*((float)P_SIZE/ node.getScale().y) *BubbleSortView.dimensions.y*.5f))));
        curveControlPoints.set(2, new Point(end.x + (start.x - end.x)/2, (int) (BubbleSortView.dimensions.y*.5f + direction*(BubbleSortView.dimensions.y*.35f + 3*((float)P_SIZE/ node.getScale().y) *BubbleSortView.dimensions.y*.5f))));
        curveControlPoints.set(3, new Point(end.x, end.y));

        node.followCurve(bc.calculatePoints(NUM_BEZIER_POINTS, curveControlPoints));
        node.setCenterDestinationPosition(end.x, end.y);
    }

    public void moveNodeToPosition(Node node, Point endPoint, int direction)
    {
        Point startPoint = new Point(node.getCenterDestinationPosition());
        moveNode(node, startPoint, endPoint, direction);
    }

    public Point convertIndexToPosition(int index)
    {
        Point p = new Point();
        p.y = BubbleSortView.dimensions.y/2;
        p.x = blockWidth/2 + index*blockWidth;

        return p;
    }

    public void printList()
    {
        for(int i = 0; i < nodes.length; i++)
        {

            System.out.println("i: " + i + " val "+ nodes[i].val);
        }
    }

    public float getAnimationSpeedIncrement()
    {
        return animationSpeedIncrement;
    }

    public float getProgressBarPercentage()
    {
        return seekBarSortSpeed.getProgress()/(float)MAX_SPEED;
    }

    private void generateBezierPath() {

        if(swapCurveStartNode== null || swapCurveEndNode == null)
        {
            return;
        }

        Point start = new Point(swapCurveStartNode.getCenterPosition().x,swapCurveStartNode.getCenterPosition().y - swapCurveStartNode.getScale().y/2);
        Point end = new Point(swapCurveEndNode.getCenterPosition().x,swapCurveEndNode.getCenterPosition().y - swapCurveEndNode.getScale().y/2);

        path.reset();
        boolean firstPoint = true;

        curveControlPoints.set(0, new Point(start.x, start.y));
        curveControlPoints.set(1, new Point(end.x, (int) swapCurveStartNode.getCenterPosition().y - swapCurveStartNode.getScale().y/2));
        curveControlPoints.set(2, new Point(start.x, (int) swapCurveEndNode.getCenterPosition().y - swapCurveEndNode.getScale().y/2));
        curveControlPoints.set(3, new Point(end.x, end.y));

        swapCurvePoints = bc.calculatePoints(NUM_BEZIER_POINTS, curveControlPoints);

        for(int index = 0; index < NUM_BEZIER_POINTS; index++)
        {
            if(firstPoint)
            {
                path.moveTo(swapCurvePoints[index].x,swapCurvePoints[index].y);
                firstPoint = false;
            }
            else
            {
                path.lineTo(swapCurvePoints[index].x,swapCurvePoints[index].y);
            }
        }

        path.lineTo(curveControlPoints.get(curveControlPoints.size()-1).x,curveControlPoints.get(curveControlPoints.size()-1).y);
    }

    public void setSortSpeed(int progress) {

        if(progress >= MAX_SPEED-MIN_SPEED)
        {
            progress = MAX_SPEED-MIN_SPEED;
        }
        animationSpeedIncrement = MAX_SPEED/SLIDER_SCALE_FACTOR - progress/SLIDER_SCALE_FACTOR;
    }

    public void setNumNodes(int progress) {

        numNodes = MIN_NODES + progress;
        initNodes();
    }

    public int getNumNodes() {
        return numNodes;
    }
    public int getSortSpeed() {
        return (int) ((int) 100f*(1f-(getProgressBarPercentage())));
    }
}