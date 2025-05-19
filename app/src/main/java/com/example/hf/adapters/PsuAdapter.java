package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.models.PsuPart;

import java.util.List;

public class PsuAdapter extends RecyclerView.Adapter<PsuAdapter.PsuViewHolder> {

    private List<PsuPart> psuList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PsuPart psuPart);
    }

    public PsuAdapter(List<PsuPart> psuList, OnItemClickListener listener) {
        this.psuList = psuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PsuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.psu_part_item, parent, false);
        return new PsuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PsuViewHolder holder, int position) {
        PsuPart psuPart = psuList.get(position);
        holder.nameTextView.setText(psuPart.getName());
        holder.powerTextView.setText("Teljesítmény: " + psuPart.getPower());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(psuPart));
    }

    @Override
    public int getItemCount() {
        return psuList.size();
    }

    static class PsuViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView powerTextView;

        public PsuViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.psuName);
            powerTextView = itemView.findViewById(R.id.psuPower);
        }
    }
}
