package com.example.project_unigo.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_unigo.R;
import com.example.project_unigo.model.RuleModel;
import com.example.project_unigo.view.RuleDetailActivity;

import java.util.List;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    private List<RuleModel> mList;

    public RuleAdapter(List<RuleModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rule, parent, false);
        return new RuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        RuleModel rule = mList.get(position);

        holder.tvTitle.setText(rule.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RuleDetailActivity.class);
            intent.putExtra("rule_data", rule);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class RuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRuleTitle);
        }
    }
}
