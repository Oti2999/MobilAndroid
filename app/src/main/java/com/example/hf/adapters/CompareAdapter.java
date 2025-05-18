package com.example.hf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.models.Compare;

import java.util.List;

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.CompareViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Compare compare);
    }

    private List<Compare> compareList;
    private OnItemClickListener listener;

    public CompareAdapter(List<Compare> compareList, OnItemClickListener listener) {
        this.compareList = compareList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.compare_item, parent, false);
        return new CompareViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull CompareViewHolder holder, int position) {
        Compare compare = compareList.get(position);
        holder.nameTextView.setText(compare.getName());
        holder.priceTextView.setText("Ár: " + compare.getPrice() + " Ft");

        // Ha az extra TextView-k nem állnak rendelkezésre (null értékűek), nem csinálunk semmit
        if (holder.extra1TextView != null) {
            holder.extra1TextView.setVisibility(View.GONE);
        }
        if (holder.extra2TextView != null) {
            holder.extra2TextView.setVisibility(View.GONE);
        }

        // CPU esetén, ha a socket érték létezik és a TextView-k is rendelkezésre állnak,
        // jelenítsük meg a típusspecifikus adatokat.
        if ("CPU".equalsIgnoreCase(compare.getType())) {
            if (compare.getSocket() != null && holder.extra1TextView != null && holder.extra2TextView != null) {
                holder.extra1TextView.setVisibility(View.VISIBLE);
                holder.extra2TextView.setVisibility(View.VISIBLE);
                holder.extra1TextView.setText("Magok: " + compare.getCore());
                holder.extra2TextView.setText("Foglalat: " + compare.getSocket());
            }
        }

        if ("Motherboard".equalsIgnoreCase(compare.getType())) {
            if (compare.getSocket() != null && compare.getRam() != null &&
                    holder.extra1TextView != null && holder.extra2TextView != null) {
                holder.extra1TextView.setVisibility(View.VISIBLE);
                holder.extra2TextView.setVisibility(View.VISIBLE);
                holder.extra1TextView.setText("Foglalat: " + compare.getSocket());
                holder.extra2TextView.setText("RAM típus: " + compare.getRam());
            }
        }

        // RAM
        if ("RAM".equalsIgnoreCase(compare.getType())) {
            if (compare.getRam() != null && compare.getClock() != null &&
                    holder.extra1TextView != null && holder.extra2TextView != null) {
                holder.extra1TextView.setVisibility(View.VISIBLE);
                holder.extra2TextView.setVisibility(View.VISIBLE);
                holder.extra1TextView.setText("RAM típus: " + compare.getRam());
                holder.extra2TextView.setText("Órajel: " + compare.getClock());
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(compare));
    }

    @Override
    public int getItemCount() {
        return compareList.size();
    }

    static class CompareViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView extra1TextView;
        TextView extra2TextView;

        public CompareViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.compareItemName);
            priceTextView = itemView.findViewById(R.id.compareItemPrice);
            // Ezek próbálnak kapcsolódni az XML-hez. Ha az XML-ben nem szerepelnek ezek az ID-k,
            // akkor a findViewById visszaad null-t, és a kód a fenti null-check-ok miatt nem dob hibát.
            extra1TextView = itemView.findViewById(R.id.compareItemExtra1);
            extra2TextView = itemView.findViewById(R.id.compareItemExtra2);
        }
    }
}
