package com.udit.squaregame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class Tutorial extends Activity {

    TextView tv1,tv2;
    Typeface custom_font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/post_rock.ttf");

        tv1 = (TextView)findViewById(R.id.header_tutorial);
        tv2 = (TextView)findViewById(R.id.content_tutorial);

        tv1.setText("How to play??");
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);

        tv2.setText("* Play with the Dot-BOT in single player mode."
                + System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"* And with your mate in multi player mode."
                + System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"* Connect dots and make squares by tapping in the middle of them."
                + System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"* Player who completes the square will get that one on his/her name."
                +System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"*Player with maximum square wins the game."
                +System.getProperty("line.separator")
                +System.getProperty("line.separator")
                +"Happy Square and Dots."
        );
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, Menu.class);
        startActivity(mainIntent);
        finish();
    }
}
