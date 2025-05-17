package utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.hf.R;

import java.util.List;

public class SpinnerUtils {

    public static ArrayAdapter<String> createCenteredAdapter(Context context, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.spinner_item_centered, // saját layout
                items
        );
        adapter.setDropDownViewResource(R.layout.spinner_item_centered);
        return adapter;
    }
}
