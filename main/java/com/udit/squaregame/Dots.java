package com.udit.squaregame;

/**
 * Created by User on 01-Jul-17.
 */

public class Dots {

    public int idxX;
    public int idxY;
    public int color;

    public Dots(int idxX,int idxY,int color){
        this.idxX = idxX;
        this.idxY = idxY;
        this.color = color;
    }

    public void setIdxX(int idxX){
        this.idxX = idxX;
    }
    public void setIdxY(int idxY){
        this.idxY = idxY;
    }
    public void setColor(int color){
        this.color = color;
    }
    public int getIdxX(){
        return this.idxX;
    }
    public int getIdxY(){
        return this.idxY;
    }
    public int getColor(){
        return this.color;
    }
}
