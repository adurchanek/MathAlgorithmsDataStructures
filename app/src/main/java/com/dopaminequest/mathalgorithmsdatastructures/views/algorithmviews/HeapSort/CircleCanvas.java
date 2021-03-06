package com.dopaminequest.mathalgorithmsdatastructures.views.algorithmviews.HeapSort;

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

public class CircleCanvas extends Object {

    private SeekBar seekBarSortSpeed;
    private SeekBar seekBarNumNodes;
    private Paint generalPaint;
    private Paint nodesLinePaint;

    public float maxBlockHeight;
    public float minBlockHeight;
    boolean nodesInitialized;

    private Paint pathPaint;
    public boolean resettingMainCanvas = true;
    private  Point canvasDimensions;
    private final int yDimension = HeapSortView.dimensions.y;
    private final int xDimension = HeapSortView.dimensions.x;
    private int P_SIZE = HeapSortView.dimensions.x/110;
    private boolean pause;
    private boolean animate;

    private float SLIDER_SCALE_FACTOR = 100f;
    private int MIN_SPEED = (int) ( SLIDER_SCALE_FACTOR*.4);
    private int MAX_SPEED = (int) (30* SLIDER_SCALE_FACTOR);

    private int MIN_NODES = 7;
    private int MAX_NODES = 63;

    private ArrayList<Point> curveControlPoints;
    private Path path;
    private BezierCurve bc;
    private Paint stackPaint;

    private int size;
    private Paint paintText;
    private int textAlpha;
    private NodeCircle[] nodes;
    private NodeCircle[] nodesCopyPreSort;


    private int NUM_BEZIER_POINTS = 60;
    private boolean autoSort;

    private int iSort;
    private int jSort;

    public int numberAnimating;
    private boolean nextSort;
    private int animationIndex;

    private float animationSpeedCounter;
    private int animationSpeed;

    private float animationSpeedIncrement;
    private NodeCircle currentlyAnimatingNode;

    private HashMap<Integer, Integer> nodeConverter;
    private ArrayList<MoveInfo> animationSequence;

    private float circleDiameter;
    private int numNodes;
    private Paint generalPaint5;
    private int maxLevel;
    private int bottomRowNumNodes;
    private float levelHeight;
    private float levelHeightMultiplier;
    private float yBottomEndValue;
    private float yRootStartValue;
    private float MAX_NODE_DIAMETER =  P_SIZE*20;
    private float normalizedCircleDiameter;
    private float totalHeight;


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
        public boolean isMax;
        public String leftOrRight;

