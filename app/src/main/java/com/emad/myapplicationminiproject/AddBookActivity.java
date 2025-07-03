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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emad.myapplicationminiproject.databinding.ActivityAddBookBinding;

import java.io.IOException;

public class AddBookActivity extends AppCompatActivity {


    ActivityAddBookBinding binding;
    LibraryDataBase db;

    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                        binding.bookImageAddBook.setImageBitmap(imageBitmap);
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
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new LibraryDataBase(this);

        binding.backIvAddBook.setOnClickListener(view -> {
            finish();
        });

        binding.bookImageAddBook.setOnClickListener(view -> {
            launcher.launch("image/*");
        });

        binding.addBookBtAddBook.setOnClickListener(view -> {
            AddBook();
        });


    }

    void AddBook() {

        try {

            String name = binding.nameBookEtAddBook.getText().toString().trim();
            String priceStr = binding.priceBookEtAddBook.getText().toString().trim();
            String quantityStr = binding.quantityBookEtAddBook.getText().toString().trim();
            String desc = binding.dickBookEtAddBook.getText().toString().trim();
            String tackPriceStr = binding.tackPriceBookEtAddBook.getText().toString().trim();
            boolean isTack = false;

            Drawable imageIvDrawable = binding.bookImageAddBook.getDrawable();
            Bitmap imageBook = ((BitmapDrawable) imageIvDrawable).getBitmap();
            Drawable defaultDrawable = ContextCompat.getDrawable(this, R.drawable.add_book_image);

            if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty() ||
                    desc.isEmpty() || tackPriceStr.isEmpty() ||
                    imageIvDrawable.getConstantState().equals(defaultDrawable.getConstantState())) {
                Toast.makeText(this, "Fill The Data", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            double tackPrice = Double.parseDouble(tackPriceStr);

            if (price < 0 || quantity < 0 || tackPrice < 0) {
                Toast.makeText(this, "Enter Data >= 0 in Price, Quantity, TakePrice", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = getIntent().getStringExtra("userEmail");
            User user = db.getUserByEmail(userEmail);

            long result = db.insertBook(new Book(name, price, quantity, isTack, tackPrice, imageBook, desc, user.getId()));

            if (result != -1) {
                Toast.makeText(this, "Added Book Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Added Book Filed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("TAG", "AddBook: " + e.getStackTrace());
            throw new RuntimeException(e);

        }

    }

}