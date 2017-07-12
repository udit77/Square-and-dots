package com.udit.squaregame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.attr.resource;
import static android.R.attr.textColor;
import static java.lang.Thread.sleep;

/**
 * Created by User on 27-Jun-17.
 */

public class GameSurfaceView extends SurfaceView implements Runnable {

    public int GRID_SIZE = 6;
    public int SQUARES = GRID_SIZE-1;
    Thread thread;
    Bitmap backgroundImage,avatar;
    Canvas canvas;
    volatile boolean running;
    SurfaceHolder surfaceHolder;
    int gameBoard_Offset = 0;
    int initialX = 0,initialY = 0;
    float dotRadius = 8;
    Dots [][] dots = new Dots[GRID_SIZE][GRID_SIZE];
    ArrayList<LineMap> lineMap = new ArrayList<>();

    int []X_IDX = new int[GRID_SIZE];
    int []Y_IDX = new int[GRID_SIZE];


    int [] squareGrid = new int[SQUARES*SQUARES];
    int [] squareColor = new int[SQUARES*SQUARES];

    boolean gameOn = false;
    boolean first_player_play = true;
    public int firstPlayerScore = 0,secondPlayerScore = 0;
    Typeface custom_font;
    SharedPreference sharedPreference;
    Scores scores = new Scores();

