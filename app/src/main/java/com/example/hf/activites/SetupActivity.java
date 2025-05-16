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

    private final ActivityResultLauncher<Intent> cpuResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String cpuName = result.getData().getStringExtra("cpuName");
                    selectedCpuText.setText(cpuName != null ? cpuName : "Nincs kiválasztva");
                }
            });

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
        Button selectCpuButton = findViewById(R.id.selectCpuButton);

        selectCpuButton.setOnClickListener(v -> {
            Intent intent = new Intent(SetupActivity.this, CpuParts.class);
            cpuResultLauncher.launch(intent);
        });
    }
}
