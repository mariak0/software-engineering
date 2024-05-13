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
                "ebook_id INTEGER PRIMARY KEY ," +
                "ebook_author TEXT NOT NULL DEFAULT 'unknown'," +
                "ebook_description TEXT NOT NULL," +
                "price INTEGER NOT NULL DEFAULT 0" +
                ")";

        db.execSQL(createEbookTableQuery);

        // Create the events table
        String createEventsTableQuery = "CREATE TABLE events (" +
                "event_id INTEGER PRIMARY KEY NOT NULL," +
                "event_title TEXT NOT NULL," +
                "event_description TEXT NOT NULL DEFAULT 'unknown'," +
                "date DATE NOT NULL," +
                "start_time TEXT NOT NULL," +
                "end_time TEXT NOT NULL," +
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

        // Create the review table
        String createReviewTableQuery = "CREATE TABLE review ("
                + "review_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "reviewer INT NOT NULL,"
                + "review TEXT,"
                + "reviewed_book VARCHAR(155) NOT NULL,"
                + "FOREIGN KEY (reviewer) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE,"
                + "FOREIGN KEY (reviewed_book) REFERENCES book(isbn) ON UPDATE CASCADE ON DELETE CASCADE"
                + ")";
        db.execSQL(createReviewTableQuery);

        //quiz table
        String createQuizTableQuery = "CREATE TABLE quiz (" +
                "quiz_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL" +
                ");";
        db.execSQL(createQuizTableQuery);

        //questions table
        String createQuestionsTableQuery = "CREATE TABLE questions (" +
                "question_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "quiz_id INTEGER NOT NULL," +
                "question_text TEXT NOT NULL," +
                "FOREIGN KEY (quiz_id) REFERENCES quiz(quiz_id) ON DELETE CASCADE" +
                ");";
        db.execSQL(createQuestionsTableQuery);

        //options table
        String createOptionsTableQuery = "CREATE TABLE options (" +
                "option_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "question_id INTEGER NOT NULL," +
                "option_text TEXT NOT NULL," +
                "is_correct INTEGER NOT NULL, " + // 0 for false, 1 for true
                "FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE" +
                ");";
        db.execSQL(createOptionsTableQuery);



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
        insertSellingAd(db, "9786810146189", 9, 2, "ΚΑΛΗ");

        // Insert records into events table
        insertEvents(db,82224,"'Βιβλιοφάγοι'","Μια πρώτη γνωριμία με τον δημιουργό του έργου 'Βιβλιοφάγοι'","2024-08-22", "15:30","17:00 PM","Παπανδρέου 20, Πάτρα",50,1 );
        insertEvents(db,81324,"Βιβλιοδεσίες","Η Πρώτη μας συνάντηση στον Βιβλιοπωλείο 'Βιβλιοδεσίες'","2024-04-10", "10:00 PM","13:30 AM","Ανθείας 09, Πάτρα",100,1 );

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

    private void insertEvents(SQLiteDatabase db, int eventId, String title, String description, String date,String start_time,String end_time, String location, int capacity, int creator) {
        ContentValues values = new ContentValues();
        values.put("event_id", eventId);
        values.put("event_title", title);
        values.put("event_description", description);
        values.put("date", date);
        values.put("start_time", start_time);
        values.put("end_time", end_time);
        values.put("event_location", location);
        values.put("capacity", capacity );
        values.put("writer_creator", creator );

        db.insert("events", null, values);
    }

    public void insertReview(int reviewerId, String reviewText, String reviewedBook) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reviewer", reviewerId);
        values.put("review", reviewText);
        values.put("reviewed_book", reviewedBook);
        db.insert("review", null, values);
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

    public List<events> returnEventsInfo() {
        List<events> eventsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to search for events
        String query = "SELECT * FROM events";

        // Execute the query
        Cursor cursor = db.rawQuery(query, null);

        // Iterate through the cursor to retrieve events
        if (cursor.moveToFirst()) {
            do {
                int eventIdIndex = cursor.getColumnIndex("event_id");
                int titleIndex = cursor.getColumnIndex("event_title");
                int descriptionIndex = cursor.getColumnIndex("event_description");
                int dateIndex = cursor.getColumnIndex("date");
                int starttimeIndex = cursor.getColumnIndex("start_time");
                int endtimeIndex = cursor.getColumnIndex("end_time");
                int locationIndex = cursor.getColumnIndex("event_location");
                int capacityIndex = cursor.getColumnIndex("capacity");
                int creatorIndex = cursor.getColumnIndex("writer_creator");

                // Check if column indices are valid
                if (eventIdIndex != -1 && titleIndex != -1 && descriptionIndex != -1 &&
                        dateIndex != -1 && starttimeIndex != -1 && endtimeIndex != -1 && locationIndex != -1 && capacityIndex != -1 && creatorIndex != -1) {
                    int eventId = cursor.getInt(eventIdIndex);
                    String title = cursor.getString(titleIndex);
                    String description = cursor.getString(descriptionIndex);
                    String date = cursor.getString(dateIndex);
                    String startTime = cursor.getString(starttimeIndex);
                    String endTime = cursor.getString(endtimeIndex);
                    String location = cursor.getString(locationIndex);
                    int capacity = cursor.getInt(capacityIndex);
                    int creator = cursor.getInt(creatorIndex);

                    // Create an Event object for each row and add it to the list
                    events event = new events(eventId, title, description, date,startTime,endTime, location, capacity, creator);
                    eventsList.add(event);
                } else {
                    // Handle case where one or more columns are missing
                    // Log a warning or take appropriate action
                }
            } while (cursor.moveToNext());
        }
        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the list of events
        return eventsList;
    }



    public List<sellingAd> getSellingAdByIsbn(String isbn) {
        List<sellingAd> sellingAds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to search for selling ads by ISBN
        String query = "SELECT * FROM selling_ad WHERE selling_ad_isbn = ?";

        // Execute the query with the ISBN parameter
        Cursor cursor = db.rawQuery(query, new String[]{isbn});

        // Iterate through the cursor to retrieve selling ads
        if (cursor.moveToFirst()) {
            do {
                int sellingAdIdIndex = cursor.getColumnIndex("selling_ad_id");
                int sellingPriceIndex = cursor.getColumnIndex("selling_price");
                int sellingPublisherIndex = cursor.getColumnIndex("selling_publisher");
                int sellingStatusIndex = cursor.getColumnIndex("selling_status");

                // Check if column indices are valid
                if (sellingAdIdIndex != -1 && sellingPriceIndex != -1 &&
                        sellingPublisherIndex != -1 && sellingStatusIndex != -1) {
                    // Retrieve data from the cursor and create a SellingAd object
                    int sellingAdId = cursor.getInt(sellingAdIdIndex);
                    float sellingPrice = cursor.getFloat(sellingPriceIndex);
                    int sellingPublisher = cursor.getInt(sellingPublisherIndex);
                    String sellingStatus = cursor.getString(sellingStatusIndex);

                    // Create SellingAd object and add it to the list
                    sellingAd sellingAd = new sellingAd(sellingAdId, isbn, sellingPrice, sellingPublisher, sellingStatus);
                    sellingAds.add(sellingAd);
                }
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the list of selling ads
        return sellingAds;
    }


    // Method to retrieve user name by user ID
    public String getUserNameById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;

        // Define the query
        String query = "SELECT name FROM user WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        // Check if the cursor has data and the column index is valid
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            if (nameIndex >= 0) {
                // Retrieve user name from the cursor
                userName = cursor.getString(nameIndex);
            }
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return userName;
    }

}