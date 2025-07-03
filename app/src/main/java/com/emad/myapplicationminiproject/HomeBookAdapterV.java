package com.emad.myapplicationminiproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emad.myapplicationminiproject.databinding.ItemBookHomeActicityVBinding;

import java.util.ArrayList;

public class HomeBookAdapterV extends RecyclerView.Adapter<HomeBookAdapterV.BookHomeViewHolder> {

    ArrayList<Book> books;
    BookListener listener;

    public HomeBookAdapterV(ArrayList<Book> books, BookListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookHomeActicityVBinding binding = ItemBookHomeActicityVBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new BookHomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHomeViewHolder holder, int position) {

        Book book = books.get(position);

        holder.nameBookItem.setText(book.getName());
        holder.priceBookItem.setText(String.valueOf(book.getPrice()));
        holder.quantityBookItem.setText(String.valueOf(book.getQuantity()));
        holder.imageBookIvItem.setImageBitmap(book.getBookImage());

        holder.itemView.setOnClickListener(view -> {

            listener.onItemBook(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookHomeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBookIvItem;
        TextView nameBookItem, priceBookItem, quantityBookItem;

        public BookHomeViewHolder(ItemBookHomeActicityVBinding binding) {
            super(binding.getRoot());

            imageBookIvItem = binding.imageBookIvItem;
            nameBookItem = binding.nameBookItem;
            priceBookItem = binding.priceBookItem;
            quantityBookItem = binding.quantityBookItem;

        }
    }
}
