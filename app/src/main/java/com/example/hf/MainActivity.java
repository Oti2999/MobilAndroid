package com.example.hf;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 99;
    EditText UsernameET;
    EditText PasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsernameET = findViewById(R.id.editTextUsername);
        PasswordET = findViewById(R.id.editTextPassword);
    }
    public void Login(View view){

        String userName = UsernameET.getText().toString();
        String password = PasswordET.getText().toString();

        Log.i("MainActivity", "Bejelentkezett: " + userName + ", jelszó: " + password);
    }

    public void Register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", 99);
        startActivity(intent);
    }
}


