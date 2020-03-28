package com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.ScriptC;


import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;


public class Grid extends GameObject{


    enum State
    {
        Start, End, Blocked, Explored, Unexplored, Path;
    }


    int rows;
    int columns;

    Boolean dim = false;

    int x = 10;

    final public int PADDING = 1;


    private Paint mPaintSquare;
    private Paint textPaint;

    public int startIndex;
    public int endIndex;

    public boolean pathFound;

    public boolean resettingGrid = true;



    Square s;

    int numSquares;


    Vector<Square> squares;

    Point canvasDimensions;




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

        x += 1;

        if(DijkstraView.dimensions.x == 0)
        {

            dim = true;
        }

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

        if(DijkstraView.dimensions.x == 0)
        {

            dim = true;
        }

        //canvas.drawColor(Color.RED);

        mPaintSquare.setTextSize(45);
        mPaintSquare.setColor(Color.GREEN);






        for(int i = 0; i < squares.size(); i++)
        {

            squares.get(i).draw(canvas);
        }


//        canvas.drawText("DimWidth: " + DijkstraView.dimensions.x, 20, 500, textPaint);
//        canvas.drawText("DimHeight: " + DijkstraView.dimensions.y, 20, 550, textPaint);
//
//
//        canvas.drawText("Pointx: " + DijkstraView.input.x, 20, 700, textPaint);
//        canvas.drawText("pointy: " + DijkstraView.input.y, 20, 800, textPaint);





    }


    public void init() {

        this.canvasDimensions = new Point();
        squares = new Vector<Square>();
        mPaintSquare = new Paint();

        mPaintSquare = new Paint();
        textPaint = new Paint();
        textPaint.setTextSize(45);
        textPaint.setColor(Color.RED);

        x = 100;

        createGrid(numSquares);

        position = new Point();
        scale = new Point();
        pathFound = false;
        resettingGrid = false;


    }

    public void reset() {



        //createGrid(numSquares);
        //TODO place bool to pause update and draw
        init();

    }

    public void changeState() {



        createGrid(numSquares);

    }



    public void createGrid(int numSquares)
    {

        int width = DijkstraView.dimensions.x/numSquares;
        int height = DijkstraView.dimensions.y/numSquares;
        for(int i = 0; i < numSquares; i++)
        {


            for(int j = 0; j < numSquares; j++) {

                int posX = j * width;

                int posY = i * height;


                int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);
                Square s = new Square(height, width, j * width, i * height,currentSquareIndex);

                squares.add(s);

                //s.neighbors.add(i);
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
                    int posXN = (j-1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
//                    squaresToAdd.add(-1);

                }
                if(j != numSquares - 1)
                {
                    int posXN = (j+1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
//                    squaresToAdd.add(1);

                }

                if(i != 0)
                {
                    int posXN = (j) * width;
                    int posYN = (i-1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
//                    squaresToAdd.add(-1);

                }
                if(i != numSquares - 1)
                {
                    int posXN = (j) * width;
                    int posYN = (i+1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
//                    squaresToAdd.add(1);

                }

//                for(int sIndex = 0; sIndex < squaresToAdd.size(); sIndex++)
//                {
//
//                    int posXN = (j) * width;
//
//                    int posYN = (i) * height;
//
//                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
//                    squares.get( currentNeighbor);
//                }



                //s.neighbors.add(i);
            }



        }


        startIndex = -1;
        endIndex = -1;



    }

    public Vector<Integer> findPath()
    {

        //PriorityQueue<Square> pQueue =  new PriorityQueue<Square>();

        ArrayList<Square> pQ =  new ArrayList<>();



        Vector<Integer> returnPath;

        returnPath = new Vector<Integer>();

        if(startIndex > 0)
        {

            squares.get(startIndex).pvPrevious = -2;
            ///pQueue.add(squares.get(startIndex));
            pQ.add(squares.get(startIndex));
            ///System.out.println("------------------------------- ADDED: " +pQueue.size()+" ");

        }


        for(int i = 0; i < squares.size(); i++)
        {
            //System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- x y: " +i+" "+ squares.get(i).index);
        }

//        while(!pQueue.isEmpty())
        while(!pQ.isEmpty())
        {

            //System.out.println("------------------------------- WHILE: " +returnPath.size()+" ");

///            Square s = pQueue.remove();
            Square s = pQ.remove(0);


            if(!s.kvKnown)
            {

                s.kvKnown = true;

                for(int i = 0; i < s.neighbors.size(); i++)
                {

                    if(s.neighbors.get(i).dvLength > s.dvLength + 1)
                    {
                        //System.out.println("------------------------------- WHILE: " +returnPath.size()+" " + i);
                        s.neighbors.get(i).dvLength = s.dvLength + 1;
                        s.neighbors.get(i).pvPrevious = s.index;
                        ///pQueue.add(s.neighbors.get(i));
                        pQ.add(s.neighbors.get(i));
                    }
                }
            }


        }


        int i = 0;
        if(endIndex > 0)
        {

            Square currentS = squares.get( endIndex);
            while(true)
            {



//                returnPath.add(currentS.index);

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
//                if(i >= 254)
//                {
//                    break;
//
//                }
//                i++;


            }

        }

        return returnPath;



//        for(int i = 0; i < squares.size(); i++)
//        {
//
//            squares.get(i).
//        }


//        for each vertex v
//        {
//            set kv to false
//            set pv to unknown (or none, if v is the start vertex)
//            set dv to ∞ (or 0, if v is the start vertex)
//        }
//
//        let pq be an empty priority queue
//        enqueue the start vertex into pq with priority 0
//
//        while (pq is not empty)
//        {
//            vertex v = the vertex in pq with the smallest priority
//            dequeue the smallest-priority vertex from pq
//
//            if (kv is false)
//            {
//                kv = true
//
//                for each vertex w such that edge v → w exists
//                {
//                    if (dw > dv + C(v, w))
//                    {
//                        dw = dv + C(v, w)
//                        pw = v
//                        enqueue w into pq with priority dw
//                    }
//                }
//            }
//        }




    }



    public boolean findShortestPath()
    {

        if(pathFound)
        {
            return pathFound;
        }

        Vector<Integer> returnPath = findPath();

//        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- FIND PATH: " +returnPath.size()+" ");



        for(int i = 0; i < returnPath.size(); i++)
        {

//            squares.get(returnPath.get(i).x + numSquares*returnPath.get(i).y).setPath();
            squares.get(returnPath.get(i)).setPath();
        }

        if(returnPath.size() > 0)
        {
            pathFound = true;

        }

        //TODO hide buttons
        //create new fragment

        return pathFound;
    }

    public void setStart(int posX, int posY)
    {

        int width = DijkstraView.dimensions.x/numSquares;
        int height = DijkstraView.dimensions.y/numSquares;

//        int x = (int)(numSquares*(float)(DijkstraView.input.x)/(float)DijkstraView.dimensions.x);
//
//
//        int y = (int)((float)numSquares*((float)DijkstraView.input.y/(float)DijkstraView.dimensions.y));

        //int currentSquareIndex = (int)((numSquares*(float)(DijkstraView.input.x)/(float)DijkstraView.dimensions.x) + numSquares*(float)numSquares*((float)DijkstraView.input.y/(float)DijkstraView.dimensions.y));
        int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);



        if(startIndex == -1)
        {
            squares.get(currentSquareIndex).state = Square.State.Start;
            startIndex = currentSquareIndex;
            //System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- x y: " + startIndex);
        }
        else
        {
//            if(squares.get(startIndex).state == Square.State.Start)
//            {
//
//            }

            if(squares.get(currentSquareIndex).state == Square.State.Blocked)
            {

//                int posX = j * width;
//                int posY = i * height;

                int i = (int)((float)posY/(float)height);
                int j = (int)((float)posX/(float)width);

                System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- X Y: " + i + " "+ j);


                //int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);

                if(j != 0)
                {
                    int posXN = (j-1) * width;
                    int posYN = (i) * height;



                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);


                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE1: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }

//                    squaresToAdd.add(-1);

                }
                if(j != numSquares - 1)
                {
                    int posXN = (j+1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);


                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE2: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(1);

                }

                if(i != 0)
                {
                    int posXN = (j) * width;
                    int posYN = (i-1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE3: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(-1);

                }
                if(i != numSquares - 1)
                {
                    int posXN = (j) * width;
                    int posYN = (i+1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    if(squares.get(currentNeighbor).state != Square.State.Blocked) {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE4: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(1);

                }

                System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE #1: " + squares.get(currentSquareIndex).neighbors.size() + " "+ currentSquareIndex);

            }


            squares.get(startIndex).state = Square.State.Unexplored;
            squares.get(startIndex).mPaintSquare.setColor(squares.get(startIndex).getColor());


            squares.get(currentSquareIndex).state = Square.State.Start;
            squares.get(currentSquareIndex).mPaintSquare.setColor(squares.get(currentSquareIndex).getColor());

            startIndex = currentSquareIndex;
        }

        //System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- x y: " + x + " " + y);


    }


    public void setEnd(int posX, int posY)
    {

        int width = DijkstraView.dimensions.x/numSquares;
        int height = DijkstraView.dimensions.y/numSquares;

//        int x = (int)(numSquares*(float)(DijkstraView.input.x)/(float)DijkstraView.dimensions.x);
//
//
//        int y = (int)((float)numSquares*((float)DijkstraView.input.y/(float)DijkstraView.dimensions.y));

        //int currentSquareIndex = (int)((numSquares*(float)(DijkstraView.input.x)/(float)DijkstraView.dimensions.x) + numSquares*(float)numSquares*((float)DijkstraView.input.y/(float)DijkstraView.dimensions.y));
        int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);

        if(endIndex == -1)
        {
            squares.get(currentSquareIndex).state = Square.State.End;
            endIndex = currentSquareIndex;
            //System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- x y: " + endIndex);
        }
        else
        {

            if(squares.get(currentSquareIndex).state == Square.State.Blocked)
            {

//                int posX = j * width;
//                int posY = i * height;

                int i = (int)((float)posY/(float)height);
                int j = (int)((float)posX/(float)width);

                System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- X Y: " + i + " "+ j);


                //int currentSquareIndex = (int)(posX/(float)width + (numSquares*posY)/(float)height);

                if(j != 0)
                {
                    int posXN = (j-1) * width;
                    int posYN = (i) * height;



                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);


                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE1: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get( currentSquareIndex).neighbors.add(squares.get( currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }

//                    squaresToAdd.add(-1);

                }
                if(j != numSquares - 1)
                {
                    int posXN = (j+1) * width;
                    int posYN = (i) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);


                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE2: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(1);

                }

                if(i != 0)
                {
                    int posXN = (j) * width;
                    int posYN = (i-1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    if(squares.get(currentNeighbor).state != Square.State.Blocked)
                    {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE3: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(-1);

                }
                if(i != numSquares - 1)
                {
                    int posXN = (j) * width;
                    int posYN = (i+1) * height;

                    int currentNeighbor = (int)(posXN/(float)width + (numSquares*posYN)/(float)height);
                    if(squares.get(currentNeighbor).state != Square.State.Blocked) {
                        System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE4: " + currentNeighbor + " "+ currentSquareIndex);
                        squares.get(currentSquareIndex).neighbors.add(squares.get(currentNeighbor));
                        squares.get( currentNeighbor).neighbors.add(squares.get( currentSquareIndex));
                    }
//                    squaresToAdd.add(1);

                }

                System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- NICEEEE #1: " + squares.get(currentSquareIndex).neighbors.size() + " "+ currentSquareIndex);

            }


            squares.get(endIndex).state = Square.State.Unexplored;
            squares.get(endIndex).mPaintSquare.setColor(squares.get(endIndex).getColor());
            squares.get(currentSquareIndex).state = Square.State.End;
            squares.get(currentSquareIndex).mPaintSquare.setColor(squares.get(currentSquareIndex).getColor());
            endIndex = currentSquareIndex;
        }

        //System.out.println("--------------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=- x y: " + x + " " + y);


    }


}
