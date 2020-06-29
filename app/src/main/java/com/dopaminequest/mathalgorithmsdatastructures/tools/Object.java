package com.dopaminequest.mathalgorithmsdatastructures.tools;

import android.graphics.Canvas;
import android.graphics.Point;

public abstract class Object {
//    public Point getPosition() {
//        return position;
//    }
//    public void setPosition(Point position) {
//        this.position = position;
//    }
//    public Point getScale() {
//        return scale;
//    }
//    public void setScale(Point scale) {
//        this.scale = scale;
//    }
//
//    Point position;
//    Point scale;

    protected Point getPosition() {
        return position;
    }
    protected void setPosition(Point position) {
        this.position = position;
    }
    protected Point getScale() {
        return scale;
    }
    protected void setScale(Point scale) {
        this.scale = scale;
    }
    protected abstract void update();
    protected abstract void draw(Canvas canvas);
    protected Point position;
    protected Point scale;
}
