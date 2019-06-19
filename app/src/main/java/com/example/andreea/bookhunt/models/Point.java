package com.example.andreea.bookhunt.models;

public class Point {
    private String xValue;
    private long yValue;

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public long getyValue() {
        return yValue;
    }

    public void setyValue(long yValue) {
        this.yValue = yValue;
    }

    public Point(String xValue, long yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }
}
