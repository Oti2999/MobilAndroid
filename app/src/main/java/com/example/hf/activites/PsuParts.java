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
import com.example.hf.adapters.PsuAdapter;
import com.example.hf.models.PsuPart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PsuParts extends AppCompatActivity {

    private RecyclerView psuRecyclerView;
    private PsuAdapter psuAdapter;
    private List<PsuPart> psuPartsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_psu_parts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        psuRecyclerView = findViewById(R.id.psuRecyclerView);
        psuRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        psuAdapter = new PsuAdapter(psuPartsList, psuPart -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("psuName", psuPart.getName());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        psuRecyclerView.setAdapter(psuAdapter);

        loadPsuPartsFromFirestore();
    }

    private void loadPsuPartsFromFirestore() {
        db.collection("PSupply")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    psuPartsList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        PsuPart psuPart = document.toObject(PsuPart.class);
                        psuPartsList.add(psuPart);
                    }
                    psuAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Hiba történt a PSU-k lekérésekor", e);
                    Toast.makeText(this, "Hiba a PSU-k betöltésekor", Toast.LENGTH_SHORT).show();
                });
    }
}
