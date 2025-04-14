package com.example.hf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    EditText UsernameET, PasswordET, PasswordAgainET, EmailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);
        UsernameET = findViewById(R.id.editTextUsername);
        PasswordET = findViewById(R.id.editTextPassword);
        PasswordAgainET = findViewById(R.id.editTextPasswordAgain);
        EmailET = findViewById(R.id.editTextUserEmail);

    }

    public void Complete(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void onRegister(View view) {
        String Username = UsernameET.getText().toString();
        String Password = PasswordET.getText().toString();
        String Email = EmailET.getText().toString();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("userName", Username);
                        user.put("email", Email);
                        user.put("UUID", uid);

                        db.collection("Users").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> Complete())
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Nem sikerült elmenteni az adatokat: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Létrehozás sikertelen: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void Complete(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}