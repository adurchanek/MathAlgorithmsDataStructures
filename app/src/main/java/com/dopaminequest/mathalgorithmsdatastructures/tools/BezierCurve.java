package com.dopaminequest.mathalgorithmsdatastructures.tools;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;

public class BezierCurve extends Object{

    public Point canvasDimensions;
    private double time = 0;
    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;
    private ArrayList<Point> controlPoints;

    public BezierCurve()
    {
        ArrayList<Point> cPoints = new ArrayList<Point>();
        for(int i = 0; i < 4; i ++)
        {
            cPoints.add(new Point(i*50,i*50 ));
        }

        init(cPoints);
    }

    public BezierCurve(ArrayList<Point> cPoints)
    {
        init(cPoints);
    }

    public void init(ArrayList<Point> cPoints)
    {
        this.canvasDimensions = new Point();

        position = new Point();
        scale = new Point();

        p1 = cPoints.get(0);
        p2 = cPoints.get(1);
        p3 = cPoints.get(2);
        p4 = cPoints.get(3);

        controlPoints = new ArrayList<Point>();
        controlPoints.add(p1);
        controlPoints.add(p2);
        controlPoints.add(p3);
        controlPoints.add(p4);
    }

    public Point[] calculatePoints(int numLinePoints)
    {
        Point bezierPoint = new Point();
        Point[] linePoints = new Point[numLinePoints];
        initializeLinePoints(numLinePoints, linePoints);
        for(int index = 0; index < numLinePoints-1; index++)
        {
            double t = (double)index / (numLinePoints-1);

            bezierPoint.x = (int) ((Math.pow(1 - t, 3) * controlPoints.get(0).x + Math.pow(1 - t, 2) * t * 3 * controlPoints.get(1).x  + (1 - t) * Math.pow(t, 2) * 3 * controlPoints.get(2).x  + Math.pow(t, 3) * controlPoints.get(3).x));
            bezierPoint.y = (int) ((Math.pow(1 - t, 3) * controlPoints.get(0).y + Math.pow(1 - t, 2) * t * 3 * controlPoints.get(1).y  + (1 - t) * Math.pow(t, 2) * 3 * controlPoints.get(2).y  + Math.pow(t, 3) * controlPoints.get(3).y));

            linePoints[index].x =  bezierPoint.x;
            linePoints[index].y =  bezierPoint.y;
        }

        linePoints[numLinePoints-1].x = controlPoints.get(controlPoints.size()-1).x;
        linePoints[numLinePoints-1].y = controlPoints.get(controlPoints.size()-1).y;


        return linePoints;
    }

    public Point[] calculatePoints(int numLinePoints, ArrayList<Point> controlPoints)
    {
        Point bezierPoint = new Point();
        Point[] linePoints = new Point[numLinePoints];
        initializeLinePoints(numLinePoints, linePoints);
        for(int index = 0; index < numLinePoints-1; index++)
        {
            double t = (double)index / (numLinePoints-1);

            bezierPoint.x = (int) ((Math.pow(1 - t, 3) * controlPoints.get(0).x + Math.pow(1 - t, 2) * t * 3 * controlPoints.get(1).x  + (1 - t) * Math.pow(t, 2) * 3 * controlPoints.get(2).x  + Math.pow(t, 3) * controlPoints.get(3).x));
            bezierPoint.y = (int) ((Math.pow(1 - t, 3) * controlPoints.get(0).y + Math.pow(1 - t, 2) * t * 3 * controlPoints.get(1).y  + (1 - t) * Math.pow(t, 2) * 3 * controlPoints.get(2).y  + Math.pow(t, 3) * controlPoints.get(3).y));

            linePoints[index].x =  bezierPoint.x;
            linePoints[index].y =  bezierPoint.y;
        }

        linePoints[numLinePoints-1].x = controlPoints.get(controlPoints.size()-1).x;
        linePoints[numLinePoints-1].y = controlPoints.get(controlPoints.size()-1).y;

        return linePoints;
    }

    private void initializeLinePoints(int numLinePoints, Point[] linePoints)
    {
        for(int i = 0; i < numLinePoints; i++)
        {
            linePoints[i] = new Point(0,0);
        }
    }

    public void reset() {

        ArrayList<Point> cPoints = new ArrayList<Point>();
        for(int i = 0; i < 4; i ++)
        {
            cPoints.add(new Point(i*50,i*50 ));
        }

        init(cPoints);
    }

    private float getDistance(Point p, Point input)
    {
        return (float) Math.sqrt(Math.pow(input.x - p.x, 2) + Math.pow(input.y - p.y, 2));
    }

    @Override
    protected void update() {

    }

    @Override
    protected void draw(Canvas canvas) {

    }
}