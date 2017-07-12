package com.udit.squaregame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 11-Jul-17.
 */

public class SharedPreference {

    public SharedPreference() {
        super();
    }

    public void saveScores(Context context, List<Scores> score, String PREFS_NAME, String SCORES) {
        SharedPreferences scoresPref;
        SharedPreferences.Editor editor;
        scoresPref = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);
        editor = scoresPref.edit();

        Collections.sort(score, new MyComparator1());

        if(score.size()==11){
            score.remove(10);
        }

        Gson gson = new Gson();
        String jsonScores = gson.toJson(score);
        editor.putString(SCORES, jsonScores);

        editor.commit();
    }

    public void addScore(Context context, Scores gameScore, String PREFS_NAME,String SCORES) {
        List<Scores> scores = getScores(context,PREFS_NAME,SCORES);
        if (scores == null)
            scores = new ArrayList<Scores>();
        if(scores.size()<11) {
            scores.add(gameScore);
            saveScores(context, scores,PREFS_NAME, SCORES);
        }
    }

    public ArrayList<Scores> getScores(Context context,String PREFS_NAME,String SCORES) {
        SharedPreferences settings;
        List<Scores> scores;

        settings = context.getSharedPreferences(PREFS_NAME,
                MODE_PRIVATE);

        if (settings.contains(SCORES)) {
            String jsonScores = settings.getString(SCORES, null);
            Gson gson = new Gson();
            Scores[] gameScores = gson.fromJson(jsonScores,
                    Scores[].class);

            scores = Arrays.asList(gameScores);
            scores = new ArrayList<Scores>(scores);
        } else {
            return null;
        }
        return (ArrayList<Scores>) scores;
    }

    class MyComparator1 implements Comparator<Scores> {

        public int compareValue(long x, long y) {
            return x > y ? -1
                    : x < y ? 1
                    : 0;
        }

        @Override
        public int compare(Scores scores, Scores t1) {
            return compareValue(scores.getTime(),t1.getTime());
        }
    }
}