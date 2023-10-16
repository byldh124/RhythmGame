package com.moondroid.rhythmgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moondroid.rhythmgame.dialog.RankingDialog;
import com.moondroid.rhythmgame.dialog.SettingDialog;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void clickStart(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void clickSetting(View view) {
        SettingDialog dialog = new SettingDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void clickRanking(View view) {
        RankingDialog dialog = new RankingDialog(this);
        dialog.setCancelable(false);
        dialog.show();
    }
}