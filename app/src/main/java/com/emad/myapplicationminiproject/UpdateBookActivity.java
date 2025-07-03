package com.emad.myapplicationminiproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emad.myapplicationminiproject.databinding.ActivityUpdateBookBinding;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateBookActivity extends AppCompatActivity {

    ActivityUpdateBookBinding binding;
    LibraryDataBase db;
    ArrayList<Book> books;
    int bookId;
    int userId;

    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                        binding.newBookImageUpdate.setImageBitmap(imageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUpdateBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new LibraryDataBase(this);

        bookId = getIntent().getIntExtra("bookId", -1);
        int booPos = getIntent().getIntExtra("bookPos", -1);
        userId = getIntent().getIntExtra("userId", -1);
        books = db.readBooksByOwnerId(userId);
        Book book = books.get(booPos);

        binding.newNameBookEtUpdate.setText(book.getName());
        binding.newPriceBookEtUpdate.setText(String.valueOf(book.getPrice()));
        binding.newQuantityBookEtUpdate.setText(String.valueOf(book.getQuantity()));
        binding.newDickBookEtUpdate.setText(book.getDisc());
        binding.newTackPriceBookEtUpdate.setText(String.valueOf(book.getTakePrice()));
        binding.newBookImageUpdate.setImageBitmap(book.getBookImage());

        binding.backIvUpdate.setOnClickListener(view -> {
            finish();
        });

        binding.updateBtUpdate.setOnClickListener(view -> {
            UpdateBook();
        });

        binding.newBookImageUpdate.setOnClickListener(view -> {
            launcher.launch("image/*");
        });
    }

    void UpdateBook() {

        try {

            String newName = binding.newNameBookEtUpdate.getText().toString().trim();
            String newPriceStr = binding.newPriceBookEtUpdate.getText().toString().trim();
            String newQuantityStr = binding.newQuantityBookEtUpdate.getText().toString().trim();
            String newDesc = binding.newDickBookEtUpdate.getText().toString().trim();
            String newTackPriceStr = binding.newTackPriceBookEtUpdate.getText().toString().trim();
            boolean isTack = false;

            Drawable imageDraw = binding.newBookImageUpdate.getDrawable();
            Bitmap newImage = ((BitmapDrawable) imageDraw).getBitmap();

            if (newName.isEmpty() || newPriceStr.isEmpty() || newQuantityStr.isEmpty() ||
                    newDesc.isEmpty() || newTackPriceStr.isEmpty()) {
                Toast.makeText(this, "Fill The Data", Toast.LENGTH_SHORT).show();
                return;
            }

            double newPrice = Double.parseDouble(newPriceStr);
            int newQuantity = Integer.parseInt(newQuantityStr);
            double newTackPrice = Double.parseDouble(newTackPriceStr);

            if (newPrice < 0 || newQuantity < 0 || newTackPrice < 0) {
                Toast.makeText(this, "Enter Data >= 0 in Price, Quantity, TakePrice", Toast.LENGTH_SHORT).show();
                return;
            }

            int result = db.updateBook(new Book(bookId, newName, newPrice, newQuantity, isTack, newTackPrice, newImage, newDesc));

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