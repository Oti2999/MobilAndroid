package com.example.hf.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hf.R;

public class SetupActivity extends AppCompatActivity {

    private TextView selectedCpuText;
    private TextView selectedMotherboardText;
    private String selectedMotherboardSocket = null;
    private Button selectCpuButton;
    private TextView selectedRamText;
    private Button selectRamButton;
    private TextView selectedGpuText;
    private Button selectGpuButton;
    private TextView selectedPsuText;
    private Button selectPsuButton;
    private ActivityResultLauncher<Intent> psuResultLauncher;
    private ActivityResultLauncher<Intent> gpuResultLauncher;
    private ActivityResultLauncher<Intent> ramResultLauncher;

    private ActivityResultLauncher<Intent> motherboardResultLauncher;


    private ActivityResultLauncher<Intent> cpuResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedCpuText = findViewById(R.id.selectedCpuText);
        selectedMotherboardText = findViewById(R.id.selectedMotherboardText);
        selectedGpuText = findViewById(R.id.selectedGpuText);
        selectedRamText = findViewById(R.id.selectedRamText);
        selectedPsuText = findViewById(R.id.selectedPsuText);

        selectGpuButton = findViewById(R.id.selectGpuButton);
        selectGpuButton.setEnabled(false);
        selectRamButton = findViewById(R.id.selectRamButton);
        selectRamButton.setEnabled(false);
        selectPsuButton = findViewById(R.id.selectPsuButton);
        selectPsuButton.setEnabled(false);
        Button selectCpuButton = findViewById(R.id.selectCpuButton);
        selectCpuButton.setEnabled(false);

        Button selectMotherboardButton = findViewById(R.id.selectMotherboardButton);

        motherboardResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String motherboardName = result.getData().getStringExtra("motherboardName");
                selectedMotherboardText.setText(motherboardName);
                selectedMotherboardSocket = result.getData().getStringExtra("motherboardSocket");
                selectedMotherboardText.setTag(selectedMotherboardSocket);
                selectedCpuText.setText("Nincs kiválasztva");
                if (selectedMotherboardSocket != null && !selectedMotherboardSocket.isEmpty()) {
                    selectCpuButton.setEnabled(true);
                }
            }
        });

        cpuResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String cpuName = result.getData().getStringExtra("cpuName");
                selectedCpuText.setText(cpuName != null ? cpuName : "Nincs kiválasztva");
                if (cpuName != null && !cpuName.isEmpty()) {
                    selectRamButton.setEnabled(true);
                }
            }
        });

        ramResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String ramName = result.getData().getStringExtra("ramName");
                selectedRamText.setText(ramName != null ? ramName : "Nincs kiválasztva");
                if (ramName != null && !ramName.isEmpty()) {
                    selectGpuButton.setEnabled(true);
                }
            }
        });
        gpuResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String gpuName = result.getData().getStringExtra("gpuName");
                selectedGpuText.setText(gpuName != null ? gpuName : "Nincs kiválasztva");
                if (gpuName != null && !gpuName.isEmpty()) {
                    selectPsuButton.setEnabled(true);
                }
            }
        });

        psuResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                String psuName = result.getData().getStringExtra("psuName");
                selectedPsuText.setText(psuName != null ? psuName : "Nincs kiválasztva");
            }
        });

        selectMotherboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, MotherboardParts.class);
            motherboardResultLauncher.launch(intent);
        });

        selectCpuButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, CpuParts.class);
            String selectedMotherboardSocket = selectedMotherboardText.getTag() != null
                    ? selectedMotherboardText.getTag().toString()
                    : null;
            intent.putExtra("socketFilter", selectedMotherboardSocket);
            cpuResultLauncher.launch(intent);
        });

        selectRamButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, RamParts.class);
            ramResultLauncher.launch(intent);
        });
        selectGpuButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, GpuParts.class);
            gpuResultLauncher.launch(intent);
        });
        selectPsuButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, PsuParts.class);
            psuResultLauncher.launch(intent);
        });
    }
}
