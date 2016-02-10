package tahastudio.storybuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * This class holds all the SQL queries used elsewhere in the app
 */
public class SQLDatabase extends SQLiteOpenHelper {
    // Use only one instance of the database throughout the app. Prevents leakages...
    private static SQLDatabase sbDatabase;

    public SQLDatabase(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Configuration for write-ahead logging, foreign key restraint and other things
    @Override
    public void onConfigure(SQLiteDatabase sbDatabase) {
        super.onConfigure(sbDatabase);
        sbDatabase.setForeignKeyConstraintsEnabled(true);
        sbDatabase.execSQL("PRAGMA foreign_keys=ON;");

    }

    // Create the below tables
    @Override
    public void onCreate(SQLiteDatabase sbDatabase) {
        sbDatabase.execSQL(Constants.SQL_CREATE_STORY_TABLE);
        sbDatabase.execSQL(Constants.SQL_CREATE_CHARACTERS);
        sbDatabase.execSQL(Constants.SQL_CREATE_LOCATIONS);
        sbDatabase.execSQL(Constants.SQL_CREATE_EVENTS);
        sbDatabase.execSQL(Constants.SQL_CREATE_PLOTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sbDatabase, int old_ver, int new_ver) {
        onCreate(sbDatabase);
    }

    // Get one synchronized instance of the database across the app
    public static synchronized SQLDatabase getInstance(Context context) {
        if ( sbDatabase == null ) {
            sbDatabase = new SQLDatabase(context.getApplicationContext());
        }
        return sbDatabase;
    }

    // Get the story ID back from database to create the other tables
    // Location: CreateStoryTask
    public Integer getStoryID() {
        SQLiteDatabase helper = getReadableDatabase();
        Cursor cursor = helper.rawQuery(Constants.GET_STORY_ID, null);

        cursor.moveToFirst(); // Move to the first position
        int the_id = cursor.getInt(0);
        cursor.close(); // Close the cursor when done

        return the_id;
    }

    // Get the story element user clicked on
    // Location: AddCharacters, AddLocations, AddEvents
    public Cursor getElementRow(String query, String name, int id) {
        SQLiteDatabase helper = getReadableDatabase();
        return helper.rawQuery(query + id, null);
    }

    // Get the story genre to set the drawable
    // Location: Story
    public Cursor getStoryGenre() {
        SQLiteDatabase helper = getReadableDatabase();
        return helper.rawQuery(Constants.GET_STORY_GENRE + Constants.SB_ID, null);
    }

    // For the ListViews in StoryBuilderMain, AddCharacters, AddEvents, AddLocations
    public Cursor getSearchResults(String id,
                                   String[] projection,
                                   String selection,
                                   String[] selectionArgs,
                                   String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        // Tables to search
        builder.setTables(id);

        return builder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    // Insert an entry into the database. StoryProvider is the middleman
    public long addEntry(Uri uri, ContentValues values) throws SQLException {

        int uriType = Constants.uriMatcher.match(uri); // Find the table by the uri

        // Call the FindTable class to use its switch statement
        FindTable getTable = new FindTable();
        String table = getTable.findTheTable(uriType); // Get the table by the uri code

        long id = getWritableDatabase().insert(table, "", values);

        if ( id <= 0 ) {
            throw new SQLException("Failed to add entry");
        }
        return id;
    }

    // StoryProvider is the middleman
    public int deleteEntry(String table, String selection, String[] selectionArgs) {

        return getWritableDatabase().delete(table, selection, selectionArgs);
    }

    // StoryProvider is the middleman
    public int updateEntry(String id, ContentValues values, String selection,
                           String[] selectionArgs) {
        if ( id == null ) {
            return getWritableDatabase().update(null, values, null, null);
        } else {
            return getWritableDatabase().update(id, values, selection, selectionArgs);
        }
    }
}
