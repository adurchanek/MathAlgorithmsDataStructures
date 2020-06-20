package com.dopaminequest.mathalgorithmsdatastructures.views.AStar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;
import androidx.annotation.RequiresApi;


public class Grid extends Object {

    private int rows;
    private int columns;
    private Paint mPaintSquare;
    private Paint textPaint;
    public int startIndex;
    public int endIndex;
    public boolean pathFound;
    public boolean resettingGrid = true;
    public Square s;
    public int numSquares;
    public Vector<Square> squares;
    public Point canvasDimensions;
    public int pathSequenceIndex;
    public int exploredSequenceIndex;
    public Vector<Integer> exploredSquares;
    public Vector<Integer> returnPath;

    public Grid(int numSquares)
    {
        this.rows = numSquares;
        this.columns = numSquares;
        this.numSquares = numSquares;

        init();
    }

    @Override
    public void update() {

        if(resettingGrid)
        {
            return;
        }

        advanceExploredSequence();

        for(int i = 0; i < squares.size(); i++)
        {
            squares.get(i).update();
        }
    }

    @Override
    public void draw(Canvas canvas) {

        if(resettingGrid)
        {
            return;
        }

        mPaintSquare.setTextSize(45);
        mPaintSquare.setColor(Color.GREEN);

        for(int i = 0; i < squares.size(); i++)
        {
            squares.get(i).draw(canvas);
        }
    }

    public void init()
    {
        this.canvasDimensions = new Point();
        squares = new Vector<Square>();
        mPaintSquare = new Paint();
        mPaintSquare = new Paint();
        textPaint = new Paint();
        textPaint.setTextSize(45);
        textPaint.setColor(Color.RED);
        createGrid(numSquares);
        position = new Point();
        scale = new Point();
        pathFound = false;
        resettingGrid = false;
        pathSequenceIndex = -1;
        exploredSequenceIndex = -1;
    }

    public void reset() {
        init();
    }

    public void changeState() {
        createGrid(numSquares);
    }

    public void createGrid(int numSquares)
    {
        int width = AStarView.dimensions.x/numSquares;
        int height = AStarView.dimensions.y/numSquares;
        for(int i = 0; i < numSquares; i++)
        {
            for(int j = 0; j < numSquares; j++) {

                int posX = j * width;
                int posY = i * height;
                int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);
                Square s = new Square(height, width, j * width, i * height, currentSquareIndex);
                squares.add(s);
            }
        }

