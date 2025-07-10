package com.emad.myapplicationminiproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emad.myapplicationminiproject.databinding.ActivityDetailsBookBinding;

import java.util.ArrayList;

public class DetailsBookActivity extends AppCompatActivity {

    ActivityDetailsBookBinding binding;
    LibraryDataBase db;
    ArrayList<Book> books;
    int count;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailsBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new LibraryDataBase(this);
        int bookPos = getIntent().getIntExtra("bookPos", -1);
        books = db.readAllBooks();

        book = books.get(bookPos);

        binding.bookImageDetails.setImageBitmap(book.getBookImage());
        binding.nameTvDetails.setText(book.getName());
        binding.descTvDetails.setText(book.getDisc());
        binding.priceBookTvDetails.setText(book.getPrice() + "$");


        binding.plusQuantity.setOnClickListener(view -> {
            addQuantity();
            binding.quantityTv.setText(String.valueOf(count));
        });

        binding.minusQuantity.setOnClickListener(view -> {
            minusQuantity();
            binding.quantityTv.setText(String.valueOf(count));
        });

        binding.addToCartBtDetails.setOnClickListener(view -> {
            AddBookToCart();
        });

    }

    void addQuantity() {
        if (count < book.getQuantity())
            count++;
        else
            Toast.makeText(this, "Max Quantity", Toast.LENGTH_SHORT).show();
    }

    void minusQuantity() {
        if (Integer.parseInt(binding.quantityTv.getText().toString()) > 0)
            count--;

    }

    void AddBookToCart() {

        try {

            if (count <= 0) {
                Toast.makeText(this, "Add Quantity Book", Toast.LENGTH_SHORT).show();
                return;
            }

            Book bookCart = new Book();
            bookCart.setId(book.getId());

            boolean isFind = true;
            for (int i = 0; i < Utilities.booksCart.size(); i++) {
                Book bookOld = Utilities.booksCart.get(i);
                if (Utilities.booksCart.get(i).getId() == book.getId()) {

                    isFind = false;
                    bookOld.setQuantity(bookOld.getQuantity() + count);
                    db.updateQuantityBookMinus(book, count);
                    bookOld.updateTotalPrice();
                    break;

                }
            }

            if (isFind) {

                bookCart.setBookImage(book.getBookImage());
                bookCart.setName(book.getName());
                bookCart.setPrice(book.getPrice());
                bookCart.setQuantity(count);
                bookCart.setTotalPriceBook(book.getPrice() * count);
                boolean isAdd = Utilities.booksCart.add(bookCart);

                if (isAdd) {

                    db.updateQuantityBookMinus(book, count);
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Added Filed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Added Quantity Successfully", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}