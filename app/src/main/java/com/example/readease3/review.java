package com.example.readease3;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.readease3.ui.search.search_fragment;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class review extends AppCompatActivity {

    private EditText reviewEditText;
    private String searchedBookISBN; // Declare variable to store ISBN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.review);

        // Retrieve the ISBN passed from search_fragment
        searchedBookISBN = getIntent().getStringExtra("searched_book_isbn");

        // Find views
        reviewEditText = findViewById(R.id.editText);
        Button submitButton = findViewById(R.id.submit1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewText = reviewEditText.getText().toString().trim();

                if (!reviewText.isEmpty()) {
                    // Use the retrieved ISBN in the insertReview method
                    DBHandler dbHandler = new DBHandler(review.this);
                    dbHandler.insertReview(1, reviewText, searchedBookISBN); // Pass actual ISBN
                    Toast.makeText(review.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(review.this, "Please enter your review", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Apply window insets listener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}