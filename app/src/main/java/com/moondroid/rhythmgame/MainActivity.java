package com.moondroid.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.Dimension;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.moondroid.rhythmgame.dialog.FinishDialog;

import java.util.Queue;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[] cardViews;

    ConstraintLayout container;
    Animation[] animations;
    TextView[] textViews;

    int i;
    int combo = 0;
    int score = 0;
    boolean[] isClicked;
    Random rnd = new Random();
    int stageNum = 0;
    int clear, miss = 0;

    int colorClear, colorMiss;

    int shape;
    int maximumCombo = 0;

    MyThread myThread;
    TextView tvClear, tvMiss;
    TextView tvCntDown;
    TextView tvCombo, tvScore;

    String theme;
    int grade = 1;
    int speed = 0;
    int comboAdd = 100;

    boolean isRun = true;

    Typeface typeface;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        theme = sharedPreferences.getString("theme", "circle");
        grade = sharedPreferences.getInt("grade", 1);

        setGame();


        container = findViewById(R.id.container_main);

        tvClear = findViewById(R.id.tv_clear);
        tvMiss = findViewById(R.id.tv_miss);
        tvClear.setText("Clear : " + clear);
        tvMiss.setText("Miss: " + miss);
        tvCntDown = findViewById(R.id.tv_count_down);
        tvCombo = findViewById(R.id.tv_combo);
        tvScore = findViewById(R.id.tv_score);

        animations = new Animation[stageNum];
        cardViews = new ImageView[stageNum];
        textViews = new TextView[stageNum];
        isClicked = new boolean[stageNum];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.font_ex);
        }

        startThread();
    }

    @Override
    public void onClick(View v) {
        v.clearAnimation();
        container.removeView(v);
        int tag = (int) v.getTag();
        isClicked[tag] = true;
        container.addView(textViews[tag]);
        clear++;
        tvClear.setText("Clear : " + clear);
        combo++;
        if (combo > maximumCombo) maximumCombo = combo;
        score += (combo * comboAdd);
        tvCombo.setText("Combo\n" + combo);
        tvScore.setText("Score\n" + score);

        textViews[tag].setText("+" + (combo * comboAdd));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                container.removeView(textViews[tag]);
            }
        }, 300);
    }

    float x = 0f;
    float y = 0f;

    class MyThread extends Thread {
        @Override
        public void run() {
            while (isRun) {
                for (int i = 3; i > 0; i--) {
                    int k = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCntDown.setText("" + k);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCntDown.setVisibility(View.GONE);
                    }
                });
                for (i = 0; i < cardViews.length + 3; i++) {
                    if (i < cardViews.length) {
                        x = (x + ((rnd.nextInt(2500) + 5000) / 10000f)) % 1f;
                        y = (y + ((rnd.nextInt(2500) + 5000) / 10000f)) % 1f;
                        cardViews[i] = new ImageView(MainActivity.this);
                        cardViews[i].setTag(i);
                        textViews[i] = new TextView(MainActivity.this);
                        animations[i] = AnimationUtils.loadAnimation(MainActivity.this, R.anim.dot_animation);
                        isClicked[i] = false;
                        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
                        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                        layoutParams.matchConstraintPercentWidth = (float) 0.2;
                        layoutParams.dimensionRatio = "1";
                        layoutParams.horizontalBias = x;
                        layoutParams.verticalBias = y;

                        cardViews[i].setLayoutParams(layoutParams);
                        textViews[i].setLayoutParams(layoutParams);
                        textViews[i].setTextColor(colorClear);
                        textViews[i].setGravity(Gravity.CENTER);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && typeface != null) {
                            textViews[i].setTypeface(typeface);
                        }
                        cardViews[i].setClickable(true);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (i < cardViews.length) {
                                cardViews[i].setImageResource(shape);
                                cardViews[i].setAnimation(animations[i]);
                                container.addView(cardViews[i]);
                            }
                            if (i > 2 && !isClicked[i - 3]) {
                                if (cardViews[i - 3] != null) {
                                    cardViews[i - 3].clearAnimation();
                                    container.removeView(cardViews[i - 3]);
                                    cardViews[i - 3] = null;
                                }
                                final int tag = i - 3;
                                container.addView(textViews[tag]);
                                textViews[tag].setTextColor(colorMiss);
                                textViews[tag].setText("miss");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        container.removeView(textViews[tag]);
                                    }
                                }, 300);
                                miss++;
                                tvMiss.setText("Miss : " + miss);
                                combo = 0;
                            }
                        }
                    });

                    if (i < cardViews.length) {
                        cardViews[i].setOnClickListener(MainActivity.this);
                    }
                    int k = rnd.nextInt(200);
                    if (i == cardViews.length) k = 500;
                    try {
                        Thread.sleep(speed + k - i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == cardViews.length + 1) {
                        stopThread2();
                    }
                }
            }
        }

        public void stopThread() {
            isRun = false;
        }

        public void stopThread2() {
            isRun = false;
            if (!MainActivity.this.isFinishing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FinishDialog dialog = new FinishDialog(MainActivity.this, clear, score, maximumCombo);
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                });
            }
        }
    }


    @Override
    protected void onPause() {
        myThread.stopThread();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myThread.stopThread();
        super.onDestroy();

    }

    public void setGame() {
        shape = R.drawable.ic_baseline_brightness_1_24;
        colorClear = 0xff3fc1c9;
        colorMiss = 0xfffc5185;
        stageNum = 50;
        speed = 500;
        comboAdd = 100;
        switch (theme) {
            case "circle":
                break;
            case "star":
                shape = R.drawable.ic_baseline_star_24;
                colorClear = 0xfffce38a;
                colorMiss = 0xff3fc1c9;
                break;
            case "heart":
                shape = R.drawable.ic_baseline_favorite_24;
                colorClear = 0xfffc5185;
                colorMiss = 0xff364f6b;
                break;
        }

        switch (grade) {
            case 1:
                break;
            case 2:
                speed = 450;
                stageNum = 65;
                comboAdd = 150;
                break;
            case 3:
                speed = 400;
                stageNum = 80;
                comboAdd = 200;
                break;
            case 4:
                speed = 350;
                stageNum = 90;
                comboAdd = 250;
                break;
            case 5:
                speed = 330;
                stageNum = 100;
                comboAdd = 300;
                break;
        }
    }

    public void startThread() {
        tvCntDown.setVisibility(View.VISIBLE);
        isRun = true;
        i = 0;
        miss = 0;
        clear = 0;
        combo = 0;
        score = 0;

        tvClear.setText("Clear : " + clear);
        tvCombo.setText("Combo\n" + combo);
        tvMiss.setText("Miss : " + miss);
        tvScore.setText("Score\n" + score);

        myThread = new MyThread();
        myThread.start();
    }
}