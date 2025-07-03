package com.emad.myapplicationminiproject;

import android.app.ProgressDialog;
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

import com.emad.myapplicationminiproject.databinding.ActivitySignUpBinding;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    LibraryDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new LibraryDataBase(this);

        binding.signUpBtSignUp.setOnClickListener(view -> {
            CreateAccount();
        });

        binding.haveAccountSignUp.setOnClickListener(view -> {
            finish();
        });


    }

    void CreateAccount() {

        try {
            String name = binding.nameEtSignUp.getText().toString().trim();
            String email = binding.emailEtSignUp.getText().toString().trim();
            String password = binding.passwordEtSignUp.getText().toString().trim();

            String userType = "";
            int checkedId = binding.userTypeRgSignUp.getCheckedRadioButtonId();

            if (checkedId == R.id.ownerRb) {
                userType = "owner";
            } else if (checkedId == R.id.customerRb) {
                userType = "customer";
            }


            if (email.isEmpty() || password.isEmpty() || name.isEmpty() ||
                    binding.userTypeRgSignUp.getCheckedRadioButtonId() == -1) {

                Toast.makeText(this, "Fill The Data", Toast.LENGTH_SHORT).show();
                return;
            }

            // التاكد ان المدخل ايميل
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            // لجعل كلمة المرور متكونة من كل الانماط (Redex)
//                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";
//                if (!password.matches(passwordPattern)) {
//                    Toast.makeText(this, "Password must contain at least 6 characters, including a number, a lowercase and an uppercase letter", Toast.LENGTH_LONG).show();
//                    return;
//                }

            if (password.length() < 6) {
                Toast.makeText(this, "Invalid Password format", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(name, email, password, userType);
            ArrayList<User> existUser = db.readAllUsers();

            for (User u : existUser) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            long result = db.insertUser(newUser);

            if (result != -1) {
                Toast.makeText(this, "Create Account Successfully", Toast.LENGTH_SHORT).show();
                ClearData();
            } else {
                Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "CreateAccount: " + e.getMessage());
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    void ClearData() {
        binding.nameEtSignUp.setText("");
        binding.emailEtSignUp.setText("");
        binding.passwordEtSignUp.setText("");
        binding.userTypeRgSignUp.clearCheck();
    }

}