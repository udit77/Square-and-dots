package com.udit.squaregame;

/**
 * Created by User on 04-Jul-17.
 */

public class LineMap {

    public int fromIdx;
    public int toIdx;
    public int color;

    public LineMap(int fromIdx,int toIdx,int color){
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
        this.color = color;
    }


    public void setFromIdx (int fromIdx){
        this.fromIdx = fromIdx;
    }
    public void setToIdx(int toIdx){
        this.toIdx = toIdx;
    }
    public void setColor(int color){this.color = color; }

    public int getFromIdx(){
        return this.fromIdx;
    }

    public  int getToIdx(){
        return this.toIdx;
    }

    public int getColor(){
        return this.color;
    }
}
