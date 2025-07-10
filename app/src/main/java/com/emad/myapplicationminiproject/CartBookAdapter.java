package com.emad.myapplicationminiproject;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emad.myapplicationminiproject.databinding.ItemCartActivityBinding;

import java.util.ArrayList;

public class CartBookAdapter extends RecyclerView.Adapter<CartBookAdapter.CartBookViewHolder> {
    ArrayList<Book> books;
    BookListener listener;

    public CartBookAdapter(ArrayList<Book> books, BookListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartActivityBinding binding = ItemCartActivityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartBookViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartBookViewHolder holder, int position) {

        Book book = books.get(position);

        holder.imageBookIvItem.setImageBitmap(book.getBookImage());
        holder.nameBookItem.setText(book.getName());
        holder.priceBookItem.setText("Price: " + book.getPrice());
        holder.quantityBookItem.setText("Quan: " + book.getQuantity());
        holder.totalBooksPriceItem.setText("Total: " + book.getTotalPriceBook());

        holder.deleteBookIvItem.setOnClickListener(view -> {
            listener.onDeleteBook(holder.getAdapterPosition());
        });

        holder.itemView.setOnClickListener(view -> {
            listener.onItemBook(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public static class CartBookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBookIvItem, deleteBookIvItem;
        TextView nameBookItem, priceBookItem, quantityBookItem, totalBooksPriceItem;

        public CartBookViewHolder(ItemCartActivityBinding binding) {
            super(binding.getRoot());
            imageBookIvItem = binding.imageBookIvItem;
            deleteBookIvItem = binding.deleteBookIvItem;
            nameBookItem = binding.nameBookItem;
            priceBookItem = binding.priceBookItem;
            quantityBookItem = binding.quantityBookItem;
            totalBooksPriceItem = binding.totalBooksPriceItem;
        }
    }

}

