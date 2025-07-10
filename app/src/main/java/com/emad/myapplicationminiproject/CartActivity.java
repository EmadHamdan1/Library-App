package com.emad.myapplicationminiproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.emad.myapplicationminiproject.databinding.ActivityCartBinding;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements BookListener {

    ActivityCartBinding binding;
    CartBookAdapter adapter;
    double totalPrice;
    LibraryDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new LibraryDataBase(this);

        adapter = new CartBookAdapter(Utilities.booksCart, this);
        binding.bookCartRv.setAdapter(adapter);
        binding.bookCartRv.setLayoutManager(new LinearLayoutManager(this));

        calculateTotalPrice();


        binding.checkOutBtCart.setOnClickListener(view -> {
            Utilities.booksCart.clear();
            adapter.notifyDataSetChanged();
            calculateTotalPrice();
        });

    }

    @Override
    public void onDeleteBook(int pos) {
        Book deletedBook = Utilities.booksCart.get(pos);
        int id = Utilities.booksCart.get(pos).getId();
        db.updateQuantityBookPlus(db.getBookById(id), deletedBook.getQuantity());
        Utilities.booksCart.remove(pos);
        adapter.notifyItemRemoved(pos);
        calculateTotalPrice();
    }


    @Override
    public void onEditBook(int id, int pos) {

    }

    @Override
    public void onItemBook(int pos) {

    }

    private void calculateTotalPrice() {
        totalPrice = 0;
        for (Book book : Utilities.booksCart) {
            totalPrice += book.getTotalPriceBook();
        }
        binding.totalPriceCart.setText(String.format("Total Price: %.2f$", totalPrice));
    }

}