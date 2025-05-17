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
import com.example.hf.adapters.GpuAdapter;
import com.example.hf.models.GpuPart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GpuParts extends AppCompatActivity {

    private RecyclerView gpuRecyclerView;
    private GpuAdapter gpuAdapter;
    private List<GpuPart> gpuPartsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gpu_parts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        gpuRecyclerView = findViewById(R.id.gpuRecyclerView);
        gpuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        gpuAdapter = new GpuAdapter(gpuPartsList, gpuPart -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("gpuName", gpuPart.getName());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        gpuRecyclerView.setAdapter(gpuAdapter);

        loadGpuPartsFromFirestore();
    }

    private void loadGpuPartsFromFirestore() {
        db.collection("GPU")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    gpuPartsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        GpuPart gpuPart = document.toObject(GpuPart.class);
                        gpuPartsList.add(gpuPart);
                    }
                    gpuAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Hiba történt a GPU-k lekérésekor", e);
                    Toast.makeText(this, "Hiba a GPU-k betöltésekor", Toast.LENGTH_SHORT).show();
                });
    }
}
