package com.example.books;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityBookDetails extends AppCompatActivity {
    private EditText editBookName, editBookAuthor;
    private Button btnUpdate, btnDelete;
    private DataBaseHelper dbHelper;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        editBookName = findViewById(R.id.editTextName);
        editBookAuthor = findViewById(R.id.editTextAuthor);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        dbHelper = new DataBaseHelper(this);

        Intent intent = getIntent();
        bookId = intent.getIntExtra("BOOK_ID", -1);

        if (bookId != -1) {
            loadBookDetails();
        } else {
            Toast.makeText(this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdate.setOnClickListener(v -> updateBook());
        btnDelete.setOnClickListener(v -> deleteBook());
    }

    private void loadBookDetails() {
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID));
                if (id == bookId) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_NAME));
                    String author = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_AUTHOR));

                    editBookName.setText(name);
                    editBookAuthor.setText(author);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void updateBook() {
        String bookName = editBookName.getText().toString().trim();
        String bookAuthor = editBookAuthor.getText().toString().trim();

        if (bookName.isEmpty() || bookAuthor.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.addOrUpdateBook(bookId, bookName, bookAuthor);

        if (result > 0) {
            Toast.makeText(this, "Книга обновлена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        int result = dbHelper.deleteBookById(bookId);

        if (result > 0) {
            Toast.makeText(this, "Книга удалена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
        }
    }
}