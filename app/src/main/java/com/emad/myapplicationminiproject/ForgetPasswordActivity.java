package com.emad.myapplicationminiproject;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emad.myapplicationminiproject.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {

    ActivityForgetPasswordBinding binding;
    LibraryDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new LibraryDataBase(this);

        binding.resetPasswordForget.setOnClickListener(view -> {
            ResetPassword();
        });


    }

    void ResetPassword() {

        try {

            String email = binding.emailEtForget.getText().toString().trim();
            String newPassword = binding.newPasswordEtForget.getText().toString().trim();

            if (email.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Fill The Data", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Invalid Password format", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = db.getUserByEmail(email);

            if (user == null) {
                Toast.makeText(this, "Account Not Exists", Toast.LENGTH_SHORT).show();
                return;
            }
            user.setPassword(newPassword);
            int result = db.updateUserPassword(user);

            if (result != -1) {
                Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Filed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("TAG", "ResetPassword: " + e.getStackTrace());
            throw new RuntimeException(e);
        }


    }

}