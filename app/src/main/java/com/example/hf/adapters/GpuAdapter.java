package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.models.GpuPart;

import java.util.List;

public class GpuAdapter extends RecyclerView.Adapter<GpuAdapter.GpuViewHolder> {

    public interface OnGpuSelectedListener {
        void onGpuSelected(GpuPart gpuPart);
    }

    private final List<GpuPart> gpuList;
    private final OnGpuSelectedListener listener;

    public GpuAdapter(List<GpuPart> gpuList, OnGpuSelectedListener listener) {
        this.gpuList = gpuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GpuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gpu_part_item, parent, false);
        return new GpuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GpuViewHolder holder, int position) {
        GpuPart part = gpuList.get(position);
        holder.name.setText(part.getName());
        holder.vram.setText(part.getVram());

        holder.itemView.setOnClickListener(v -> listener.onGpuSelected(part));
    }

    @Override
    public int getItemCount() {
        return gpuList.size();
    }

    public static class GpuViewHolder extends RecyclerView.ViewHolder {
        TextView name, vram;

        public GpuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.gpuName);
            vram = itemView.findViewById(R.id.gpuVram);
        }
    }
}
