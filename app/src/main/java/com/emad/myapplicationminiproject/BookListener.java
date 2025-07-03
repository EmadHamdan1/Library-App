package com.emad.myapplicationminiproject;

public interface BookListener {
    void onDeleteBook(int id);

    void onEditBook(int id, int pos);

    void onItemBook(int pos);
}
