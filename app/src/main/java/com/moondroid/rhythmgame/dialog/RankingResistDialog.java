package com.moondroid.rhythmgame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.rhythmgame.R;
import com.moondroid.rhythmgame.RankingVO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RankingResistDialog extends Dialog {

    private Context context;

    Button btnResist, btnClose;
    EditText etName;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ranking_resist);

        btnResist = findViewById(R.id.btn_resist_true);
        btnClose = findViewById(R.id.btn_resist_false);

        etName = findViewById(R.id.et_nick);

        btnResist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String name = etName.getText().toString();
                if (name == null || name.equals("")){
                    Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference("rank/").push().setValue(new RankingVO(name, score)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        RankingResistDialog.this.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingResistDialog.this.dismiss();
            }
        });
    }

    public RankingResistDialog(@NonNull Context context, int score) {
        super(context);
        this.context = context;
        this.score = score;
    }


}