        public MoveInfo(int iVal, Point end, int dir, int nNodesToAnimate, boolean cSelectedNode, boolean moveNode, boolean sorted, boolean complete, boolean max, String leftRight)
        {
            endPoint = end;
            direction = dir;
            indexVal = iVal;
            numberNodesToAnimate  = nNodesToAnimate;
            currentlySelectedNode = cSelectedNode;
            moveNodeToPosition = moveNode;
            isSorted = sorted;
            isComplete = complete;
            isMax = max;
            leftOrRight = leftRight;
        }
    }

    public CircleCanvas(Activity context)
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
        //System.out.println("numnodes: " + numNodes +" getBottomRowNumNodes(): " + calculateBottomRowNumNodes());

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

    private void processNextMoveSequence()
    {
        MoveInfo moveInfo = animationSequence.get(animationIndex);
        int numberNodesToAnimate = moveInfo.numberNodesToAnimate;

        for (int nodeIndex = 0; nodeIndex < numberNodesToAnimate; nodeIndex++)
        {
            moveInfo = animationSequence.get(animationIndex + nodeIndex);
            if(moveInfo.leftOrRight.equals("N/A"))
            {
                processMoveInfo(moveInfo);
            }
            else
            {
                if(moveInfo.leftOrRight.equals("R"))
                {
                    NodeCircle.rightNode = nodes[nodeConverter.get(moveInfo.indexVal)];
                }
                else if(moveInfo.leftOrRight.equals("Right NULL"))
                {
                    NodeCircle.rightNode = null;
                }
                if(moveInfo.leftOrRight.equals("L"))
                {
                    NodeCircle.leftNode = nodes[nodeConverter.get(moveInfo.indexVal)];
                }
                else if(moveInfo.leftOrRight.equals("Left NULL"))
                {
                    NodeCircle.leftNode = null;
                }

            }
        }
        animationIndex += numberNodesToAnimate;
        animationSpeedCounter = 0f;
    }

    private void processMoveInfo(MoveInfo moveInfo)
    {
        NodeCircle currentNode = nodes[nodeConverter.get(moveInfo.indexVal)];

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
        if(moveInfo.isMax)
        {
            NodeCircle.maxNode = currentNode;
        }
    }

    private void heapify(int n, int i)
    {
        animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,4, true, false, false, false, false, "N/A"));
        int largest = i;
        animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, true, "N/A"));

        int l = 2*i + 1;
        int r = 2*i + 2;

        if(l < n)
        {
            animationSequence.add(new MoveInfo(nodes[l].val,  new Point(convertIndexToPosition(i)), 1,2, false, false, false, false, false, "L"));

        }
        else
        {
            animationSequence.add(new MoveInfo(nodes[0].val,  new Point(convertIndexToPosition(i)), 1,2, false, false, false, false, false, "Left NULL"));
        }

        if(r < n)
        {
            animationSequence.add(new MoveInfo(nodes[r].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, false, "R"));
        }
        else
        {
            animationSequence.add(new MoveInfo(nodes[0].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, false,"Right NULL"));
        }

        if(l < n)
        {
            if(nodes[l].val > nodes[largest].val)
            {
                largest = l;
                animationSequence.add(new MoveInfo(nodes[l].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, true, "N/A"));

            }
        }

        if(r < n)
        {
            if(nodes[r].val > nodes[largest].val)
            {
                largest = r;
                animationSequence.add(new MoveInfo(nodes[r].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, true,"N/A"));
            }
        }

        if(largest != i)
        {
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(largest)), 1,6, true, true, false, false, true,"N/A"));
            animationSequence.add(new MoveInfo(nodes[largest].val,  new Point(convertIndexToPosition(i)), -1,1,false, true,false, false, false,"N/A"));

            NodeCircle temp = nodes[i];
            nodes[i] = nodes[largest];
            nodes[largest] = temp;

            heapify(n, largest);
        }
    }

    private void sort()
    {
        for(int i = numNodes/2 - 1; i >= 0; i--)
        {
            heapify(numNodes, i);
        }

        for(int i = numNodes-1; i > 0; i--)
        {
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,3, false, false, false, false, false,"Left NULL"));
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,1, false, false, false, false, false,"Right NULL"));
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,1, true, false, false, false, true,"N/A"));


            animationSequence.add(new MoveInfo(nodes[0].val,  new Point(convertIndexToPosition(i)), 1,6, true, true, true, false, true,"N/A"));
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(0)), -1,1,false, true,false, false, false,"N/A"));

            NodeCircle temp = nodes[0];
            nodes[0] = nodes[i];
            nodes[i] = temp;

            heapify(i,0);
        }

        for(int i = 0; i < numNodes; i++)
        {
            animationSequence.add(new MoveInfo(nodes[i].val,  new Point(convertIndexToPosition(i)), 1,1, true, false, false, true, false,"N/A"));
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        if(resettingMainCanvas)
        {
            return;
        }
        for(int i = 0; i < nodes.length; i++)
        {
            int l = 2*i + 1;
            int r = 2*i + 2;

            if(l < numNodes)
            {
                canvas.drawLine(convertIndexToPosition(i).x, convertIndexToPosition(i).y, convertIndexToPosition(l).x,convertIndexToPosition(l).y , nodesLinePaint);
            }

            if(r < numNodes)
            {
                canvas.drawLine(convertIndexToPosition(i).x, convertIndexToPosition(i).y, convertIndexToPosition(r).x,convertIndexToPosition(r).y , nodesLinePaint);
            }
        }

        drawNodes(canvas);

        drawBorder(canvas);
    }

    public void drawNodes(Canvas canvas)
    {
        for(int i = 0; i < nodes.length; i++)
        {
            nodes[i].draw(canvas);
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
        nodesInitialized = false;
        seekBarNumNodes.setEnabled(true);
        this.canvasDimensions = new Point();
        generalPaint = new Paint();
        position = new Point();
        scale = new Point();
        pause = false;
        animate = true;

        path = new Path();

        bc = new BezierCurve();
        stackPaint = new Paint();
        stackPaint.setColor(Color.GREEN);
        stackPaint.setAlpha(55);

        maxBlockHeight = HeapSortView.dimensions.y*.5f;
        minBlockHeight = HeapSortView.dimensions.y*.1f;
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
                HeapSortView.dimensions.x / 2f,
                HeapSortView.dimensions.y / 2f,
                HeapSortView.dimensions.x,
                HeapSortView.dimensions.y,
                Color.rgb(180,255,0),
                Color.rgb(0,255,200),
                Shader.TileMode.MIRROR));

        generalPaint5 = new Paint();
        generalPaint5.setStyle(Paint.Style.STROKE);
        generalPaint5.setAntiAlias(true);
        generalPaint5.setColor(Color.LTGRAY);
        generalPaint5.setAlpha(100);

        nodesLinePaint = new Paint();
        nodesLinePaint.setStyle(Paint.Style.FILL);
        nodesLinePaint.setAntiAlias(true);
        nodesLinePaint.setColor(Color.LTGRAY);
        nodesLinePaint.setStrokeWidth(P_SIZE*.5f);
        nodesLinePaint.setAlpha(100);

        numberAnimating = 0;

        levelHeightMultiplier = 1f;
        setNumNodes(31 - MIN_NODES);
        seekBarSortSpeed.setMax(MAX_SPEED);
        seekBarSortSpeed.setProgress((int) (MAX_SPEED*.75f));

        seekBarNumNodes.setMax(MAX_NODES-MIN_NODES);
        seekBarNumNodes.setProgress(numNodes-MIN_NODES);

        //System.out.println("numNodes!!: " + numNodes);

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

    public void initNodes()
    {
        animationSequence = new ArrayList<MoveInfo>();
        nodes = new NodeCircle[numNodes];
        nodesCopyPreSort = new NodeCircle[numNodes];
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
            int arrayVal = HeapSortView.getRandomIntegerArray().get(i);
            nodes[i] = new NodeCircle((int) normalizedCircleDiameter, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, arrayVal, this);
            nodesCopyPreSort[i] = new NodeCircle((int) normalizedCircleDiameter, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, arrayVal, this);
        }

        //shuffleArray();

        for(int i = 0; i < numNodes; i++)
        {
            nodesCopyPreSort[i] = new NodeCircle(nodes[i].scale.x, nodes[i].scale.y, nodes[i].getCenterPosition().x, nodes[i].getCenterPosition().y, nodes[i].val, this);
        }
    }

    private void shuffleArray() {

        Random rand = new Random();


        for(int i = 0; i < numNodes; i++)
        {
            System.out.println("i: " + i + " val: "+ HeapSortView.getRandomIntegerArray().get(i));
            int randomIndex = rand.nextInt(numNodes);
            NodeCircle temp = nodes[i];
            NodeCircle randomTemp = nodes[randomIndex];
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

    public Point convertIndexToPosition(int index)
    {
//        Point p = new Point();
//        p.y = HeapView.dimensions.y/2;
//        p.x = blockWidth/2 + index*blockWidth;

        int numNodesCurrentLevel = (int) Math.pow(2, log2(index+1));
        float w = (float) (bottomRowNumNodes/(float)numNodesCurrentLevel);

        Point p = new Point();
        //System.out.println("index: "+ (index + 1) + " || circleDiameter*log2(index): "+ ((int)Math.pow(2, log2(index+1))-1));
        //System.out.println("index: "+ index + " || circleDiameter*log2(index): "+ (HeapView.dimensions.x/2f*(float)(1f/(float)numNodesCurrentLevel)));
        p.y = (int) (yRootStartValue +  normalizedCircleDiameter/2 +(levelHeight)*log2(index+1));
        p.x = (int) ((HeapSortView.dimensions.x/2f*(float)(1f/(float)numNodesCurrentLevel))  + (w * circleDiameter)*( index - ((int)Math.pow(2, log2(index + 1)) - 1)));//(int)numNodesCurrentLevel/2));// + circleDiameter*(1+ index - (int)Math.pow(2, log2(index+1))));//(int)numNodesCurrentLevel/2);

        return p;
    }

    public void setNumNodes(int progress)
    {
        numNodes = MIN_NODES + progress;
        maxLevel = calculateMaxLevel();
        bottomRowNumNodes = calculateBottomRowNumNodes();
        circleDiameter = HeapSortView.dimensions.x/(float)bottomRowNumNodes;

        yRootStartValue = HeapSortView.dimensions.y*.7f;
        yBottomEndValue = HeapSortView.dimensions.y;
        totalHeight = yBottomEndValue - yRootStartValue;

        levelHeight = Math.max((totalHeight-circleDiameter)/(float)maxLevel, circleDiameter*.25f);
        System.out.println("(totalHeight-circleDiameter)/(float)maxLevel: "+ (totalHeight-circleDiameter)/(float)maxLevel + " (float)maxLevel, circleDiameter*.25f  "+ circleDiameter*.25f);
        normalizedCircleDiameter = Math.min(circleDiameter*.75f, MAX_NODE_DIAMETER);
        normalizedCircleDiameter = Math.min(normalizedCircleDiameter, levelHeight);

        System.out.println("maxLevel: "+levelHeight);
        initNodes();
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



    public void moveNode(NodeCircle node, Point start, Point end, int direction)
    {
        if(!node.isAnimating())
        {
            numberAnimating++;
        }

        float mid = node.getCenterPosition().y - levelHeight;
        if(mid < yRootStartValue)
        {
            mid = yRootStartValue + levelHeight;
        }
        float height = levelHeight*2.75f;

        curveControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
        curveControlPoints.set(1, new Point(end.x + (start.x - end.x)/2, (int) (mid + direction*(height*.35f + 3*((float)P_SIZE/ node.getScale().y) *height*.5f))));
        curveControlPoints.set(2, new Point(end.x + (start.x - end.x)/2, (int) (mid + direction*(height*.35f + 3*((float)P_SIZE/ node.getScale().y) *height*.5f))));
        curveControlPoints.set(3, new Point(end.x, end.y));

        node.followCurve(bc.calculatePoints(NUM_BEZIER_POINTS, curveControlPoints));
        node.setCenterDestinationPosition(end.x, end.y);
    }

    public void moveNodeToPosition(NodeCircle node, Point endPoint, int direction)
    {
        Point startPoint = new Point(node.getCenterDestinationPosition());
        moveNode(node, startPoint, endPoint, direction);
    }

    public int calculateMaxLevel()
    {
        if(numNodes == 0)
        {
            return 0;
        }
        return (int) Math.ceil(log2(numNodes));
    }

    private int log2(int N)
    {
        return (int)(Math.log(N) / Math.log(2));
    }

    public int calculateBottomRowNumNodes()
    {
        return (int) Math.pow(2, calculateMaxLevel()+1)/2;
    }

    public int getNumNodes() {
        return numNodes;
    }
    public int getSortSpeed() {
        return (int) ((int) 100f*(1f-(getProgressBarPercentage())));
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

    public void setSortSpeed(int progress) {

        if(progress >= MAX_SPEED-MIN_SPEED)
        {
            progress = MAX_SPEED-MIN_SPEED;
        }
        animationSpeedIncrement = MAX_SPEED/SLIDER_SCALE_FACTOR - progress/SLIDER_SCALE_FACTOR;
    }


    private void drawGrid(Canvas canvas, int xDim, int yDim) {

        generalPaint5.setAlpha(generalPaint5.getAlpha() + 155);
//        canvas.drawLine(HeapView.dimensions.x/2,0,HeapView.dimensions.x/2, HeapView.dimensions.y, generalPaint5);
//        canvas.drawLine(0,HeapView.dimensions.y/2,HeapView.dimensions.x, HeapView.dimensions.y/2, generalPaint5);
        generalPaint5.setAlpha(generalPaint5.getAlpha() - 155);

        for(int x = 1; x <= xDim; x++)
        {
            float currentX = x*(HeapSortView.dimensions.x/(float)xDim);
            canvas.drawLine(currentX,0,currentX, HeapSortView.dimensions.y, generalPaint5);
        }

        for(int y = 1; y <= yDim; y++)
        {
            float currentY = y*(HeapSortView.dimensions.y/(float)yDim);

            canvas.drawLine(0,currentY, HeapSortView.dimensions.x, currentY, generalPaint5);
        }
    }
}