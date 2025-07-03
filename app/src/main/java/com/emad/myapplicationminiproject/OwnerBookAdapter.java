package com.emad.myapplicationminiproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emad.myapplicationminiproject.databinding.ItemBookOwnerActivityBinding;

import java.util.ArrayList;

public class OwnerBookAdapter extends RecyclerView.Adapter<OwnerBookAdapter.OwnerBookViewHolder> {

    ArrayList<Book> books;
    BookListener listener;

    public OwnerBookAdapter(ArrayList<Book> books, BookListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OwnerBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookOwnerActivityBinding binding = ItemBookOwnerActivityBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OwnerBookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerBookViewHolder holder, int position) {

        Book book = books.get(position);

        holder.imageBookIvItem.setImageBitmap(book.getBookImage());
        holder.nameBookItem.setText(book.getName());
        holder.priceBookItem.setText(String.valueOf(book.getPrice()));
        holder.quantityBookItem.setText(String.valueOf(book.getQuantity()));

        holder.deleteBookIvItem.setOnClickListener(view -> {
            listener.onDeleteBook(book.getId());
        });

        holder.editBookIvItem.setOnClickListener(view -> {
            listener.onEditBook(book.getId(), holder.getAdapterPosition());
        });

        holder.itemView.setOnClickListener(view -> {
            listener.onItemBook(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class OwnerBookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageBookIvItem, deleteBookIvItem, editBookIvItem;
        TextView nameBookItem, priceBookItem, quantityBookItem;

        public OwnerBookViewHolder(ItemBookOwnerActivityBinding binding) {
            super(binding.getRoot());
            imageBookIvItem = binding.imageBookIvItem;
            deleteBookIvItem = binding.deleteBookIvItem;
            editBookIvItem = binding.editBookIvItem;
            nameBookItem = binding.nameBookItem;
            priceBookItem = binding.priceBookItem;
            quantityBookItem = binding.quantityBookItem;
        }
    }

}
