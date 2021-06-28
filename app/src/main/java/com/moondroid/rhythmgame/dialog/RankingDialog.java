package com.moondroid.rhythmgame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.moondroid.rhythmgame.R;
import com.moondroid.rhythmgame.RankingVO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class RankingDialog extends Dialog {
    private Context context;

    RecyclerView recyclerView;
    ImageView ivClose;

    ArrayList<RankingVO> items;
    RankingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ranking);

        ivClose = findViewById(R.id.iv_close_ranking);
        ivClose.setClickable(true);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingDialog.this.dismiss();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        items = new ArrayList<>();
        adapter = new RankingAdapter(context, items);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference("rank").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RankingVO rankingVO = ds.getValue(RankingVO.class);
                    items.add(rankingVO);
                }

                Collections.sort(items, new Comparator<RankingVO>() {
                    @Override
                    public int compare(RankingVO o1, RankingVO o2) {
                        if (o1.getScore() > o2.getScore()) {
                            return -1;
                        } else if (o1.getScore() < o2.getScore()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }

    public RankingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.VH> {

        Context context;
        ArrayList<RankingVO> items;

        public RankingAdapter(Context context, ArrayList<RankingVO> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @NotNull
        @Override
        public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(context).inflate(R.layout.layout_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RankingDialog.RankingAdapter.VH holder, int position) {
            holder.rank.setText(String.valueOf(position + 1));
            holder.nick.setText(items.get(position).getName());
            holder.score.setText(String.valueOf(items.get(position).getScore()));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class VH extends RecyclerView.ViewHolder {

            TextView rank, nick, score;

            public VH(@NonNull @NotNull View itemView) {
                super(itemView);

                rank = itemView.findViewById(R.id.tv_rank);
                nick = itemView.findViewById(R.id.tv_nick);
                score = itemView.findViewById(R.id.tv_score_rank);
            }
        }
    }
}
