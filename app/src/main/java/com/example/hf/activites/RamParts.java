package com.example.hf.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.adapters.RamAdapter;
import com.example.hf.models.RamPart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RamParts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RamAdapter ramAdapter;
    private final ArrayList<RamPart> ramPartsList = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram_parts);

        recyclerView = findViewById(R.id.recyclerViewRam);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ramAdapter = new RamAdapter(ramPartsList, ramPart -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ramName", ramPart.getName());
            resultIntent.putExtra("ramSize", ramPart.getClock());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        recyclerView.setAdapter(ramAdapter);

        loadRamParts();
    }

    private void loadRamParts() {
        db.collection("RAM")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ramPartsList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            RamPart part = doc.toObject(RamPart.class);
                            ramPartsList.add(part);
                        }
                        ramAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Hiba a RAM-ek betöltésekor.", Toast.LENGTH_SHORT).show();
                        Log.e("RamParts", "Firestore hiba", task.getException());
                    }
                });
    }
}

