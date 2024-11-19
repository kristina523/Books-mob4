package com.example.books;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_add_books extends AppCompatActivity {
    private EditText editbookName, editbookAuthor;
    private DataBaseHelper dbHelper;
    private Button addButton;
    private int bookId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_books);

        editbookName = findViewById(R.id.editTextName);
        editbookAuthor = findViewById(R.id.editTextAuthor);
        addButton = findViewById(R.id.add);
        dbHelper = new DataBaseHelper(this);

        // Получаем переданный ID для редактирования (если есть)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BOOK_ID")) {
            bookId = intent.getIntExtra("BOOK_ID", -1);
            loadBookDetails(bookId);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateBookInDatabase();
            }
        });
    }

    private void loadBookDetails(int bookId) {
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID));
                if (id == bookId) {
                    editbookName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_NAME)));
                    editbookAuthor.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_AUTHOR)));
                    break;
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
    }

    private void addOrUpdateBookInDatabase() {
        String bookName = editbookName.getText().toString().trim();
        String bookAuthor = editbookAuthor.getText().toString().trim();

        if (bookName.isEmpty() || bookAuthor.isEmpty()) {
            Toast.makeText(this, "Заполните все поля для ввода", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.addOrUpdateBook(bookId, bookName, bookAuthor);

        if (result > 0) {
            Toast.makeText(this, "Книга успешно добавлена/обновлена", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Activity_add_books.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Ошибка при добавлении/обновлении", Toast.LENGTH_SHORT).show();
        }
    }
}