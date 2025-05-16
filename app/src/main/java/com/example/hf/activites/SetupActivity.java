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


    }
}
