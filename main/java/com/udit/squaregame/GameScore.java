package com.udit.squaregame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameScore extends Activity {

    private ListView firstView, secondView;
    MyAdapter myAdapter1,myAdapter2;
    TextView tv1,tv2;
    Typeface custom_font;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_score);
        firstView = (ListView) findViewById(R.id.firstListView);
        secondView = (ListView) findViewById(R.id.secondListView);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/post_rock.ttf");
        tv1 = (TextView)findViewById(R.id.header_single);
        tv2 = (TextView)findViewById(R.id.header_multi);
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);

        ArrayList<Scores> firstArrayList = new ArrayList<>();
        firstArrayList = (new SharedPreference()).getScores(getApplicationContext(),"SINGLE_SCORE","Single_Score");
        ArrayList<Scores> secondArrayList = new ArrayList<>();
        secondArrayList = (new SharedPreference()).getScores(getApplicationContext(),"MULTI_SCORE","Multi_Score");


        myAdapter1 = new MyAdapter(getApplicationContext(), firstArrayList);
        firstView.setAdapter(myAdapter1);

        myAdapter2 = new MyAdapter(getApplicationContext(), secondArrayList);
        secondView.setAdapter(myAdapter2);
    }

    public class MyAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Scores> arrayList;

        public MyAdapter(Context context, ArrayList<Scores> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            if (arrayList == null)
                return 0;
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.content_listview_score, null);
                holder.scoreView = (TextView) convertView.findViewById(R.id.scoreTextView);
                holder.dateView = (TextView) convertView.findViewById(R.id.dateTextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM");
            String dateString = formatter.format(new Date(arrayList.get(position).getTime()));
            String score = arrayList.get(position).getFirstPlayer()+" ("+arrayList.get(position).getFirstScore()+")"+
                    " - "+ arrayList.get(position).getSecondPlayer()+" ("+arrayList.get(position).getSecondScore()+")";
            holder.scoreView.setText(score);
            holder.scoreView.setTypeface(custom_font);
            holder.dateView.setText(dateString);
            holder.dateView.setTypeface(custom_font);
            return convertView;
        }

        public class ViewHolder {
            TextView scoreView;
            TextView dateView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, Menu.class);
        startActivity(mainIntent);
        finish();
    }
}