package com.udit.squaregame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOver extends Activity {

    TextView tv1,tv2,tv3,tv4,tv5,header,final_score;
    Button playAgain,menu;
    String gameType,winner,player1,player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/post_rock.ttf");

        final_score = (TextView)findViewById(R.id.final_score);
        playAgain = (Button)findViewById(R.id.replay);
        menu = (Button)findViewById(R.id.goMenu);
        winner = getIntent().getStringExtra("winner").toString();
        gameType = getIntent().getStringExtra("GameType");
        player1 = getIntent().getStringExtra("Player1").toString();
        player2 = getIntent().getStringExtra("Player2").toString();

        header = (TextView)findViewById(R.id.header);
        tv1 = (TextView)findViewById(R.id.winner);
        tv2 = (TextView)findViewById(R.id.player1);
        tv3 = (TextView)findViewById(R.id.player2);
        tv4 = (TextView)findViewById(R.id.firstScore);
        tv5 = (TextView)findViewById(R.id.secondScore);

        if(winner.equals("It's a tie")){
            header.setText("");
        }
        else{
            header.setText("Winner!!");
        }
        tv1.setText(winner);
        tv2.setText(player1);
        tv3.setText(player2);
        tv4.setText(getIntent().getStringExtra("Score1").toString());
        tv5.setText(getIntent().getStringExtra("Score2").toString());

        playAgain.setTypeface(custom_font);
        final_score.setTypeface(custom_font);
        menu.setTypeface(custom_font);
        header.setTypeface(custom_font);
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        tv3.setTypeface(custom_font);
        tv4.setTypeface(custom_font);
        tv5.setTypeface(custom_font);

        if(winner.equals(player1)){
            tv1.setTextColor(getResources().getColor(R.color.menuColor_1));
        }
        else if(winner.equals(player2)){
            tv1.setTextColor(getResources().getColor(R.color.menuColor_2));
        }
        else{
            tv1.setTextColor(Color.WHITE);
        }

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this,Game.class);
                intent.putExtra("GameType",gameType);
                intent.putExtra("Player 1",tv2.getText().toString().toUpperCase());
                intent.putExtra("Player 2",tv3.getText().toString().toUpperCase());
                startActivity(intent);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this,Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
