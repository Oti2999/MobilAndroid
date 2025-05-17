package com.example.hf.activites;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hf.R;
import com.example.hf.models.Compare;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CompareActivity extends AppCompatActivity {

    private Spinner spinnerPartType;
    private Spinner spinnerCpuOption1, spinnerCpuOption2;
    private TextView textViewComparisonResult;
    private TextView instructionText;

    private FirebaseFirestore db;
    // Lista a CPU modellek tárolására
    private List<Compare> cpuList;
    // Adapterek a két CPU spinnerhez
    private ArrayAdapter<String> adapterCpu1, adapterCpu2;

    // A felhasználó által kiválasztott CPU modellek
    private Compare selectedCpu1 = null;
    private Compare selectedCpu2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        spinnerPartType = findViewById(R.id.partTypeSpinner);
        spinnerCpuOption1 = findViewById(R.id.spinnerCpuOption1);
        spinnerCpuOption2 = findViewById(R.id.spinnerCpuOption2);
        textViewComparisonResult = findViewById(R.id.comparisonResultText);
        instructionText = findViewById(R.id.instructionText);

        db = FirebaseFirestore.getInstance();

        String[] partTypes = new String[]{"CPU", "GPU", "Motherboard", "RAM", "PSU"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_centered, partTypes);
        typeAdapter.setDropDownViewResource(R.layout.spinner_item_centered);
        spinnerPartType.setAdapter(typeAdapter);

        spinnerPartType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedType = (String) parent.getItemAtPosition(pos);
                if ("CPU".equalsIgnoreCase(selectedType)) {
                    // CPU típus esetén jelenítsük meg a két CPU spinner-t és az instrukciós szöveget
                    spinnerCpuOption1.setVisibility(View.VISIBLE);
                    spinnerCpuOption2.setVisibility(View.VISIBLE);
                    instructionText.setVisibility(View.VISIBLE);
                    // Töltjük be a CPU modelleket Firestore-ból
                    loadCpuList();
                } else {
                    // Más típus esetén elrejthetjük a CPU opció spinner-eket és törölhetjük az eredményt
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.GONE);
                    textViewComparisonResult.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Spinner CPU Option 1 esemény kezelése
        spinnerCpuOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (cpuList != null && pos < cpuList.size()) {
                    selectedCpu1 = cpuList.get(pos);
                    performComparison();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Spinner CPU Option 2 esemény kezelése
        spinnerCpuOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (cpuList != null && pos < cpuList.size()) {
                    selectedCpu2 = cpuList.get(pos);
                    performComparison();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // CPU modellek lekérése Firestore-ból
    // Fontos: Győződj meg róla, hogy a Firestore dokumentumaidban a CPU modellek "type" mezője "CPU".
    private void loadCpuList() {
        db.collection("CPU")
                .whereEqualTo("type", "CPU")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cpuList = new ArrayList<>();
                    List<String> cpuNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Compare cpu = doc.toObject(Compare.class);
                        cpuList.add(cpu);
                        cpuNames.add(cpu.getName());
                    }
                    Log.d("CompareActivity", "CPU-k száma: " + cpuList.size());

                    // Beállítjuk a spinner adaptereket
                    adapterCpu1 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, cpuNames);
                    adapterCpu1.setDropDownViewResource(R.layout.spinner_item_centered);
                    spinnerCpuOption1.setAdapter(adapterCpu1);

                    adapterCpu2 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, cpuNames);
                    adapterCpu2.setDropDownViewResource(R.layout.spinner_item_centered);
                    spinnerCpuOption2.setAdapter(adapterCpu2);

                    // Reseteljük a korábbi választásokat, ha vannak
                    selectedCpu1 = null;
                    selectedCpu2 = null;
                    textViewComparisonResult.setText("");
                })
                .addOnFailureListener(e -> {
                    textViewComparisonResult.setText("Hiba történt az adatok betöltésekor");
                });
    }

    // Összehasonlítás elvégzése, ha mindkét CPU ki van választva
    private void performComparison() {
        if (selectedCpu1 != null && selectedCpu2 != null) {
            String result = getComparisonResult(selectedCpu1, selectedCpu2);
            textViewComparisonResult.setText(result);
        }
    }

    // Egyszerű összehasonlító logika, itt például az ár alapján hasonlítunk (ezt igény szerint kiterjesztheted)
    @NonNull
    private String getComparisonResult(@NonNull Compare cpu1, @NonNull Compare cpu2) {
        StringBuilder result = new StringBuilder();
        result.append("Összehasonlítás:\n");
        if (cpu1.getPrice() < cpu2.getPrice()) {
            result.append(cpu1.getName()).append(" olcsóbb.\n");
        } else if (cpu1.getPrice() > cpu2.getPrice()) {
            result.append(cpu2.getName()).append(" olcsóbb.\n");
        } else {
            result.append("Mindkét CPU ára azonos.\n");
        }
        // CPU specifikus adatok (pl. core, socket) megjelenítése:
        result.append("CPU 1: Magok: ").append(cpu1.getCore())
                .append(", Foglalat: ").append(cpu1.getSocket()).append("\n");
        result.append("CPU 2: Magok: ").append(cpu2.getCore())
                .append(", Foglalat: ").append(cpu2.getSocket()).append("\n");
        return result.toString();
    }
}
