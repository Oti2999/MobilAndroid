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
    private Spinner spinnerGpuOption1, spinnerGpuOption2;
    private Spinner spinnerMbOption1, spinnerMbOption2;
    private Spinner spinnerRamOption1, spinnerRamOption2;
    private Spinner spinnerPsuOption1, spinnerPsuOption2;
    private TextView textViewComparisonResult;
    private TextView instructionText;

    private TextView gpuVramText;

    private FirebaseFirestore db;
    // Lista a CPU modellek tárolására
    private List<Compare> cpuList;
    private List<Compare> gpuList;
    private List<Compare> motherboardList;
    private List<Compare> ramList;
    private List<Compare> psuList;
    // Adapterek a két CPU spinnerhez
    private ArrayAdapter<String> adapterCpu1, adapterCpu2;
    private ArrayAdapter<String> adapterGpu1, adapterGpu2;
    private ArrayAdapter<String> adapterMb1, adapterMb2;
    private ArrayAdapter<String> adapterRam1, adapterRam2;
    private ArrayAdapter<String> adapterPsu1, adapterPsu2;

    // A felhasználó által kiválasztott CPU modellek
    private Compare selectedCpu1 = null, selectedCpu2 = null;
    private Compare selectedGpu1 = null, selectedGpu2 = null;
    private Compare selectedMb1 = null, selectedMb2 = null;
    private Compare selectedRam1 = null, selectedRam2 = null;
    private Compare selectedPsu1 = null, selectedPsu2 = null;

    private void hideAllSpinners() {
        spinnerCpuOption1.setVisibility(View.GONE);
        spinnerCpuOption2.setVisibility(View.GONE);
        spinnerGpuOption1.setVisibility(View.GONE);
        spinnerGpuOption2.setVisibility(View.GONE);
        spinnerMbOption1.setVisibility(View.GONE);
        spinnerMbOption2.setVisibility(View.GONE);
        spinnerRamOption1.setVisibility(View.GONE);
        spinnerRamOption2.setVisibility(View.GONE);
        spinnerPsuOption1.setVisibility(View.GONE);
        spinnerPsuOption2.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        spinnerPartType = findViewById(R.id.partTypeSpinner);
        spinnerCpuOption1 = findViewById(R.id.spinnerCpuOption1);
        spinnerCpuOption2 = findViewById(R.id.spinnerCpuOption2);
        spinnerGpuOption1 = findViewById(R.id.spinnerGpuOption1);
        spinnerGpuOption2 = findViewById(R.id.spinnerGpuOption2);
        spinnerMbOption1 = findViewById(R.id.spinnerMbOption1);
        spinnerMbOption2 = findViewById(R.id.spinnerMbOption2);
        spinnerRamOption1 = findViewById(R.id.spinnerRamOption1);
        spinnerRamOption2 = findViewById(R.id.spinnerRamOption2);
        spinnerPsuOption1 = findViewById(R.id.spinnerPsuOption1);
        spinnerPsuOption2 = findViewById(R.id.spinnerPsuOption2);

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
                hideAllSpinners();
                instructionText.setVisibility(View.VISIBLE);
                String selectedType = (String) parent.getItemAtPosition(pos);
                if ("CPU".equalsIgnoreCase(selectedType)) {
                    // CPU típus esetén a CPU spinnerek jelennek meg, a GPU spinnereket elrejtjük.
                    spinnerCpuOption1.setVisibility(View.VISIBLE);
                    spinnerCpuOption2.setVisibility(View.VISIBLE);
                    spinnerGpuOption1.setVisibility(View.GONE);
                    spinnerGpuOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.VISIBLE);
                    loadCpuList();
                } else if ("GPU".equalsIgnoreCase(selectedType)) {
                    // GPU típus esetén a GPU spinnerek jelennek meg, a CPU spinnereket elrejtjük.
                    spinnerGpuOption1.setVisibility(View.VISIBLE);
                    spinnerGpuOption2.setVisibility(View.VISIBLE);
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.VISIBLE);
                    loadGpuList();

                }
                else if ("Motherboard".equalsIgnoreCase(selectedType)) {
                    spinnerMbOption1.setVisibility(View.VISIBLE);
                    spinnerMbOption2.setVisibility(View.VISIBLE);
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    spinnerGpuOption1.setVisibility(View.GONE);
                    spinnerGpuOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.VISIBLE);
                    loadMotherboardList();
                }
                else if ("RAM".equalsIgnoreCase(selectedType)) {
                    spinnerRamOption1.setVisibility(View.VISIBLE);
                    spinnerRamOption2.setVisibility(View.VISIBLE);
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    spinnerGpuOption1.setVisibility(View.GONE);
                    spinnerGpuOption2.setVisibility(View.GONE);
                    spinnerMbOption1.setVisibility(View.GONE);
                    spinnerMbOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.VISIBLE);
                    loadRamList();
                }
                else if ("PSU".equalsIgnoreCase(selectedType)) {
                    spinnerPsuOption1.setVisibility(View.VISIBLE);
                    spinnerPsuOption2.setVisibility(View.VISIBLE);
                    spinnerRamOption1.setVisibility(View.GONE);
                    spinnerRamOption2.setVisibility(View.GONE);
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    spinnerGpuOption1.setVisibility(View.GONE);
                    spinnerGpuOption2.setVisibility(View.GONE);
                    spinnerMbOption1.setVisibility(View.GONE);
                    spinnerMbOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.VISIBLE);
                    loadPsuList();
                }
                else {
                    // Más típusok esetén elrejtjük mindkét típus spinnerét és töröljük az eredményt
                    spinnerCpuOption1.setVisibility(View.GONE);
                    spinnerCpuOption2.setVisibility(View.GONE);
                    spinnerGpuOption1.setVisibility(View.GONE);
                    spinnerGpuOption2.setVisibility(View.GONE);
                    spinnerPsuOption1.setVisibility(View.GONE);
                    spinnerPsuOption2.setVisibility(View.GONE);
                    spinnerRamOption1.setVisibility(View.GONE);
                    spinnerRamOption2.setVisibility(View.GONE);
                    spinnerMbOption1.setVisibility(View.GONE);
                    spinnerMbOption2.setVisibility(View.GONE);
                    instructionText.setVisibility(View.GONE);
                    textViewComparisonResult.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideAllSpinners();
                instructionText.setVisibility(View.GONE);
                textViewComparisonResult.setText("");
            }
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

        // Spinner GPU Option 1 esemény kezelése
        spinnerGpuOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (gpuList != null && pos < gpuList.size()) {
                    selectedGpu1 = gpuList.get(pos);
                    performGpuComparison(); // Hívjuk meg az összehasonlítást
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

// Spinner GPU Option 2 esemény kezelése
        spinnerGpuOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (gpuList != null && pos < gpuList.size()) {
                    selectedGpu2 = gpuList.get(pos);
                    performGpuComparison(); // Hívjuk meg az összehasonlítást
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerMbOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (motherboardList != null && pos < motherboardList.size()) {
                    selectedMb1 = motherboardList.get(pos);
                    performMbComparison();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerMbOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (motherboardList != null && pos < motherboardList.size()) {
                    selectedMb2 = motherboardList.get(pos);
                    performMbComparison();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinnerRamOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (ramList != null && pos < ramList.size()) {
                    selectedRam1 = ramList.get(pos);
                    performRamComparison();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerRamOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (ramList != null && pos < ramList.size()) {
                    selectedRam2 = ramList.get(pos);
                    performRamComparison();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinnerPsuOption1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (psuList != null && pos < psuList.size()) {
                    selectedPsu1 = psuList.get(pos);
                    performPsuComparison();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerPsuOption2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (psuList != null && pos < psuList.size()) {
                    selectedPsu2 = psuList.get(pos);
                    performPsuComparison();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
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
    private void loadGpuList() {
        db.collection("GPU").whereEqualTo("type", "GPU").get().addOnSuccessListener(queryDocumentSnapshots -> {
            gpuList = new ArrayList<>();
            List<String> gpuNames = new ArrayList<>();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Compare gpu = doc.toObject(Compare.class);
                gpuList.add(gpu);
                gpuNames.add(gpu.getName());
            }
            adapterGpu1 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, gpuNames);
            adapterGpu2 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, gpuNames);
            spinnerGpuOption1.setAdapter(adapterGpu1);
            spinnerGpuOption2.setAdapter(adapterGpu2);
            selectedGpu1 = null;
            selectedGpu2 = null;
            textViewComparisonResult.setText("");
        });
    }

    @NonNull
    private String getGpuComparisonResult(@NonNull Compare gpu1, @NonNull Compare gpu2) {
        StringBuilder result = new StringBuilder();
        result.append("GPU összehasonlítás:\n");

        if (gpu1.getPrice() < gpu2.getPrice()) {
            result.append(gpu1.getName()).append(" olcsóbb.\n");
        } else if (gpu1.getPrice() > gpu2.getPrice()) {
            result.append(gpu2.getName()).append(" olcsóbb.\n");
        } else {
            result.append("Mindkét GPU ára azonos.\n");
        }

        // VRAM összehasonlítás
        result.append("VRAM:\n")
                .append(gpu1.getName()).append(": ").append(gpu1.getVram()).append("\n")
                .append(gpu2.getName()).append(": ").append(gpu2.getVram()).append("\n");

        return result.toString();
    }
    private void performGpuComparison() {
        if (selectedGpu1 != null && selectedGpu2 != null) {
            String result = getGpuComparisonResult(selectedGpu1, selectedGpu2);
            textViewComparisonResult.setText(result);
        }
    }
    private void loadMotherboardList() {
        db.collection("Motherboard")
                .whereEqualTo("type", "MB")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    motherboardList = new ArrayList<>();
                    List<String> mbNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Compare mb = doc.toObject(Compare.class);
                        motherboardList.add(mb);
                        mbNames.add(mb.getName());
                    }
                    adapterMb1 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, mbNames);
                    adapterMb2 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, mbNames);
                    spinnerMbOption1.setAdapter(adapterMb1);
                    spinnerMbOption2.setAdapter(adapterMb2);
                    selectedMb1 = null;
                    selectedMb2 = null;
                    textViewComparisonResult.setText("");
                })
                .addOnFailureListener(e -> textViewComparisonResult.setText("Hiba történt az alaplapok betöltésekor"));
    }

    private void performMbComparison() {
        if (selectedMb1 != null && selectedMb2 != null) {
            String result = getMbComparisonResult(selectedMb1, selectedMb2);
            textViewComparisonResult.setText(result);
        }
    }

    @NonNull
    private String getMbComparisonResult(@NonNull Compare mb1, @NonNull Compare mb2) {
        StringBuilder result = new StringBuilder();
        result.append("Alaplap összehasonlítás:\n");

        result.append("Socket típusa:\n")
                .append(mb1.getName()).append(": ").append(mb1.getSocket()).append("\n")
                .append(mb2.getName()).append(": ").append(mb2.getSocket()).append("\n");

        result.append("Támogatott RAM:\n")
                .append(mb1.getName()).append(": ").append(mb1.getRam()).append("\n")
                .append(mb2.getName()).append(": ").append(mb2.getRam()).append("\n");

        return result.toString();
    }

    private void loadRamList() {
        db.collection("RAM")
                .whereEqualTo("type", "RAM")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ramList = new ArrayList<>();
                    List<String> ramNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Compare ram = doc.toObject(Compare.class);
                        ramList.add(ram);
                        ramNames.add(ram.getName());
                    }
                    adapterRam1 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, ramNames);
                    adapterRam2 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, ramNames);
                    spinnerRamOption1.setAdapter(adapterRam1);
                    spinnerRamOption2.setAdapter(adapterRam2);
                    selectedRam1 = null;
                    selectedRam2 = null;
                    textViewComparisonResult.setText("");
                })
                .addOnFailureListener(e -> textViewComparisonResult.setText("Hiba történt a RAM-ok betöltésekor"));

    }
    private void performRamComparison() {
        if (selectedRam1 != null && selectedRam2 != null) {
            String result = getRamComparisonResult(selectedRam1, selectedRam2);
            textViewComparisonResult.setText(result);
        }
    }
    @NonNull
    private String getRamComparisonResult(@NonNull Compare ram1, @NonNull Compare ram2) {
        StringBuilder result = new StringBuilder();
        result.append("RAM összehasonlítás:\n");

        if (ram1.getPrice() < ram2.getPrice()) {
            result.append(ram1.getName()).append(" olcsóbb.\n");
        } else if (ram1.getPrice() > ram2.getPrice()) {
            result.append(ram2.getName()).append(" olcsóbb.\n");
        } else {
            result.append("Mindkét RAM ára azonos.\n");
        }

        result.append("Órajelek:\n")
                .append(ram1.getName()).append(": ").append(ram1.getClock()).append("\n")
                .append(ram2.getName()).append(": ").append(ram2.getClock()).append("\n");

        return result.toString();
    }
    private void loadPsuList() {
        db.collection("PSupply")
                .whereEqualTo("type", "PS")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    psuList = new ArrayList<>();
                    List<String> psuNames = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Compare psu = doc.toObject(Compare.class);
                        psuList.add(psu);
                        psuNames.add(psu.getName());
                    }
                    adapterPsu1 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, psuNames);
                    adapterPsu2 = new ArrayAdapter<>(this, R.layout.spinner_item_centered, psuNames);
                    spinnerPsuOption1.setAdapter(adapterPsu1);
                    spinnerPsuOption2.setAdapter(adapterPsu2);
                    selectedPsu1 = null;
                    selectedPsu2 = null;
                    textViewComparisonResult.setText("");
                })
                .addOnFailureListener(e -> textViewComparisonResult.setText("Hiba történt a PSU-k betöltésekor"));
    }
    private void performPsuComparison() {
        if (selectedPsu1 != null && selectedPsu2 != null) {
            String result = getPsuComparisonResult(selectedPsu1, selectedPsu2);
            textViewComparisonResult.setText(result);
        }
    }
    private String getPsuComparisonResult(@NonNull Compare psu1, @NonNull Compare psu2) {
        StringBuilder result = new StringBuilder();
        result.append("PSU összehasonlítás:\n");

        if (psu1.getPrice() < psu2.getPrice()) {
            result.append(psu1.getName()).append(" olcsóbb.\n");
        } else if (psu1.getPrice() > psu2.getPrice()) {
            result.append(psu2.getName()).append(" olcsóbb.\n");
        } else {
            result.append("Mindkét PSU ára azonos.\n");
        }

        result.append("Teljesítmény:\n")
                .append(psu1.getName()).append(": ").append(psu1.getPower()).append("\n")
                .append(psu2.getName()).append(": ").append(psu2.getPower()).append("\n");

        return result.toString();
    }
}