        for(int i = 0; i < numSquares; i++)
        {
            for(int j = 0; j < numSquares; j++) {

                int posX = j * width;
                int posY = i * height;

                int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);

                if(j != 0)
                {
                    int posXN = (j - 1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                }
                if(j != numSquares - 1)
                {
                    int posXN = (j + 1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                }

                if(i != 0)
                {
                    int posXN = (j) * width;
                    int posYN = (i - 1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                }
                if(i != numSquares - 1)
                {
                    int posXN = (j) * width;
                    int posYN = (i + 1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                }
            }
        }

        startIndex = -1;
        endIndex = -1;

        int startX = (int)(width*0);
        int startY = (int)(height*0);

        int endX = (int)(height*(numSquares-1));;
        int endY = (int)(height*(numSquares-1));;

        startIndex = setStartOrEnd(startX, startY, startIndex, Square.State.Start);
        endIndex = setStartOrEnd(endX, endY, endIndex, Square.State.End);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Vector<Integer> findPath()
    {
        //ArrayList<Square> pQ =  new ArrayList<>();
        class SquareComparator implements Comparator<Square> {
            public int compare(Square square1, Square square2) {
                return Double.compare(square1.fCost, square2.fCost);
            }
        }

        PriorityQueue<Square> pQ = new PriorityQueue<Square>(new SquareComparator());

        exploredSquares = new Vector<Integer>();
        returnPath = new Vector<Integer>();

        if(startIndex >= 0)
        {
            squares.get(startIndex).pvPrevious = -2;
            squares.get(startIndex).dvLength = 0;
            int hCost = (int) Math.sqrt(Math.pow(squares.get(endIndex).getPosition().x - squares.get(startIndex).getPosition().x,2)+ Math.pow(squares.get(endIndex).getPosition().y - squares.get(startIndex).getPosition().y,2));
            squares.get(startIndex).fCost = hCost;
            pQ.add(squares.get(startIndex));
        }

        while(!pQ.isEmpty())
        {
            Square s = pQ.poll();

            if(squares.get(s.index).index== endIndex)
            {
                break;
            }

//            if(!s.kvKnown)
//            {
//                s.kvKnown = true;

            for(int i = 0; i < s.neighbors.size(); i++)
            {
                int newGCost = s.dvLength + s.neighbors.get(i).cost;
                int neighborHCost = (int) Math.sqrt(Math.pow(squares.get(endIndex).getPosition().x - s.neighbors.get(i).getPosition().x,2)+ Math.pow(squares.get(endIndex).getPosition().y - s.neighbors.get(i).getPosition().y,2));
                int newFCost = neighborHCost + newGCost;

                if(newFCost < s.neighbors.get(i).fCost)
                {
                    s.neighbors.get(i).dvLength = newGCost;
                    s.neighbors.get(i).fCost = newFCost;
                    s.neighbors.get(i).pvPrevious = s.index;

                    if(pQ.contains(s.neighbors.get(i)))
                    {
                        pQ.remove(s.neighbors.get(i));
                        exploredSquares.remove(s.neighbors.get(i).index);
                    }

                    pQ.add(s.neighbors.get(i));
                    exploredSquares.add(s.neighbors.get(i).index);
                }
            }
            //}
        }

        if(endIndex >= 0)
        {
            Square currentS = squares.get( endIndex);
            while(true)
            {
                if(currentS.index != endIndex)
                {
                    returnPath.add(currentS.index);
                }

                if(currentS.pvPrevious == -1)
                {
                    returnPath.clear();
                    break;
                }

                currentS = squares.get( currentS.pvPrevious);
                if(currentS.index == startIndex)
                {
                    break;
                }
            }
        }
        return returnPath;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean findShortestPath()
    {
        if(pathFound)
        {
            return pathFound;
        }

        Vector<Integer> returnPath = findPath();

        if(returnPath.size() > 0)
        {
            pathFound = true;
            pathSequenceIndex = returnPath.size() - 1;
        }

        if(exploredSquares.size() > 0)
        {
            exploredSequenceIndex = 0;
        }

        return pathFound;
    }

    public int setStartOrEnd(int posX, int posY, int index, Square.State state)
    {

        int width = AStarView.dimensions.x/numSquares;
        int height = AStarView.dimensions.y/numSquares;
        int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);

        if(index == -1)
        {
            index = currentSquareIndex;
        }

        if(squares.get(currentSquareIndex).state == Square.State.Blocked)
        {
            int i = (int)((float)posY/(float)height);
            int j = (int)((float)posX/(float)width);

            if(j != 0)
            {
                int posXN = (j-1) * width;
                int posYN = (i) * height;
                int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);

                if(squares.get(currentNeighbor).state != Square.State.Blocked)
                {
                    squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
                    squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                }
            }

            if(j != numSquares - 1)
            {
                int posXN = (j+1) * width;
                int posYN = (i) * height;
                int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);

                if(squares.get(currentNeighbor).state != Square.State.Blocked)
                {
                    squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                    squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                }
            }

            if(i != 0)
            {
                int posXN = (j) * width;
                int posYN = (i-1) * height;

                int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                if(squares.get(currentNeighbor).state != Square.State.Blocked)
                {
                    squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                    squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                }
            }

            if(i != numSquares - 1)
            {
                int posXN = (j) * width;
                int posYN = (i+1) * height;

                int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                if(squares.get(currentNeighbor).state != Square.State.Blocked) {
                    squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                    squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                }
            }
        }

        squares.get(index).state = Square.State.Unexplored;
        squares.get(index).mPaintSquare.setColor(squares.get(index).getColor());
        squares.get(currentSquareIndex).state = state;
        squares.get(currentSquareIndex).mPaintSquare.setColor(squares.get(currentSquareIndex).getColor());
        index = currentSquareIndex;

        return index;
    }

    private void advanceExploredSequence() {
        if(exploredSequenceIndex != -1)
        {
            if(exploredSequenceIndex < exploredSquares.size())
            {
                int current = exploredSquares.get(exploredSequenceIndex);
                if(squares.get(current).state != Square.State.Start && squares.get(current).state != Square.State.End)
                {
                    squares.get(current).state = Square.State.Explored;
                    squares.get(current).mPaintSquare.setColor(squares.get(current).getColor());
                    squares.get(current).animate(.7f);
                }
                else if(squares.get(current).state == Square.State.End)
                {
                    squares.get(current).state = Square.State.Found;
                    squares.get(current).mPaintSquare.setColor(squares.get(current).getColor());
                }
                exploredSequenceIndex++;
            }
            else
            {
                exploredSequenceIndex = -1;
            }
        }
        else if(pathSequenceIndex != -1)
        {
            if(pathSequenceIndex >= returnPath.size()/2)
            {
                squares.get(returnPath.get(pathSequenceIndex)).setPath();
                squares.get(returnPath.get(pathSequenceIndex)).animate(.4f);

                squares.get(returnPath.get(returnPath.size() - 1 - pathSequenceIndex)).setPath();
                squares.get(returnPath.get(returnPath.size() - 1 - pathSequenceIndex)).animate(.4f);

                pathSequenceIndex--;
            }
            else
            {
                pathSequenceIndex = -1;
            }
        }
    }
}
