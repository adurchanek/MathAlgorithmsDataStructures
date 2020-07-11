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

public class RectangleCanvas extends Object {

    private SeekBar seekBarSortSpeed;
    private SeekBar seekBarNumNodes;
    private Paint generalPaint;

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

    private int blockWidth;
    private int size;
    private Paint paintText;
    private int textAlpha;
    private NodeRect[] nodes;
    private NodeRect[] nodesCopyPreSort;
    private int numNodes;

    private int NUM_BEZIER_POINTS = 60;
    private boolean autoSort;

    private int iSort;
    private int jSort;

    public int numberAnimating;
    private boolean nextSort;
    int animationIndex;

    private float yBottomEndValue;
    private float yRootStartValue;

    float animationSpeedCounter;
    int animationSpeed;

    private float animationSpeedIncrement;
    NodeRect currentlyAnimatingNode;

    HashMap<Integer, Integer> nodeConverter;
    ArrayList<MoveInfo> animationSequence;
    ArrayList<Integer> randomNumArray;
    private float totalHeight;

    public ArrayList<Integer> getRandomIntegerArray() {
        return randomNumArray;
    }

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

    public RectangleCanvas(Activity context)
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
                    NodeRect.rightNode = nodes[nodeConverter.get(moveInfo.indexVal)];
                }
                else if(moveInfo.leftOrRight.equals("Right NULL"))
                {
                    NodeRect.rightNode = null;
                }
                if(moveInfo.leftOrRight.equals("L"))
                {
                    NodeRect.leftNode = nodes[nodeConverter.get(moveInfo.indexVal)];
                }
                else if(moveInfo.leftOrRight.equals("Left NULL"))
                {
                    NodeRect.leftNode = null;
                }

            }
        }
        animationIndex += numberNodesToAnimate;
        animationSpeedCounter = 0f;
    }

    private void processMoveInfo(MoveInfo moveInfo)
    {
        NodeRect currentNode = nodes[nodeConverter.get(moveInfo.indexVal)];

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
            NodeRect.maxNode = currentNode;
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

            NodeRect temp = nodes[i];
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

            NodeRect temp = nodes[0];
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
        numNodes = 31;

        bc = new BezierCurve();
        stackPaint = new Paint();
        stackPaint.setColor(Color.GREEN);
        stackPaint.setAlpha(55);

        maxBlockHeight = HeapSortView.dimensions.y*.3f;
        minBlockHeight = HeapSortView.dimensions.y*.05f;
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

    public void initNodes()
    {
        randomNumArray = new ArrayList<Integer>();
        animationSequence = new ArrayList<MoveInfo>();
        blockWidth = HeapSortView.dimensions.x/ numNodes;
        nodes = new NodeRect[numNodes];
        nodesCopyPreSort = new NodeRect[numNodes];
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
            nodes[i] = new NodeRect(blockWidth, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, i+1, this);
            nodesCopyPreSort[i] = new NodeRect(blockWidth, (int) ( minBlockHeight + blockHeight *(i)), p.x, (int) p.y, i+1, this);
        }

        shuffleArray();

        for(int i = 0; i < numNodes; i++)
        {
            nodesCopyPreSort[i] = new NodeRect(nodes[i].scale.x, nodes[i].scale.y, nodes[i].getCenterPosition().x, nodes[i].getCenterPosition().y, nodes[i].val, this);
            randomNumArray.add(nodes[i].val);
        }
    }

    private void shuffleArray() {

        Random rand = new Random();

        for(int i = 0; i < numNodes; i++)
        {
            int randomIndex = rand.nextInt(numNodes);
            NodeRect temp = nodes[i];
            NodeRect randomTemp = nodes[randomIndex];
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

    public void moveNode(NodeRect node, Point start, Point end, int direction)
    {
        if(!node.isAnimating())
        {
            numberAnimating++;
        }

        float mid = yRootStartValue + totalHeight/2;

        float height = totalHeight*.75f;

        curveControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
        curveControlPoints.set(1, new Point(end.x + (start.x - end.x)/2, (int) (mid + direction*(2*height*.35f + 3*((float)P_SIZE/ node.getScale().y) *height))));
        curveControlPoints.set(2, new Point(end.x + (start.x - end.x)/2, (int) (mid + direction*(2*height*.35f + 3*((float)P_SIZE/ node.getScale().y) *height))));
        curveControlPoints.set(3, new Point(end.x, end.y));



//        curveControlPoints.set(0, new Point(node.getCenterPosition().x, node.getCenterPosition().y));
//        curveControlPoints.set(1, new Point(end.x + (start.x - end.x)/2, (int) (HeapSortView.dimensions.y*.5f + direction*(HeapSortView.dimensions.y*.35f + 3*((float)P_SIZE/ node.getScale().y) *HeapSortView.dimensions.y*.5f))));
//        curveControlPoints.set(2, new Point(end.x + (start.x - end.x)/2, (int) (HeapSortView.dimensions.y*.5f + direction*(HeapSortView.dimensions.y*.35f + 3*((float)P_SIZE/ node.getScale().y) *HeapSortView.dimensions.y*.5f))));
//        curveControlPoints.set(3, new Point(end.x, end.y));

        node.followCurve(bc.calculatePoints(NUM_BEZIER_POINTS, curveControlPoints));
        node.setCenterDestinationPosition(end.x, end.y);
    }

    public void moveNodeToPosition(NodeRect node, Point endPoint, int direction)
    {
        Point startPoint = new Point(node.getCenterDestinationPosition());
        moveNode(node, startPoint, endPoint, direction);
    }

    public Point convertIndexToPosition(int index)
    {
        Point p = new Point();
        //p.y = HeapSortView.dimensions.y/2;
        p.y = (int) (yRootStartValue + totalHeight/2f);

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

    public void setSortSpeed(int progress) {

        if(progress >= MAX_SPEED-MIN_SPEED)
        {
            progress = MAX_SPEED-MIN_SPEED;
        }
        animationSpeedIncrement = MAX_SPEED/SLIDER_SCALE_FACTOR - progress/SLIDER_SCALE_FACTOR;
    }

    public void setNumNodes(int progress) {

        numNodes = MIN_NODES + progress;

        yRootStartValue = .2f;
        yBottomEndValue = HeapSortView.dimensions.y*.45f;
        totalHeight = yBottomEndValue - yRootStartValue;

        initNodes();
    }

    public int getNumNodes() {
        return numNodes;
    }
    public int getSortSpeed() {
        return (int) ((int) 100f*(1f-(getProgressBarPercentage())));
    }
}