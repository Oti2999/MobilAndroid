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
import com.example.hf.adapters.MotherboardAdapter;
import com.example.hf.models.MotherboardPart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MotherboardParts extends AppCompatActivity {

    private RecyclerView motherboardRecyclerView;
    private MotherboardAdapter motherboardAdapter;
    private List<MotherboardPart> motherboardPartsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_motherboard_parts);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        motherboardRecyclerView = findViewById(R.id.motherboardRecyclerView);
        motherboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        motherboardAdapter = new MotherboardAdapter(motherboardPartsList, motherboardPart -> {
            // ✅ Kiválasztott alaplap visszaküldése
            Intent resultIntent = new Intent();
            resultIntent.putExtra("motherboardName", motherboardPart.getName());
            resultIntent.putExtra("motherboardSocket", motherboardPart.getSocket());
            setResult(RESULT_OK, resultIntent);

            finish();
        });

        motherboardRecyclerView.setAdapter(motherboardAdapter);

        loadMotherboardsFromFirestore();
    }

    private void loadMotherboardsFromFirestore() {
        db.collection("Motherboard")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    motherboardPartsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MotherboardPart part = document.toObject(MotherboardPart.class);
                        motherboardPartsList.add(part);
                    }
                    motherboardAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Hiba történt az alaplapok lekérésekor", e);
                    Toast.makeText(this, "Hiba az alaplapok betöltésekor", Toast.LENGTH_SHORT).show();
                });
    }
}
