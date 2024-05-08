package com.example.readease3;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the book table
        String createTableQuery = "CREATE TABLE book (" +
                "isbn TEXT PRIMARY KEY," +
                "title TEXT NOT NULL DEFAULT 'unknown'," +
                "book_description TEXT NOT NULL," +
                "book_author TEXT NOT NULL DEFAULT 'unknown'," +
                "pages INTEGER NOT NULL DEFAULT 0," +
                "category TEXT CHECK(category IN ('Αισθηματικα', 'Επιστημονικης Φαντασιας', 'Αστυνομικα', 'Αυτοβελτιωση'))" +
                ")";

        db.execSQL(createTableQuery);

        // Create the user table
        String createUserTableQuery = "CREATE TABLE user (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "type TEXT CHECK(type IN ('USER', 'WRITER'))," +
                "mail TEXT NOT NULL," +
                "phone INTEGER NOT NULL," +
                "user_location TEXT NOT NULL," +
                "points INTEGER" +
                ")";
        db.execSQL(createUserTableQuery);


        // Create the ebook table
        String createEbookTableQuery = "CREATE TABLE ebook (" +
                "ebook_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ebook_author TEXT NOT NULL DEFAULT 'unknown'," +
                "ebook_description TEXT NOT NULL," +
                "price INTEGER NOT NULL DEFAULT 0" +
                ")";

        db.execSQL(createEbookTableQuery);

        // Create the events table
        String createEventsTableQuery = "CREATE TABLE events (" +
                "event_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "event_title TEXT NOT NULL," +
                "event_description TEXT NOT NULL DEFAULT 'unknown'," +
                "date_time DATETIME NOT NULL," +
                "event_location TEXT NOT NULL," +
                "capacity INTEGER NOT NULL," +
                "writer_creator INTEGER," +
                "CONSTRAINT CREATOR FOREIGN KEY (writer_creator) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";
        db.execSQL(createEventsTableQuery);

        // Create the participate table
        String createParticipateTableQuery = "CREATE TABLE participate (" +
                "participants_user_id INTEGER NOT NULL," +
                "participate_event_id INTEGER NOT NULL," +
                "PRIMARY KEY (participants_user_id, participate_event_id)," +
                "CONSTRAINT PARTICIPANTS FOREIGN KEY (participants_user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "CONSTRAINT EVENT FOREIGN KEY (participate_event_id) REFERENCES events(event_id) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";
        db.execSQL(createParticipateTableQuery);



        // Create the selling_ad table
        String createSellingAdTableQuery = "CREATE TABLE selling_ad (" +
                "selling_ad_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "selling_ad_isbn TEXT NOT NULL," +
                "selling_price REAL NOT NULL," +
                "selling_publisher INTEGER," +
                "selling_status TEXT CHECK(selling_status IN ('ΚΑΚΗ', 'ΜΕΤΡΙΑ', 'ΚΑΛΗ', 'ΠΟΛΥ ΚΑΛΗ'))," +
                "FOREIGN KEY (selling_ad_isbn) REFERENCES book(isbn) ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY (selling_publisher) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";
        db.execSQL(createSellingAdTableQuery);

        // Insert initial books
        insertBook(db, "9786180149173", "Ψιλά Γράμματα", "LAUREN ASHER", "Description of the book", 445, "Αισθηματικα");
        insertBook(db, "9786810146189", "Το τρίτο κορίτσι", "Agatha Christie", "Description of the book", 277, "Αστυνομικα");

        // Insert some random values into the user table
        insertUser(db, "John Doe", "123", "USER", "john@example.com", 123456789, "New York", 100);
        insertUser(db, "Jane Smith", "456", "WRITER", "jane@example.com", 987654321, "Los Angeles", 150);
        insertUser(db, "Alice Johnson", "789", "USER", "alice@example.com", 555555555, "Chicago", 200);

        // Insert records into the selling_ad table
        insertSellingAd(db, "9786180149173", 18, 1, "ΚΑΛΗ");
        insertSellingAd(db, "9786810146189", 12, 3, "ΠΟΛΥ ΚΑΛΗ");
    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Implement if needed
        }

        // Method to insert a book into the database
        private void insertBook(SQLiteDatabase db, String isbn, String title, String author, String description, int pages, String category) {
            ContentValues values = new ContentValues();
            values.put("isbn", isbn);
            values.put("title", title);
            values.put("book_description", description);
            values.put("book_author", author);
            values.put("pages", pages);
            values.put("category", category);

            db.insert("book", null, values);
        }


    // Method to insert a user into the user table
    private void insertUser(SQLiteDatabase db, String name, String password, String type, String mail, int phone, String userLocation, int points) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("password", password);
        values.put("type", type);
        values.put("mail", mail);
        values.put("phone", phone);
        values.put("user_location", userLocation);
        values.put("points", points);

        db.insert("user", null, values);
    }
    // Method to insert a selling ad into the selling_ad table
    private void insertSellingAd(SQLiteDatabase db, String isbn, float price, int publisher, String status) {
        ContentValues values = new ContentValues();
        values.put("selling_ad_isbn", isbn);
        values.put("selling_price", price);
        values.put("selling_publisher", publisher);
        values.put("selling_status", status);

        db.insert("selling_ad", null, values);
    }
    public List<Book> searchBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to search for books by title
        String query = "SELECT * FROM book WHERE title LIKE ?";

        // Execute the query with the title parameter
        Cursor cursor = db.rawQuery(query, new String[]{"%" + title + "%"});

        // Iterate through the cursor to retrieve books
        if (cursor.moveToFirst()) {
            do {
                int isbnIndex = cursor.getColumnIndex("isbn");
                int titleIndex = cursor.getColumnIndex("title");
                int authorIndex = cursor.getColumnIndex("book_author");
                int descriptionIndex = cursor.getColumnIndex("book_description");
                int pagesIndex = cursor.getColumnIndex("pages");
                int categoryIndex = cursor.getColumnIndex("category");

                // Check if column indices are valid
                if (isbnIndex != -1 && titleIndex != -1 && authorIndex != -1 &&
                        descriptionIndex != -1 && pagesIndex != -1 && categoryIndex != -1) {
                    String isbn = cursor.getString(isbnIndex);
                    String bookTitle = cursor.getString(titleIndex);
                    String author = cursor.getString(authorIndex);
                    String description = cursor.getString(descriptionIndex);
                    int pages = cursor.getInt(pagesIndex);
                    String category = cursor.getString(categoryIndex);

                    // Create a Book object for each row and add it to the list
                    Book book = new Book(isbn, bookTitle, author, description, pages, category);
                    books.add(book);
                } else {
                    // Handle case where one or more columns are missing
                    // Log a warning or take appropriate action
                }
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the list of books
        return books;
    }


}