    public GameSurfaceView(Context context) {
        super(context);
        this.thread = null;
        this.running = false;
        this.surfaceHolder = getHolder();
        this.backgroundImage = getScaledImage(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        this.avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        gameBoard_Offset = Game.surfaceViewWidth/GRID_SIZE;
        initialX = gameBoard_Offset/2;
        initialY = (Game.surfaceViewHeight- Game.surfaceViewWidth)/2+gameBoard_Offset/4;
        initializeGameBoard(initialX,initialY,gameBoard_Offset);
        dotRadius = PxToDp(dotRadius,getContext());
        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/post_rock.ttf");
        sharedPreference = new SharedPreference();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onResumeMySurfaceView(){
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void onPauseMySurfaceView() {
        boolean retry = true;
        this.running = false;
        while (retry) {
            try {
                this.thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        while (this.running) {
            if (this.surfaceHolder.getSurface().isValid()) {
                this.canvas = this.surfaceHolder.lockCanvas();
                drawBackGround();
                drawScores();
                try {
                    drawLines();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawRectangles();
                drawDots();
                drawAvtar();
                autoPlay();
                this.surfaceHolder.unlockCanvasAndPost(this.canvas);
            }
        }
    }

    public void drawScores(){
        int finalY = initialY+(GRID_SIZE-1)*gameBoard_Offset;

        Rect rect1 = new Rect();
        rect1.set(0,finalY,Game.surfaceViewWidth/2,Game.surfaceViewHeight);
        Rect rect2 = new Rect();
        rect2.set(Game.surfaceViewWidth/2,finalY,Game.surfaceViewWidth,Game.surfaceViewHeight);

        this.canvas.getClipBounds(rect1);
        int color1 = Color.rgb(85,255,0);
        int color2 = Color.RED;

        String text1 = Game.Player1+" "+firstPlayerScore;
        String text2 = Game.Player2+" "+secondPlayerScore;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(custom_font);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(PxToDp(36,getContext()));
        paint.setColor(color1);
        paint.getTextBounds(text1, 0, text1.length(), rect1);
        int x1 = Game.surfaceViewWidth/4;
        int y1 = finalY+(Game.surfaceViewHeight-finalY)/2;
        this.canvas.drawText(text1, x1, y1, paint);

        paint.setColor(color2);
        paint.getTextBounds(text2, 0, text2.length(), rect2);
        this.canvas.drawText(text2, x1+Game.surfaceViewWidth/2, y1, paint);
    }

    public  void drawAvtar(){
        int y1 = initialY/2-this.avatar.getHeight()/2;
        int x1 = Game.surfaceViewWidth/4-this.avatar.getWidth()/2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        this.canvas.drawBitmap(this.avatar,x1,y1,paint);
        this.canvas.drawBitmap(this.avatar,x1+Game.surfaceViewWidth/2,y1,paint);
        if(first_player_play){
            paint.setColor(Color.rgb(85,255,0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(PxToDp(dotRadius/8,getContext()));
            this.canvas.drawCircle(x1+this.avatar.getWidth()/2,y1+this.avatar.getHeight()/2,(this.avatar.getHeight()/2)+3*dotRadius/2,paint);
        }
        else{
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(PxToDp(dotRadius/8,getContext()));
            this.canvas.drawCircle(x1+this.avatar.getWidth()/2+Game.surfaceViewWidth/2,y1+this.avatar.getHeight()/2,(this.avatar.getHeight()/2)+3*dotRadius/2,paint);
        }
    }

    public void autoPlay(){
        if(Game.type.equals("Multi Player") || (Game.type.equals("Single Player") && !first_player_play)){
            return;
        }
        boolean flag = false;
        for (int i = 0; i < squareGrid.length; i++) {
            if (squareGrid[i] == 3) {
                flag = true;
                drawSquareLine(i,false);
                break;
            }
        }
        if (!flag) {
            ArrayList<Integer> randomSideIndex = new ArrayList<>();
            for (int i = 0; i < squareGrid.length; i++) {
                if (squareGrid[i] < 3) {
                    randomSideIndex.add(i);
                }
            }
            if (randomSideIndex.size() != 0) {
                int idx = getRandom(0, 100) % (randomSideIndex.size());
                int randomSquare = randomSideIndex.get(idx);
                drawSquareLine(randomSquare, true);
                first_player_play = !first_player_play;
            }
        }
    }

    public void drawSquareLine(int i,boolean random){
        int sqA = (i / SQUARES) * GRID_SIZE + (i % SQUARES);
        int sqB = sqA + 1;
        int sqC = sqB + GRID_SIZE;
        int sqD = sqA + GRID_SIZE;
        int color = Color.rgb(85,255,0);

        if(random){
            ArrayList<Integer> randomSideIndex = new ArrayList<>();
            if (!alreadyAdded(sqA, sqB)) {
                randomSideIndex.add(0);
            }if (!alreadyAdded(sqB, sqC)) {
                randomSideIndex.add(1);
            }if (!alreadyAdded(sqA, sqD)) {
                randomSideIndex.add(2);
            }if (!alreadyAdded(sqD, sqC)) {
                randomSideIndex.add(3);
            }
            int idx = getRandom(0,100)%(randomSideIndex.size());
            int randomLine = randomSideIndex.get(idx);
            if(randomLine == 0){
                lineMap.add(new LineMap(sqA, sqB, color));
                squareComplete(sqA, sqB, color);
            }
            else if(randomLine == 1){
                lineMap.add(new LineMap(sqB, sqC, color));
                squareComplete(sqB, sqC, color);
            }
            else if(randomLine == 2){
                lineMap.add(new LineMap(sqA, sqD, color));
                squareComplete(sqA, sqD, color);
            }
            else{
                lineMap.add(new LineMap(sqD, sqC, color));
                squareComplete(sqD, sqC, color);
            }
        }
        else {
            if (!alreadyAdded(sqA, sqB)) {
                lineMap.add(new LineMap(sqA, sqB, color));
                squareComplete(sqA, sqB, color);
            } else if (!alreadyAdded(sqB, sqC)) {
                lineMap.add(new LineMap(sqB, sqC, color));
                squareComplete(sqB, sqC, color);
            } else if (!alreadyAdded(sqA, sqD)) {
                lineMap.add(new LineMap(sqA, sqD, color));
                squareComplete(sqA, sqD, color);
            } else if (!alreadyAdded(sqD, sqC)) {
                lineMap.add(new LineMap(sqD, sqC, color));
                squareComplete(sqD, sqC, color);
            }
        }
    }

    public void drawRectangles(){
        gameOn = false;
        for(int i=0;i<squareGrid.length;i++){
            if(squareGrid[i] == 4){
                int fromIndex1 = (i/SQUARES)*GRID_SIZE+(i%SQUARES);
                int x1 = fromIndex1%GRID_SIZE;
                int y1 = fromIndex1/GRID_SIZE;
                int toIndex2 = (fromIndex1+1)+GRID_SIZE;
                int x2 = toIndex2%GRID_SIZE;
                int y2 = toIndex2/GRID_SIZE;

                float left = dots[x1][y1].getIdxX()+dotRadius;
                float top = dots[x1][y1].getIdxY()+dotRadius;
                float right = dots[x2][y2].getIdxX()-dotRadius;
                float bottom = dots[x2][y2].getIdxY()-dotRadius;

                Paint paint = new Paint();
                paint.setColor(squareColor[i]);
                RectF rect = new RectF(left,top,right,bottom);
                this.canvas.drawRect(rect,paint);
            }
            else{
                gameOn = true;
            }
        }
        if(!gameOn){
            Intent intent = new Intent(getContext(),GameOver.class);
            intent.putExtra("GameType",Game.type);
            intent.putExtra("Player1",Game.Player1);
            intent.putExtra("Player2",Game.Player2);
            intent.putExtra("Score1",firstPlayerScore+"");
            intent.putExtra("Score2",secondPlayerScore+"");
            if(firstPlayerScore>secondPlayerScore){
                intent.putExtra("winner",Game.Player1);
            }
            else if(firstPlayerScore<secondPlayerScore){
                intent.putExtra("winner",Game.Player2);
            }
            else{
                intent.putExtra("winner","It's a tie");
            }
            Scores scores = new Scores(System.currentTimeMillis(),Game.Player1,Game.Player2,firstPlayerScore,secondPlayerScore);
            List<Scores> scoresList = new ArrayList<>();
            scoresList.add(scores);
            if(Game.type.equals("Single Player")){
                Log.d("sdfa","Hey here");
                sharedPreference.addScore(getContext(),scores,"SINGLE_SCORE","Single_Score");
            }
            else if(Game.type.equals("Multi Player")){
                Log.d("sdfa","Hey there");
                sharedPreference.addScore(getContext(),scores,"MULTI_SCORE","Multi_Score");
            }
            else{
            }
            getContext().startActivity(intent);
            ((Activity)getContext()).finish();
        }
    }

    public void drawLines() throws InterruptedException {
        int i;
        for(i=0;i<lineMap.size();i++){
            LineMap line = lineMap.get(i);

            int fromIndex = line.getFromIdx();
            int toIndex = line.getToIdx();
            int color = line.getColor();

            int x1 = fromIndex%GRID_SIZE;
            int y1 = fromIndex/GRID_SIZE;

            int x2 = toIndex%GRID_SIZE;
            int y2 = toIndex/GRID_SIZE;

            Paint paint = new Paint();
            paint.setColor(color);
            paint.setStrokeWidth(dotRadius);
            if(i==lineMap.size()-1 && color == Color.rgb(85,255,0) && Game.type.equals("Single Player")){
                sleep(500L);
            }
            this.canvas.drawLine(dots[x1][y1].getIdxX(),dots[x1][y1].getIdxY(),dots[x2][y2].getIdxX(),dots[x2][y2].getIdxY(),paint);
        }
    }

    public void initializeGameBoard(int x,int y,int offset){
        int idxX,idxY;
        for(int i=0;i<GRID_SIZE;i++){
            idxX = x + (i*offset);
            X_IDX[i] = idxX;
            for(int j=0;j<GRID_SIZE;j++){
                idxY = y + (j*offset);
                Y_IDX[j] = idxY;
                dots[i][j] = new Dots(idxX,idxY,Color.WHITE);
            }
        }
        for(int j=0;j<SQUARES*SQUARES-1;j++){
            squareGrid[j] = 0;
            squareColor[j] = Color.WHITE;
        }
    }

    public void drawDots(){
        int i,j;
        for(i=0;i<GRID_SIZE;i++){
            for(j=0;j<GRID_SIZE;j++){
                Paint paint = new Paint();
                paint.setColor(dots[i][j].getColor());
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);
                this.canvas.drawCircle((float)(dots[i][j].getIdxX()), (float)(dots[i][j].getIdxY()),dotRadius, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            drawLineOnTouch((int)event.getX(),(int)event.getY());
        }
        return true;
    }

    public void drawLineOnTouch(int x,int y){
        if(Game.type.equals("Single Player") && first_player_play)
            return;

        int idxX,idxY;
        int finalX = initialX+(GRID_SIZE-1)*gameBoard_Offset;
        int finalY = initialY+(GRID_SIZE-1)*gameBoard_Offset;

        if(x>=initialX-dotRadius&&x<=finalX+dotRadius){
            if(y>=initialY-dotRadius && y<=finalY+dotRadius){
                if(x>=initialX-dotRadius && x<=initialX+dotRadius){
                    x = initialX;
                }
                if(x<=finalX+dotRadius && x>=finalX-dotRadius){
                    x = finalX;
                }
                if(y>=initialY-dotRadius&&y<=initialY+dotRadius){
                    y = initialY;
                }
                if(y>=finalY-dotRadius&&y<=finalY+dotRadius){
                    y = finalY;
                }
                idxX = (x-initialX)/gameBoard_Offset;
                idxY = (y-initialY)/gameBoard_Offset;
                if(x>=X_IDX[idxX]+dotRadius && x<=X_IDX[idxX+1]-dotRadius){
                    if(y>=Y_IDX[idxY]-2*dotRadius && y<=Y_IDX[idxY]+2*dotRadius){
                        int lineIdx1 = idxY*GRID_SIZE+idxX;
                        int lineIdx2 = lineIdx1 + 1;
                        int color = Color.rgb(85,255,0);
                        if(!first_player_play){
                            color = Color.RED;
                        }
                        if(!alreadyAdded(lineIdx1,lineIdx2)){
                            lineMap.add(new LineMap(lineIdx1,lineIdx2,color));
                            if(squareComplete(lineIdx1,lineIdx2,color)){
                            }
                            else{
                                first_player_play = !first_player_play;
                            }
                        }
                    }
                }
                else if(y>=Y_IDX[idxY]+dotRadius && y<=Y_IDX[idxY+1]-dotRadius){
                    if(x>=X_IDX[idxX]-2*dotRadius && x<=X_IDX[idxX]+2*dotRadius){
                        int lineIdx1 = idxY*GRID_SIZE+idxX;
                        int lineIdx2 = lineIdx1+GRID_SIZE;
                        int color = Color.rgb(85,255,0);
                        if(!first_player_play){
                            color = Color.RED;
                        }
                        if(!alreadyAdded(lineIdx1,lineIdx2)){
                            lineMap.add(new LineMap(lineIdx1,lineIdx2,color));
                            if(squareComplete(lineIdx1,lineIdx2,color)){
                            }
                            else{
                                first_player_play = !first_player_play;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean squareComplete(int index1,int index2,int color){
        int diff = index2-index1;
        if(diff == 1){
            boolean flag1=false,flag2=false;
            if(index1 - GRID_SIZE >= 0){
                int upIndex = index1 - GRID_SIZE;
                int sq1 = (upIndex/(GRID_SIZE))*SQUARES+(upIndex%(GRID_SIZE));
                squareGrid[sq1]++;
                if(squareGrid[sq1] == 4){
                    flag1 = true;
                    squareColor[sq1] = color;
                    if(!first_player_play){
                        secondPlayerScore++;
                    }
                    else{
                        firstPlayerScore++;
                    }
                }
            }
            if(index1+GRID_SIZE < (GRID_SIZE*GRID_SIZE)){
                int upIndex = index1;
                int sq1 = (upIndex/(GRID_SIZE))*SQUARES+(upIndex%(GRID_SIZE));
                squareGrid[sq1]++;
                if(squareGrid[sq1] == 4){
                    flag2 = true;
                    squareColor[sq1] = color;
                    if(!first_player_play){
                        secondPlayerScore++;
                    }
                    else{
                        firstPlayerScore++;
                    }
                }
            }
            if(flag1 || flag2)
                return true;
        }else if(diff == GRID_SIZE){
            boolean flag1=false,flag2=false;
            if(index1%GRID_SIZE != 0){
                int leftIndex = index1 - 1;
                int sq1 = (leftIndex/(GRID_SIZE))*SQUARES+(leftIndex%(GRID_SIZE));
                squareGrid[sq1]++;
                if(squareGrid[sq1] == 4) {
                    flag1 = true;
                    squareColor[sq1] = color;
                    if(!first_player_play){
                        secondPlayerScore++;
                    }
                    else{
                        firstPlayerScore++;
                    }
                }
            }
            if(index1%GRID_SIZE != GRID_SIZE-1){
                int rightIndex = index1;
                int sq1 = (rightIndex/(GRID_SIZE))*SQUARES+(rightIndex%(GRID_SIZE));
                squareGrid[sq1]++;
                if(squareGrid[sq1] == 4){
                    flag2 = true;
                    squareColor[sq1] = color;
                    if(!first_player_play){
                        secondPlayerScore++;
                    }
                    else{
                        firstPlayerScore++;
                    }
                }
            }
            if(flag1 || flag2)
                return true;
        }
        return false;
    }

    public boolean alreadyAdded(int index1,int index2){
        int i;
        for(i=0;i<lineMap.size();i++){
            LineMap line = lineMap.get(i);
            if(line.getFromIdx() == index1 && line.getToIdx() == index2)
                return true;
        }
        return false;
    }

    public int getRandomColor(){
        return Color.rgb(getRandom(1,255),getRandom(1,255),getRandom(1,255));
    }

    public int getRandom(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public void drawBackGround(){
        this.canvas.drawBitmap(this.backgroundImage, 0.0f, 0.0f, new Paint());
    }

    public Bitmap getScaledImage(Bitmap image) {
        return Bitmap.createScaledBitmap(image, getScaledWidth(image), getScaledHeight(image), true);
    }
    public int getScaledHeight(Bitmap image) {
        return Math.round(((float) image.getHeight()) / getScale(image));
    }

    public int getScaledWidth(Bitmap image) {
        return Math.round(((float) image.getWidth()) / getScale(image));
    }

    public float getScale(Bitmap image) {
        return ((float) image.getHeight()) / ((float) Game.surfaceViewHeight);
    }

    public static float PxToDp(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,resources.getDisplayMetrics());

    }
}
