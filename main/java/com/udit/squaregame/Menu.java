package com.udit.squaregame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Menu extends Activity {

    TextView tv1,tv2,tv3,tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/post_rock.ttf");
        ImageView iv_background = (ImageView) findViewById(R.id.background);
        iv_background.setImageResource(R.drawable.background);

        tv1 = (TextView)findViewById(R.id.single_player);
        tv2 = (TextView)findViewById(R.id.multi_player);
        tv3 = (TextView)findViewById(R.id.scores);
        tv4 = (TextView)findViewById(R.id.tutorial);

        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        tv3.setTypeface(custom_font);
        tv4.setTypeface(custom_font);


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Details.class).putExtra("GameType", tv1.getText());
                startActivity(intent);
                finish();
            }
        });


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Details.class).putExtra("GameType", tv2.getText());
                startActivity(intent);
                finish();
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,GameScore.class);
                startActivity(intent);
                finish();
            }
        });


        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Tutorial.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
