package tahastudio.storybuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

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
        Log.d("Database", "Updating table from " + old_ver + " to " + new_ver);

        // If the database version is older than 5, then we need to do the migration,
        // as column names have changed. SQLite doesn't allow for easy renaming of
        // column names, so we first need to rename the entire table, create a new table,
        // transfer all the data to to the new table, and then drop the old table.
        if (old_ver < 5) {
            // Alter the old table name
            sbDatabase.execSQL("ALTER TABLE " + Constants.STORY_TABLE
                    + " RENAME TO " + Constants.STORY_TABLE + "_old");
            // Create a new table to transfer data to, using the old table name
            sbDatabase.execSQL(Constants.SQL_CREATE_STORY_TABLE);
            // Transfer the data over
            sbDatabase.execSQL("INSERT INTO " + Constants.STORY_TABLE + "(" +
                    Constants.STORY_NAME + "," +
                    Constants.STORY_NAME + "," +
                    Constants.STORY_GENRE + "," +
                    Constants.STORY_DESC + ")"
                    + " SELECT " +
                    Constants.STORY_NAME + "," +
                    Constants.STORY_NAME + "," +
                    Constants.STORY_GENRE + "," +
                    Constants.STORY_DESC
                    + " FROM " + Constants.STORY_TABLE + "_old");
            // Now drop the old table
            sbDatabase.execSQL("DROP TABLE " + Constants.STORY_TABLE + "_old");

            // Alter the old table name
            sbDatabase.execSQL("ALTER TABLE " + Constants.STORY_CHARACTER_TABLE
                    + " RENAME TO " + Constants.STORY_CHARACTER_TABLE + "_old");
            // Create a new table to transfer data to, using the old table name
            sbDatabase.execSQL(Constants.SQL_CREATE_CHARACTERS);
            // Transfer the data over
            sbDatabase.execSQL("INSERT INTO " + Constants.STORY_CHARACTER_TABLE + "(" +
                    Constants.STORY_CHARACTER_NAME + "," +
                    Constants.STORY_CHARACTER_TYPE + "," +
                    Constants.STORY_CHARACTER_GENDER + "," +
                    Constants.STORY_CHARACTER_AGE + "," +
                    Constants.STORY_CHARACTER_BIRTHPLACE + "," +
                    Constants.STORY_CHARACTER_HISTORY + "," +
                    Constants.STORY_CHARACTER_GOALS + "," +
                    Constants.STORY_CHARACTER_CONFLICTS + "," +
                    Constants.STORY_CHARACTER_EPIPHANY + "," +
                    Constants.STORY_CHARACTER_PERSONALITY + "," +
                    Constants.STORY_CHARACTER_STORYLINE + "," +
                    Constants.STORY_CHARACTER_NOTES + "," +
                    Constants.DB_ID + ")"
                    + " SELECT " +
                    Constants.STORY_CHARACTER_NAME  + "," +
                    Constants.STORY_CHARACTER_TYPE + "," +
                    Constants.STORY_CHARACTER_GENDER + "," +
                    Constants.STORY_CHARACTER_AGE + "," +
                    Constants.STORY_CHARACTER_BIRTHPLACE + "," +
                    Constants.STORY_CHARACTER_HISTORY + "," +
                    Constants.STORY_CHARACTER_GOALS + "," +
                    Constants.STORY_CHARACTER_CONFLICTS + "," +
                    Constants.STORY_CHARACTER_EPIPHANY + "," +
                    Constants.STORY_CHARACTER_PERSONALITY + "," +
                    Constants.STORY_CHARACTER_STORYLINE + "," +
                    Constants.STORY_CHARACTER_NOTES + "," +
                    Constants.DB_ID + " FROM " + Constants.STORY_CHARACTER_TABLE + "_old");
            // Now drop the old table
            sbDatabase.execSQL("DROP TABLE " + Constants.STORY_CHARACTER_TABLE + "_old");

            // Alter the old table name
            sbDatabase.execSQL("ALTER TABLE " + Constants.STORY_EVENT_TABLE
                    + " RENAME TO " + Constants.STORY_EVENT_TABLE + "_old");
            // Create a new table to transfer data to, using the old table name
            sbDatabase.execSQL(Constants.SQL_CREATE_EVENTS);
            // Transfer the data over
            sbDatabase.execSQL("INSERT INTO " + Constants.STORY_EVENT_TABLE + "(" +
                    Constants.STORY_EVENT_LINER + "," +
                    Constants.STORY_EVENT_DESC + "," +
                    Constants.STORY_EVENT_CHARACTERS + "," +
                    Constants.STORY_EVENT_SUMMARY + "," +
                    Constants.STORY_EVENT_NOTES + "," +
                    Constants.DB_ID + ")"
                    + " SELECT " +
                    Constants.STORY_EVENT_LINER + "," +
                    Constants.STORY_EVENT_DESC + "," +
                    Constants.STORY_EVENT_CHARACTERS + "," +
                    Constants.STORY_EVENT_SUMMARY + "," +
                    Constants.STORY_EVENT_NOTES + "," +
                    Constants.DB_ID + " FROM " + Constants.STORY_EVENT_TABLE + "_old");
            // Now drop the old table
            sbDatabase.execSQL("DROP TABLE " + Constants.STORY_EVENT_TABLE + "_old");

            // Alter the old table name
            sbDatabase.execSQL("ALTER TABLE " + Constants.STORY_LOCATION_TABLE
                    + " RENAME TO " + Constants.STORY_LOCATION_TABLE + "_old");
            // Create a new table to transfer data to, using the old table name
            sbDatabase.execSQL(Constants.SQL_CREATE_LOCATIONS);
            // Transfer the data over
            sbDatabase.execSQL("INSERT INTO " + Constants.STORY_LOCATION_TABLE + "(" +
                    Constants.STORY_LOCATION_NAME + "," +
                    Constants.STORY_LOCATION_LOCATION + "," +
                    Constants.STORY_LOCATION_DESC + "," +
                    Constants.STORY_LOCATION_IMPORTANCE + "," +
                    Constants.STORY_LOCATION_EVENTS + "," +
                    Constants.STORY_LOCATION_NOTES + "," +
                    Constants.DB_ID + ")"
                    + " SELECT " +
                    Constants.STORY_LOCATION_NAME + "," +
                    Constants.STORY_LOCATION_LOCATION + "," +
                    Constants.STORY_LOCATION_DESC + "," +
                    Constants.STORY_LOCATION_IMPORTANCE + "," +
                    Constants.STORY_LOCATION_EVENTS + "," +
                    Constants.STORY_LOCATION_NOTES + "," +
                    Constants.DB_ID + " FROM " + Constants.STORY_LOCATION_TABLE + "_old");
            // Now drop the old table
            sbDatabase.execSQL("DROP TABLE " + Constants.STORY_LOCATION_TABLE + "_old");

            // Alter the old table name
            sbDatabase.execSQL("ALTER TABLE " + Constants.STORY_PLOT_TABLE
                    + " RENAME TO " + Constants.STORY_PLOT_TABLE + "_old");
            // Create a new table to transfer data to, using the old table name
            sbDatabase.execSQL(Constants.SQL_CREATE_PLOTS);
            // Transfer the data over
            sbDatabase.execSQL("INSERT INTO " + Constants.STORY_PLOT_TABLE + "(" +
                    Constants.STORY_PLOT_TITLE + "," +
                    Constants.STORY_PLOT_MAIN + "," +
                    Constants.STORY_PLOT_MISC + "," +
                    Constants.STORY_PLOT_DESC + "," +
                    Constants.STORY_PLOT_NOTES + "," +
                    Constants.DB_ID + ")"
                    + " SELECT " +
                    Constants.STORY_PLOT_TITLE + "," +
                    Constants.STORY_PLOT_MAIN + "," +
                    Constants.STORY_PLOT_MISC + "," +
                    Constants.STORY_PLOT_DESC + "," +
                    Constants.STORY_PLOT_NOTES + "," +
                    Constants.DB_ID + " FROM " + Constants.STORY_PLOT_TABLE + "_old");
            // Now drop the old table
            sbDatabase.execSQL("DROP TABLE " + Constants.STORY_PLOT_TABLE + "_old");
        }
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
