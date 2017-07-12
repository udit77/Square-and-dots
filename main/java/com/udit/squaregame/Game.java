package com.udit.squaregame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Game extends Activity {

    int winWidth, winHeight;
    public static int surfaceViewWidth,surfaceViewHeight;
    GameSurfaceView gameSurfaceView;
    public static String type,Player1,Player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initWindow();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        type = getIntent().getStringExtra("GameType").toString();
        Player1 = getIntent().getStringExtra("Player 1").toString();
        Player2 = getIntent().getStringExtra("Player 2").toString();
        gameSurfaceView = new GameSurfaceView(this);
        LinearLayout surfaceView = (LinearLayout) findViewById(R.id.surface_view);
        surfaceView.addView(gameSurfaceView);
    }

    public void initWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.winWidth = displayMetrics.widthPixels;
        this.winHeight = displayMetrics.heightPixels;
        surfaceViewHeight = this.winHeight;
        surfaceViewWidth = this.winWidth;
    }

    protected void onResume() {
        super.onResume();
        gameSurfaceView.onResumeMySurfaceView();
    }

    protected void onPause() {
        super.onPause();
        gameSurfaceView.onPauseMySurfaceView();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Game.this, Menu.class);
        startActivity(intent);
        finish();
    }
}