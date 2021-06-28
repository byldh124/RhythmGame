package com.moondroid.rhythmgame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.moondroid.rhythmgame.MainActivity;
import com.moondroid.rhythmgame.R;

public class FinishDialog extends Dialog implements View.OnClickListener {

    private Context context;

    int clear;
    int score;
    int maximumCombo;

    Button btnRetry, btnResistRank, btnGoToMain;

    TextView tvClear, tvCombo, tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_finish);

        tvClear = findViewById(R.id.tv_clear_finish);
        tvCombo = findViewById(R.id.tv_combo_finish);
        tvScore = findViewById(R.id.tv_score_finish);

        tvClear.setText("clear : " + clear);
        tvCombo.setText("최대 콤보 : " + maximumCombo);
        tvScore.setText("점수 : " + score);

        btnRetry = findViewById(R.id.btn_retry);
        btnGoToMain = findViewById(R.id.btn_go_main);
        btnResistRank = findViewById(R.id.btn_rankin_resist);

        btnRetry.setOnClickListener(this);
        btnGoToMain.setOnClickListener(this);
        btnResistRank.setOnClickListener(this);
    }

    public FinishDialog(@NonNull Context context, int clear, int score, int maximumCombo) {
        super(context);
        this.context = context;
        this.clear = clear;
        this.score = score;
        this.maximumCombo = maximumCombo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_retry:
                ((MainActivity)context).startThread();
                dismiss();
                break;
            case R.id.btn_go_main:
                ((MainActivity)context).finish();
                dismiss();
                break;
            case R.id.btn_rankin_resist:
                RankingResistDialog dialog = new RankingResistDialog(context, score);
                dialog.setCancelable(false);
                v.setEnabled(false);
                dialog.show();
                break;
        }
    }




}
