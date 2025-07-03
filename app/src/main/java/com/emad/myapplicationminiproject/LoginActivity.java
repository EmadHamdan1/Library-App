package com.emad.myapplicationminiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emad.myapplicationminiproject.databinding.ActivityLoginBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LibraryDataBase db;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new LibraryDataBase(this);

        sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        editor = sharedPref.edit();

        if (isLoggedIn()) {
            String userType = sharedPref.getString("userType", "");
            String userEmail = sharedPref.getString("userEmail", "");

            Intent intent;
            if (userType.equalsIgnoreCase("customer")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userEmail", userEmail);

            } else if (userType.equalsIgnoreCase("owner")) {

                intent = new Intent(this, OwnerActivity.class);
                intent.putExtra("userEmail", userEmail);
            } else {
                return;
            }

            startActivity(intent);
            finish();
            return;
        }

        binding.signInBtLogin.setOnClickListener(view -> {
            LoginScreen();
        });

        binding.createAccTvLogin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });

        binding.forgetPassTvLogin.setOnClickListener(view -> {

            startActivity(new Intent(getBaseContext(), ForgetPasswordActivity.class));

        });


    }

    void LoginScreen() {

        try {

            String email = binding.emailEtLogin.getText().toString().trim();
            String password = binding.passwordEtLogin.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill The Data", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Invalid Password format", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.getUserByEmail(email);

            if (user == null) {
                Toast.makeText(this, "Email Not Correct", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!user.getPassword().equals(password)) {
                Toast.makeText(this, "Password Not Correct", Toast.LENGTH_SHORT).show();
                return;
            }

            SaveLoginData(user);

            if (user.getUsertype().equalsIgnoreCase("customer")) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userEmail", user.getEmail());
                startActivity(intent);
                finish();

            } else if (user.getUsertype().equalsIgnoreCase("owner")) {
                Intent intent = new Intent(this, OwnerActivity.class);
                intent.putExtra("userEmail", user.getEmail());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Unknown user type", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void SaveLoginData(User user) {

        sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", user.getEmail());
        editor.putInt("userId", user.getId());
        editor.putString("userType", user.getUsertype());
        editor.apply();
    }

    private boolean isLoggedIn() {
        sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPref.getBoolean("isLoggedIn", false);
    }

}