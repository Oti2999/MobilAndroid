package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.models.CpuPart;

import java.util.List;

public class CpuAdapter extends RecyclerView.Adapter<CpuAdapter.CpuViewHolder> {

    private List<CpuPart> cpuList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CpuPart cpuPart);
    }

    public CpuAdapter(List<CpuPart> cpuList, OnItemClickListener listener) {
        this.cpuList = cpuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CpuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CpuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CpuViewHolder holder, int position) {
        CpuPart cpu = cpuList.get(position);
        holder.textView.setText(cpu.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(cpu));
    }

    @Override
    public int getItemCount() {
        return cpuList.size();
    }

    static class CpuViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CpuViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
