package com.example.hf.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.adapters.CpuAdapter;
import com.example.hf.models.CpuPart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CpuParts extends AppCompatActivity {

    private RecyclerView cpuRecyclerView;
    private CpuAdapter cpuAdapter;
    private List<CpuPart> cpuPartsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cpu_parts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        cpuRecyclerView = findViewById(R.id.cpuRecyclerView);
        cpuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cpuAdapter = new CpuAdapter(cpuPartsList, cpuPart -> {
            // ✅ Kiválasztott CPU visszaküldése
            Intent resultIntent = new Intent();
            resultIntent.putExtra("cpuName", cpuPart.getName());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        cpuRecyclerView.setAdapter(cpuAdapter);

        loadCpuPartsFromFirestore();
    }

    private void loadCpuPartsFromFirestore() {
        db.collection("CPU")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cpuPartsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CpuPart cpuPart = document.toObject(CpuPart.class);
                        cpuPartsList.add(cpuPart);
                    }
                    cpuAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Hiba történt a CPU-k lekérésekor", e);
                    Toast.makeText(this, "Hiba a CPU-k betöltésekor", Toast.LENGTH_SHORT).show();
                });
    }
}
