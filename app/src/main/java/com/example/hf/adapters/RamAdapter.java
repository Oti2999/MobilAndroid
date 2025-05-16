package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.models.RamPart;

import java.util.List;

public class RamAdapter extends RecyclerView.Adapter<RamAdapter.RamViewHolder> {

    public interface OnRamClickListener {
        void onRamClick(RamPart ramPart);
    }

    private final List<RamPart> ramList;
    private final OnRamClickListener listener;

    public RamAdapter(List<RamPart> ramList, OnRamClickListener listener) {
        this.ramList = ramList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ram_part_item, parent, false);
        return new RamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RamViewHolder holder, int position) {
        RamPart ramPart = ramList.get(position);
        holder.ramNameText.setText(ramPart.getName());
        holder.ramTypeText.setText(ramPart.getClock());

        holder.itemView.setOnClickListener(v -> listener.onRamClick(ramPart));
    }

    @Override
    public int getItemCount() {
        return ramList.size();
    }

    static class RamViewHolder extends RecyclerView.ViewHolder {
        TextView ramNameText;
        TextView ramTypeText;

        public RamViewHolder(@NonNull View itemView) {
            super(itemView);
            ramNameText = itemView.findViewById(R.id.ramNameText);
            ramTypeText = itemView.findViewById(R.id.ramTypeText);
        }
    }
}
