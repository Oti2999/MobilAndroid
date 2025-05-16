package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
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
                .inflate(R.layout.cpu_part_item, parent, false);
        return new CpuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CpuViewHolder holder, int position) {
        // Lekérjük az aktuális CPU objektumot
        CpuPart cpuPart = cpuList.get(position);

        // A CPU nevét írjuk ki
        holder.nameTextView.setText(cpuPart.getName());

        // Itt íratjuk ki a magok számát
        holder.coreTextView.setText("Magok: " + cpuPart.getCore());

        // Itt íratjuk ki a foglalat típusát
        holder.socketTextView.setText("Foglalat: " + cpuPart.getSocket());

        // Item kattintási esemény
        holder.itemView.setOnClickListener(v -> listener.onItemClick(cpuPart));
    }

    @Override
    public int getItemCount() {
        return cpuList.size();
    }

    static class CpuViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView coreTextView;
        TextView socketTextView;

        public CpuViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cpuName);
            coreTextView = itemView.findViewById(R.id.cpuCore);
            socketTextView = itemView.findViewById(R.id.cpuSocket);
        }
    }
}
