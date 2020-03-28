package com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.Comparator;
import java.util.Vector;

public class Square extends GameObject implements Comparator<Square> {

//    public int node;
    public int cost;

    @Override
    public int compare(Square square1, Square square2) {
        if (square1.cost < square2.cost)
            return -1;
        if (square1.cost > square2.cost)
            return 1;
        return 0;
    }



    enum State
    {
        Start, End, Blocked, Explored, Unexplored, Path;
    }



    Paint color;
    private Rect mRectSquare;

    public Paint mPaintSquare;
    int x;

    int padding;
    State state;
    State editState;

    public Boolean kvKnown;
    public int dvLength;
    public int pvPrevious;

    public int index;

    public Vector<Square> neighbors;












    Square() {




    }


    Square(int w, int h, int x, int y, int index ) {


        //x = 0;
        init(h,w,x,y,index);


    }


    @Override
    public void update() {
        //x++;

        if(isPressed())
        {

            //state = State.Blocked;



            switch (DijkstraView.editState)
            {
                case Blocked:
                    if(state == State.Start)
                    {
                        DijkstraView.g.startIndex = -1;
                    }

                    if(state == State.End)
                    {
                        DijkstraView.g.endIndex = -1;
                    }


                    for(int i = 0; i < neighbors.size();i++)

                    {
                        Square s = neighbors.get(i);
                        s.neighbors.remove(this);
                    }

                    neighbors.clear();

                    state = State.Blocked;
                    mPaintSquare.setColor(getColor());
                    break;

                case Unexplored:
                    state = State.Unexplored;
                    mPaintSquare.setColor(getColor());
                    break;

                case Start:
                    //state = State.Start;
                    DijkstraView.g.setStart(position.x, position.y);

//                    System.out.println("------------------------------- ");
//                    for(int i = 0; i < neighbors.size(); i++)
//                    {
//                        System.out.println("------------------------------- NEIGHBOR: " + neighbors.get(i).index);
//
//                    }
//                    System.out.println("------------------------------- \n");
                    //mPaintSquare.setColor(getColor());
                    break;

                case End:


                    DijkstraView.g.setEnd(position.x, position.y);
                    break;

                case Explored:
                    state = State.Explored;
                    mPaintSquare.setColor(getColor());
                    break;

                case Path:
                    state = State.Path;
                    mPaintSquare.setColor(getColor());
                    break;


            }
        }

    }

    @Override
    public void draw(Canvas canvas) {

        mRectSquare.left = position.x+padding;
        mRectSquare.right = mRectSquare.left + scale.x-padding;
        mRectSquare.top = position.y+padding;
        mRectSquare.bottom = mRectSquare.top + scale.y-padding;

        ///
        if(DijkstraView.actionDown)
        {
            //mPaintSquare.setColor(getColor());

        }


        canvas.drawRect(mRectSquare,mPaintSquare);

    }


    public void init(int w, int h, int x, int y, int index) {

        mRectSquare = new Rect();
        mPaintSquare = new Paint();

        position = new Point();
        scale = new Point();

        position.x = x;
        position.y = y;
        scale.x = h;
        scale.y = w;
        padding = 1;

        state = State.Unexplored;
        editState = State.Blocked;

        mPaintSquare.setColor(getColor());

        kvKnown = false;
        dvLength = Integer.MAX_VALUE;
        pvPrevious = -1;

        cost = 1;

        this.index = index;
        neighbors = new Vector<Square>();


    }

    public int getColor()
    {

        int c = 0;
        switch (state)
        {
            case Blocked:
                c = Color.BLACK;
                break;

            case Unexplored:
                c = Color.GRAY;
                break;

            case Start:
                c = Color.CYAN;
                break;

            case End:
                c = Color.RED;
                break;

            case Explored:
                c = Color.MAGENTA;
                break;

            case Path:
                c = Color.GREEN;
                System.out.println("----  --  -  -------=--========-=-=-=-=-========-=-=--=-=-=test");
                break;


        }


        return c;

    }


    public Boolean isPressed()
    {


        if(!DijkstraView.actionDown)
        {
            return false;
        }
        if(DijkstraView.input.x >= position.x &&DijkstraView.input.x <= position.x+scale.x )
        {
            if(DijkstraView.input.y >= position.y &&DijkstraView.input.y <= position.y+scale.y )
            {
                return true;

            }

        }

        return false;

    }


    public void setPath()
        {


            state = State.Path;
            mPaintSquare.setColor(getColor());
            System.out.println("------- -- -- --        -------=--======= =-=-=-=-=-==== ==    ==-=-=--=-=-=test" + position.x + " " + position.y);
        }


}
