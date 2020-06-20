package com.dopaminequest.mathalgorithmsdatastructures.views.Dijkstras;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.Comparator;
import java.util.Vector;

public class Square extends Object implements Comparator<Square> {

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
        Start, End, Blocked, Explored, Unexplored, Path, Found;
    }

    public int cost;
    private boolean animating;
    private float animatingStep;
    private float speed;
    private Rect mRectSquare;
    public Paint mPaintSquare;
    private int padding;
    public State state;
    public State editState;
    public Boolean kvKnown;
    public int dvLength;
    public int pvPrevious;
    public int index;
    public Vector<Square> neighbors;

    Square()
    {

    }

    Square(int w, int h, int x, int y, int index )
    {
        init(h,w,x,y,index);
    }

    @Override
    public void update() {

        if(animating)
        {
            animatingStep -= speed*1f;

            if(animatingStep <= 0)
            {
                animatingStep = 0;
                animating = false;
            }
        }

        if(isPressed())
        {
            if(state == Square.State.Start || state == Square.State.End)
            {
                return;
            }

            editState();
            animate(1f);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(animatingStep*6, position.x + scale.x/2,position.y + scale.x/2);
        mRectSquare.left = position.x + (int)animatingStep+padding;
        mRectSquare.right = position.x - (int)animatingStep + scale.x - padding;
        mRectSquare.top = position.y + (int)animatingStep+ padding;
        mRectSquare.bottom = position.y  - (int)animatingStep + scale.y - padding;
        mPaintSquare.setColor((int) (getColor()+ animatingStep*10));
        mPaintSquare.setAlpha((int)(55 + (200*((scale.x/2 - animatingStep)/(scale.x/2 )))));
        canvas.drawRect(mRectSquare,mPaintSquare);
        canvas.restore();
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
        cost = 10;
        this.index = index;
        neighbors = new Vector<Square>();
        animating = false;
        animatingStep = 0;
        speed = 1f;
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
                c = Color.LTGRAY;
                break;

            case Path:
                c = Color.GREEN;
                break;
            case Found:
                c = Color.YELLOW;
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
        if(DijkstraView.input.x >= position.x && DijkstraView.input.x <= position.x+scale.x )
        {
            if(DijkstraView.input.y >= position.y && DijkstraView.input.y <= position.y+scale.y )
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
    }

    public void animate(float s)
    {
        animating = true;
        animatingStep = (float)scale.x/2f;
        speed = s;
    }

    public void setCost(int c)
    {
        cost = c;
    }

    private void editState() {
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
                DijkstraView.g.startIndex = DijkstraView.g.setStartOrEnd(position.x, position.y, DijkstraView.g.startIndex, State.Start);
                break;

            case End:
                DijkstraView.g.endIndex = DijkstraView.g.setStartOrEnd(position.x, position.y, DijkstraView.g.endIndex, State.End);
                break;

            case Explored:
                state = State.Explored;
                mPaintSquare.setColor(getColor());
                break;

            case Path:
                state = State.Path;
                mPaintSquare.setColor(getColor());
                break;
            case Found:
                DijkstraView.g.endIndex = DijkstraView.g.setStartOrEnd(position.x, position.y, DijkstraView.g.endIndex, State.End);
                break;
        }
    }

}
