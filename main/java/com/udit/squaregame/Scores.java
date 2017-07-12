package com.udit.squaregame;

/**
 * Created by User on 11-Jul-17.
 */

public class Scores {

    long time;
    String firstPlayer,secondPlayer;
    int firstScore,secondScore;

    public Scores(){
    }

    public Scores(long time,String firstPlayer,String secondPlayer,int firstScore,int secondScore) {
        this.time = time;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstScore = firstScore;
        this.secondScore = secondScore;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFirstPlayer() {
        return this.firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public String getSecondPlayer() {
        return this.secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public int getFirstScore() {
        return this.firstScore;
    }

    public void setFirstScore(int firstScore) {
        this.firstScore = firstScore;
    }

    public int getSecondScore() {
        return this.secondScore;
    }

    public void setSecondScore(int secondScore) {
        this.secondScore = secondScore;
    }
}
