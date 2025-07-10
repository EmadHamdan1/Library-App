package com.emad.myapplicationminiproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.emad.myapplicationminiproject.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements BookListener {

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    notifyChanges();
                }
            }
    );

    ActivityHomeBinding binding;
    LibraryDataBase db;
    HomeBookAdapterV adapterV;
    ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new LibraryDataBase(this);
        books = db.readAllBooks();

        String userEmail = getIntent().getStringExtra("userEmail");
        User user = db.getUserByEmail(userEmail);
        if (user == null) {
            Toast.makeText(this, "User data not received", Toast.LENGTH_LONG).show();
            return;
        }

        binding.nameTvHome.setText(user.getName());

        binding.goodTv.setOnClickListener(view -> {
            logout();
        });

        adapterV = new HomeBookAdapterV(books, this);
        binding.booksRvHomeV.setAdapter(adapterV);
        binding.booksRvHomeV.setLayoutManager(new LinearLayoutManager(this));

        binding.cartIvHome.setOnClickListener(view -> {

            Intent intent = new Intent(getBaseContext(), CartActivity.class);
            launcher.launch(intent);

        });

    }

    void logout() {
        SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onDeleteBook(int id) {

    }

    @Override
    public void onEditBook(int id, int pos) {

    }

    @Override
    public void onItemBook(int pos) {
        Intent intent = new Intent(getApplicationContext(), DetailsBookActivity.class);
        intent.putExtra("bookPos", pos);
        launcher.launch(intent);
    }

    void notifyChanges() {
        books = db.readAllBooks();
        adapterV = new HomeBookAdapterV(books, this);
        binding.booksRvHomeV.setAdapter(adapterV);
    }

}