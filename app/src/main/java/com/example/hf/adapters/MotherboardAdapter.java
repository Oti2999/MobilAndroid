package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.models.MotherboardPart;

import java.util.List;

public class MotherboardAdapter extends RecyclerView.Adapter<MotherboardAdapter.MotherboardViewHolder> {

    private List<MotherboardPart> motherboardList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MotherboardPart motherboardPart);
    }

    public MotherboardAdapter(List<MotherboardPart> motherboardList, OnItemClickListener listener) {
        this.motherboardList = motherboardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MotherboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.motherboard_part_item, parent, false);
        return new MotherboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotherboardViewHolder holder, int position) {
        MotherboardPart part = motherboardList.get(position);

        holder.nameTextView.setText(part.getName());
        holder.socketTextView.setText("Foglalat: " + part.getSocket());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(part));
    }

    @Override
    public int getItemCount() {
        return motherboardList.size();
    }

    static class MotherboardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView socketTextView;
        TextView formFactorTextView;

        public MotherboardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.mbName);
            socketTextView = itemView.findViewById(R.id.mbSocket);
        }
    }
}
