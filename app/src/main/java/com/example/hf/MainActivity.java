package com.example.hf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1001;
    private FirebaseAuth mAuth;
    EditText UsernameET;
    EditText PasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATIONS_PERMISSION_REQUEST_CODE);
        }

        mAuth = FirebaseAuth.getInstance();
        UsernameET = findViewById(R.id.editTextUsername);
        PasswordET = findViewById(R.id.editTextPassword);
    }

    private void createNotificationChannel() {
        CharSequence name = "Login Channel";
        String description = "Channel for login notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("login_channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
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
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "login_channel")
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Bejelentkezés sikeres!")
                                .setContentText("Üdvözöllek az alkalmazásban!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            notificationManager.notify(1001, builder.build());
                        }

                        TextView successTextView = findViewById(R.id.successTextView);
                        successTextView.setAlpha(0f);
                        successTextView.setVisibility(View.VISIBLE);
                        successTextView.animate()
                                .alpha(1f)
                                .setDuration(1250)
                                .withEndAction(() -> {
                                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(homeIntent);
                                    finish();
                                });
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
