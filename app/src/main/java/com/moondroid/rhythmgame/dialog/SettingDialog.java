package com.moondroid.rhythmgame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.moondroid.rhythmgame.R;

public class SettingDialog extends Dialog implements View.OnClickListener {
    RadioGroup rgTheme;
    RadioGroup rgGrade;
    Animation animation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView ivClose;
    Button save;

    String theme = "circle";
    int grade = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);

        rgTheme = findViewById(R.id.rg_theme);
        rgGrade = findViewById(R.id.rg_grade);
        ivClose = findViewById(R.id.iv_close);
        save = findViewById(R.id.btn_save_setting);
        save.setOnClickListener(this);

        ivClose.setClickable(true);
        ivClose.setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        theme = sharedPreferences.getString("theme", "circle");
        grade = sharedPreferences.getInt("grade", 1);

        setDefault();

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.dot_animation);
        animation.setDuration(500);

        rgTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                radioButton.startAnimation(animation);

                switch (checkedId) {
                    case R.id.rb_theme_01:
                        theme = "circle";
                        break;
                    case R.id.rb_theme_02:
                        theme = "star";
                        break;
                    case R.id.rb_theme_03:
                        theme = "heart";
                        break;
                }
                editor.commit();
            }
        });

        rgGrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_grade_01:
                        grade = 1;
                        break;
                    case R.id.rb_grade_02:
                        grade = 2;
                        break;
                    case R.id.rb_grade_03:
                        grade = 3;
                        break;
                    case R.id.rb_grade_04:
                        grade = 4;
                        break;
                    case R.id.rb_grade_05:
                        grade = 5;
                        break;
                }
            }
        });
    }

    public SettingDialog(@NonNull Context context) {
        super(context);
    }

    public void setDefault() {
        int id = R.id.rb_theme_01;

        switch (theme) {
            case "circle":
                id = R.id.rb_theme_01;
                break;
            case "star":
                id = R.id.rb_theme_02;
                break;
            case "heart":
                id = R.id.rb_theme_03;
                break;
        }

        RadioButton radioButton = findViewById(id);
        radioButton.setChecked(true);

        switch (grade){
            case 1:
                id = R.id.rb_grade_01;
                break;
            case 2:
                id = R.id.rb_grade_02;
                break;
            case 3:
                id = R.id.rb_grade_03;
                break;
            case 4:
                id = R.id.rb_grade_04;
                break;
            case 5:
                id = R.id.rb_grade_05;
                break;
        }
        radioButton = findViewById(id);
        radioButton.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save_setting) {
            editor.putString("theme", theme);
            editor.putInt("grade", grade);
            editor.commit();
        }
        dismiss();
    }
}
