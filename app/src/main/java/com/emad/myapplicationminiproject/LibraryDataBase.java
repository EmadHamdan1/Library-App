package com.emad.myapplicationminiproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LibraryDataBase extends SQLiteOpenHelper {

    // Auth Table
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";
    public static final String IMAGE_COLUMN = "image";

    // USERS_TABLE
    public static final String USERS_TABLE = "users";
    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";
    public static final String USERTYPE_COLUMN = "userType";

    // BOOKS_TABLE
    public static final String BOOKS_TABLE = "books";
    public static final String PRICE_COLUMN = "price";
    public static final String QUANTITY_COLUMN = "quantity";
    public static final String ISTAKE_COLUMN = "isTake";
    public static final String DISCRIP_COLUMN = "discription";
    public static final String TAKEPRICE_COLUMN = "tackPrice";
    public static final String OWNER_ID_COLUMN = "ownerId";


    public LibraryDataBase(@Nullable Context context) {
        super(context, "library_database", null, 3);
    }


    // لجعل المفاتيج الاجنبية تشتغل
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USERS_TABLE + " (" + ID_COLUMN + " integer primary key autoincrement," +
                NAME_COLUMN + " text , " + EMAIL_COLUMN + " text UNIQUE, " + PASSWORD_COLUMN + " text," +
                IMAGE_COLUMN + " BLOB , " + USERTYPE_COLUMN + " text) ");

        sqLiteDatabase.execSQL("CREATE TABLE " + BOOKS_TABLE + " (" + ID_COLUMN + " integer primary key autoincrement," +
                NAME_COLUMN + " text , " + PRICE_COLUMN + " REAL, " + QUANTITY_COLUMN + " integer," +
                IMAGE_COLUMN + " BLOB , " + ISTAKE_COLUMN + " boolean , " + DISCRIP_COLUMN + " text , "
                + TAKEPRICE_COLUMN + " REAL , " + OWNER_ID_COLUMN + " INTEGER," +
                "FOREIGN KEY(" + OWNER_ID_COLUMN + ") REFERENCES  " + USERS_TABLE + "(" + ID_COLUMN + ")" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop Table If Exists " + USERS_TABLE);
        sqLiteDatabase.execSQL("Drop Table If Exists " + BOOKS_TABLE);
        onCreate(sqLiteDatabase);
    }

    //---------> User Method

    // Insert User
    public long insertUser(User user) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, user.getName());
        cv.put(EMAIL_COLUMN, user.getEmail());
        cv.put(PASSWORD_COLUMN, user.getPassword());
        cv.put(USERTYPE_COLUMN, user.getUsertype());
        cv.put(IMAGE_COLUMN, Utilities.bitmapToBytes(user.getUserImage()));

        return db.insert(USERS_TABLE, null, cv);
    }

    // Update User
    public int updateUser(User user) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, user.getName());
        cv.put(EMAIL_COLUMN, user.getEmail());
        cv.put(PASSWORD_COLUMN, user.getPassword());
        cv.put(USERTYPE_COLUMN, user.getUsertype());
        cv.put(IMAGE_COLUMN, Utilities.bitmapToBytes(user.getUserImage()));

        return db.update(USERS_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(user.getId())});
    }

    // Update User
    public int updateUserPassword(User user) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(PASSWORD_COLUMN, user.getPassword());

        return db.update(USERS_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public ArrayList<User> readAllUsers() {

        Bitmap image = null;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from " + USERS_TABLE, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_COLUMN));
            String userType = cursor.getString(cursor.getColumnIndexOrThrow(USERTYPE_COLUMN));

            byte[] imageAsByte = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

            if (imageAsByte != null) {
                image = Utilities.bytesToBitmap(imageAsByte);
            }

            users.add(new User(id, name, email, password, userType, image));
        }
        cursor.close();
        db.close();
        return users;
    }

    public User getUserByEmail(String email) {

        Bitmap image = null;
        User user = null;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + EMAIL_COLUMN + " = ?", new String[]{email}, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_COLUMN));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_COLUMN));
            String userType = cursor.getString(cursor.getColumnIndexOrThrow(USERTYPE_COLUMN));

            byte[] imageAsByte = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

            if (imageAsByte != null) {
                image = Utilities.bytesToBitmap(imageAsByte);
            }

            user = new User(id, name, userEmail, password, userType, image);
        }
        cursor.close();
        return user;
    }


    //---------> Book Method

    // Insert Book
    public long insertBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, book.getName());
        cv.put(PRICE_COLUMN, book.getPrice());
        cv.put(QUANTITY_COLUMN, book.getQuantity());
        cv.put(ISTAKE_COLUMN, book.getIsTake());
        cv.put(TAKEPRICE_COLUMN, book.getTakePrice());
        cv.put(IMAGE_COLUMN, Utilities.bitmapToBytes(book.getBookImage()));
        cv.put(DISCRIP_COLUMN, book.getDisc());
        cv.put(OWNER_ID_COLUMN, book.getOwnerId());

        return db.insert(BOOKS_TABLE, null, cv);
    }

    // Delete Book
    public int deleteBook(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(BOOKS_TABLE, ID_COLUMN + " = ?", new String[]{String.valueOf(id)});
    }

    // Update Book
    public int updateBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(NAME_COLUMN, book.getName());
        cv.put(PRICE_COLUMN, book.getPrice());
        cv.put(QUANTITY_COLUMN, book.getQuantity());
        cv.put(ISTAKE_COLUMN, book.getIsTake());
        cv.put(TAKEPRICE_COLUMN, book.getTakePrice());
        cv.put(IMAGE_COLUMN, Utilities.bitmapToBytes(book.getBookImage()));
        cv.put(DISCRIP_COLUMN, book.getDisc());

        return db.update(BOOKS_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(book.getId())});
    }

    // Update Quantity Book
    public int updateQuantityBookMinus(Book book, int count) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(QUANTITY_COLUMN, book.getQuantity() - count);
        return db.update(BOOKS_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(book.getId())});
    }

    public int updateQuantityBookPlus(Book book, int count) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(QUANTITY_COLUMN, book.getQuantity() + count);
        return db.update(BOOKS_TABLE, cv, ID_COLUMN + " = ?", new String[]{String.valueOf(book.getId())});
    }


    //Read All Books
    public ArrayList<Book> readAllBooks() {

        Bitmap image = null;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Book> books = new ArrayList<>();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from " + BOOKS_TABLE, null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
            int ownerId = cursor.getInt(cursor.getColumnIndexOrThrow(OWNER_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_COLUMN));
            double takePrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TAKEPRICE_COLUMN));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY_COLUMN));
            int isTakeInt = cursor.getInt(cursor.getColumnIndexOrThrow(ISTAKE_COLUMN));
            boolean isTake = (isTakeInt == 1);
            String discrip = cursor.getString(cursor.getColumnIndexOrThrow(DISCRIP_COLUMN));
            byte[] imageAsByte = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

            if (imageAsByte != null) {
                image = Utilities.bytesToBitmap(imageAsByte);
            }

            books.add(new Book(id, name, price, quantity, isTake, takePrice, image, discrip, ownerId));
        }
        cursor.close();
        db.close();
        return books;
    }


    public ArrayList<Book> readBooksByOwnerId(int ownerId) {

        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Book> books = new ArrayList<>();

        Bitmap image = null;

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("Select * from " + BOOKS_TABLE + " Where "
                + OWNER_ID_COLUMN + " = ?", new String[]{String.valueOf(ownerId)});

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
            int owner_id = cursor.getInt(cursor.getColumnIndexOrThrow(OWNER_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_COLUMN));
            double takePrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TAKEPRICE_COLUMN));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY_COLUMN));
            int isTakeInt = cursor.getInt(cursor.getColumnIndexOrThrow(ISTAKE_COLUMN));
            boolean isTake = (isTakeInt == 1);
            String discrip = cursor.getString(cursor.getColumnIndexOrThrow(DISCRIP_COLUMN));
            byte[] imageAsByte = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

            if (imageAsByte != null) {
                image = Utilities.bytesToBitmap(imageAsByte);
            }

            books.add(new Book(id, name, price, quantity, isTake, takePrice, image, discrip, owner_id));
        }
        cursor.close();
        return books;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books WHERE id = ?", new String[]{String.valueOf(id)});
        Bitmap image = null;
        Book book = null;
        if (cursor.moveToFirst()) {
            int bookId = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMN));
            int owner_id = cursor.getInt(cursor.getColumnIndexOrThrow(OWNER_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE_COLUMN));
            double takePrice = cursor.getDouble(cursor.getColumnIndexOrThrow(TAKEPRICE_COLUMN));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY_COLUMN));
            int isTakeInt = cursor.getInt(cursor.getColumnIndexOrThrow(ISTAKE_COLUMN));
            boolean isTake = (isTakeInt == 1);
            String discrip = cursor.getString(cursor.getColumnIndexOrThrow(DISCRIP_COLUMN));
            byte[] imageAsByte = cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE_COLUMN));

            if (imageAsByte != null) {
                image = Utilities.bytesToBitmap(imageAsByte);
            }

            book = new Book(bookId, name, price, quantity, isTake, takePrice, image, discrip, owner_id);
        }

        cursor.close();
        return book;
    }

}
