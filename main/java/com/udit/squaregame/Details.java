package com.udit.squaregame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends Activity {

    TextView tv1,tv2,tv3,tv4;
    EditText et1,et2;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        final String gameType = getIntent().getStringExtra("GameType");
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/post_rock.ttf");
        TextView gameTypeView = (TextView)findViewById(R.id.game_type);
        gameTypeView.setText(gameType);
        gameTypeView.setTypeface(custom_font);

        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        tv3 = (TextView)findViewById(R.id.tv3);
        tv4 = (TextView)findViewById(R.id.tv4);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        playButton = (Button)findViewById(R.id.start_game);

        tv1.setText("Player 1");
        tv3.setText("Player 2");
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        tv3.setTypeface(custom_font);
        tv4.setTypeface(custom_font);
        et1.setTypeface(custom_font);
        et2.setTypeface(custom_font);
        playButton.setTypeface(custom_font);

        if(gameType.equals("Single Player")){
            tv2.setVisibility(View.VISIBLE);
            et1.setVisibility(View.GONE);
        }
        else if(gameType.equals("Multi Player")){
            et1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.GONE);
        }
        else{
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gameType.equals("Single Player")){
                    if(et2.getText().length()==0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter player name", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM , 0, 0);
                        toast.show();
                    }
                    else{
                        playButton.setVisibility(View.GONE);
                        tv4.setVisibility(View.VISIBLE);
                        final Intent intent = new Intent(Details.this,Game.class);
                        intent.putExtra("GameType",gameType);
                        intent.putExtra("Player 1","BOT");
                        intent.putExtra("Player 2",et2.getText().toString().toUpperCase());
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 1000L);
                    }
                }
                else if(gameType.equals("Multi Player")){
                    if(et1.getText().length() == 0  || et2.getText().length() == 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter player names", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM , 0, 0);
                        toast.show();
                    }else{
                        playButton.setVisibility(View.GONE);
                        tv4.setVisibility(View.VISIBLE);
                        final Intent intent = new Intent(Details.this,Game.class);
                        intent.putExtra("GameType",gameType);
                        intent.putExtra("Player 1",et1.getText().toString().toUpperCase());
                        intent.putExtra("Player 2",et2.getText().toString().toUpperCase());
                        Handler  mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        }, 1000L);
                    }
                }
                else{
                }
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(Details.this, Menu.class);
        startActivity(intent);
        finish();
    }
}
