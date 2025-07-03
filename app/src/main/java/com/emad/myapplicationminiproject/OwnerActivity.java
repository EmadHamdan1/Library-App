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

import com.emad.myapplicationminiproject.databinding.ActivityOwnerBinding;

import java.util.ArrayList;

public class OwnerActivity extends AppCompatActivity implements BookListener {

    ActivityOwnerBinding binding;
    OwnerBookAdapter adapter;
    ArrayList<Book> books;
    LibraryDataBase db;
    User user;
    int userId;

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    notifyChanges();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new LibraryDataBase(this);

        String userEmail = getIntent().getStringExtra("userEmail");
        user = db.getUserByEmail(userEmail);
        userId = user.getId();
        books = db.readBooksByOwnerId(user.getId());

        binding.addBookIvOwner.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
            intent.putExtra("userEmail", userEmail);
            launcher.launch(intent);

        });
        adapter = new OwnerBookAdapter(books, this);
        binding.allBookRvOwner.setAdapter(adapter);
        binding.allBookRvOwner.setLayoutManager(new LinearLayoutManager(this));

        binding.profileIvOwner.setOnClickListener(view -> {
            logout();
        });

    }

    @Override
    public void onDeleteBook(int id) {
        db.deleteBook(id);
        notifyChanges();
    }

    @Override
    public void onEditBook(int id, int pos) {
        Intent intent = new Intent(getApplicationContext(), UpdateBookActivity.class);
        intent.putExtra("bookId", id);
        intent.putExtra("userId", userId);
        intent.putExtra("bookPos", pos);
        launcher.launch(intent);
    }

    @Override
    public void onItemBook(int pos) {

    }

    void notifyChanges() {
        books = db.readBooksByOwnerId(user.getId());
        adapter = new OwnerBookAdapter(books, this);
        binding.allBookRvOwner.setAdapter(adapter);
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
}