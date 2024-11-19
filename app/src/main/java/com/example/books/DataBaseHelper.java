package com.example.books;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "book";
    static final String TABLE_NAME = "books";
    private static final int SCHEMA = 1;
    public static final String COLUMN_ID = "id_book";
    public static final String COLUMN_NAME = "book_name";
    public static final String COLUMN_AUTHOR = "book_author";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AUTHOR + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Метод добавления или обновления книги
    public long addOrUpdateBook(int bookId, String bookName, String bookAuthor) {
        SQLiteDatabase db = getWritableDatabase();

        // Проверяем, существует ли книга с данным ID
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookId)}, null, null, null);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, bookName);
        values.put(COLUMN_AUTHOR, bookAuthor);

        long result;
        if (cursor.moveToFirst()) {

            result = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookId)});
        } else {

            if (bookId > 0) {
                values.put(COLUMN_ID, bookId);
            }
            result = db.insert(TABLE_NAME, null, values);
        }

        cursor.close();
        db.close();
        return result;
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public int deleteBookById(long bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(bookId)});
        db.close();
        return result;
    }
}