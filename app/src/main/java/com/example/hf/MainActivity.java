package com.example.hf;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    EditText UsernameET;
    EditText PasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        UsernameET = findViewById(R.id.editTextUsername);
        PasswordET = findViewById(R.id.editTextPassword);

    }

    public void Login(View view) {
        String userName = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Kérlek töltsd ki a felhasználónevet és a jelszót!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Bejelentkezés sikeres
                        Log.i(LOG_TAG, "Sikeres bejelentkezés: " + userName);
                        Intent homeIntent = new Intent(this, HomeActivity.class);
                        startActivity(homeIntent);
                        finish();
                    } else {
                        // Bejelentkezés sikertelen
                        Log.w(LOG_TAG, "Sikertelen bejelentkezés", task.getException());
                        Toast.makeText(MainActivity.this, "Hibás e-mail vagy jelszó.", Toast.LENGTH_SHORT).show();
                    }
                });

        Log.i("MainActivity", "Bejelentkezett: " + userName + ", jelszó: " + password);
    }

    public void Register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
