package com.dopaminequest.mathalgorithmsdatastructures.views.mathviews.LagrangeInterpolation;

import android.graphics.Canvas;
import android.graphics.Point;

public abstract class Object {
    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }
    public Point getScale() {
        return scale;
    }
    public void setScale(Point scale) {
        this.scale = scale;
    }
    public abstract void update();
    public abstract void draw(Canvas canvas);
    Point position;
    Point scale;
}
