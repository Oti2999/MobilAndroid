package com.example.hf.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hf.R;
import com.example.hf.adapters.CompareAdapter;
import com.example.hf.models.Compare;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CompareActivity extends AppCompatActivity {

    private Spinner partTypeSpinner;
    private RecyclerView recyclerViewFirst, recyclerViewSecond;
    private TextView comparisonResultText;
    private FirebaseFirestore db;

    private CompareAdapter adapterFirst;
    private CompareAdapter adapterSecond;

    // Egy közös lista, amelyet mindkét RecyclerView megjelenít
    private List<Compare> compareList;

    // A felhasználói választások tárolása:
    private Compare firstSelectedItem = null;
    private Compare secondSelectedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        partTypeSpinner = findViewById(R.id.partTypeSpinner);
        recyclerViewFirst = findViewById(R.id.firstPartRecyclerView);
        recyclerViewSecond = findViewById(R.id.secondPartRecyclerView);
        comparisonResultText = findViewById(R.id.comparisonResultText);

        db = FirebaseFirestore.getInstance();

        // Spinner feltöltése alkatrész típusokkal:
        String[] partTypes = new String[]{"CPU", "GPU", "Motherboard", "RAM", "PSU"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        partTypeSpinner.setAdapter(spinnerAdapter);

        // RecyclerView-ok lineáris elrendezése:
        recyclerViewFirst.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSecond.setLayoutManager(new LinearLayoutManager(this));

        compareList = new ArrayList<>();

        adapterFirst = new CompareAdapter(compareList, new CompareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Compare compare) {
                firstSelectedItem = compare;
                compareSelectedItems();
            }
        });

        adapterSecond = new CompareAdapter(compareList, new CompareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Compare compare) {
                secondSelectedItem = compare;
                compareSelectedItems();
            }
        });

        recyclerViewFirst.setAdapter(adapterFirst);
        recyclerViewSecond.setAdapter(adapterSecond);

        // A Spinner eseményfigyelése: amikor a felhasználó új típusra vált, betöltjük az adott típusú alkatrészeket
        partTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                loadCompareItems(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadCompareItems(String type) {
        db.collection("CPU")
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    compareList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Compare compare = document.toObject(Compare.class);
                        compareList.add(compare);
                    }
                    adapterFirst.notifyDataSetChanged();
                    adapterSecond.notifyDataSetChanged();

                    // Töröljük az előző választásokat, ha új szűrő került kiválasztásra:
                    firstSelectedItem = null;
                    secondSelectedItem = null;
                    comparisonResultText.setText("");
                })
                .addOnFailureListener(e -> {
                    comparisonResultText.setText("Hiba történt az adatok betöltésekor");
                });
    }

    private void compareSelectedItems() {
        if (firstSelectedItem != null && secondSelectedItem != null) {
            String result = getComparisonResult(firstSelectedItem, secondSelectedItem);
            comparisonResultText.setText(result);
        }
    }

    @NonNull
    private String getComparisonResult(@NonNull Compare item1, @NonNull Compare item2) {
        StringBuilder resultBuilder = new StringBuilder();

        // Egyszerű összehasonlítás: teljesítmény és ár alapján

        if(item1.getPrice() < item2.getPrice()){
            resultBuilder.append(item1.getName()).append(" olcsóbb.\n");
        } else if(item1.getPrice() > item2.getPrice()){
            resultBuilder.append(item2.getName()).append(" olcsóbb.\n");
        } else {
            resultBuilder.append("Mindkét alkatrész ára azonos.\n");
        }

        return resultBuilder.toString();
    }
}
