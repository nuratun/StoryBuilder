package tahastudio.storybuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * This class holds all the SQL queries used elsewhere in the app
 */
public class SQLDatabase extends SQLiteOpenHelper {
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

    @Override
    public void onCreate(SQLiteDatabase sbDatabase) {
        sbDatabase.execSQL(Constants.SQL_CREATE_STORY_TABLE);
        sbDatabase.execSQL(Constants.SQL_CREATE_CHARACTERS);
        sbDatabase.execSQL(Constants.SQL_CREATE_LOCATIONS);
        sbDatabase.execSQL(Constants.SQL_CREATE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sbDatabase, int old_ver, int new_ver) {
        // TODO -> Remove when going into production
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_CHARACTER_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_EVENT_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_LOCATION_TABLE);
        onCreate(sbDatabase);
    }

    public static synchronized SQLDatabase getInstance(Context context) {
        if ( sbDatabase == null ) {
            sbDatabase = new SQLDatabase(context.getApplicationContext());
        }
        return sbDatabase;
    }

    public void insertRow(ContentValues values, String db) {
        SQLiteDatabase helper = getWritableDatabase();

        helper.insert(db, null, values);
    }

    public void updateRow(ContentValues values, String table, String column, int id) {
        SQLiteDatabase helper = getWritableDatabase();

        // Update the primary row listed in the string column where the value is id
        helper.update(table, values, column + " = " + id, null);
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

    // Get a story ID from database depending on what row user clicked
    // on in StoryBuilderMain
    public Integer findStoryID(String result) {
        SQLiteDatabase helper = getReadableDatabase();
        Cursor cursor =  helper.rawQuery(Constants.FIND_STORY_ID, new String[] { result });

        cursor.moveToFirst(); // Move to the first position
        int the_id = cursor.getInt(0);
        cursor.close(); // Close the cursor when done

        return the_id;
    }

    // Need to dynamically add in the SB_ID to this query, as it will
    // change depending on user story selection
    // Location: AddCharacters, AddLocations, AddEvents
    public Cursor getRows(String query) {
        SQLiteDatabase helper = getReadableDatabase();
        return helper.rawQuery(query, null);
    }

    // Get the story element user clicked on
    // Location: AddCharacters, AddLocations, AddEvents
    public Cursor getElementRow(String query, String name, int id) {
        SQLiteDatabase helper = getReadableDatabase();
        return helper.rawQuery(query + id, null);
    }

    // Get the story genre to set the drawable
    // Location: ShowStory
    public Cursor getStoryGenre() {
        SQLiteDatabase helper = getReadableDatabase();
        return helper.rawQuery(Constants.GET_STORY_GENRE + Constants.SB_ID, null);
    }

    // For user search queries
    public Cursor getSearchResults(String id,
                                   String[] projection,
                                   String selection,
                                   String[] selectionArgs,
                                   String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        // Tables to search
        builder.setTables(Constants.STORY_TABLE);
        builder.setTables(Constants.STORY_CHARACTER_TABLE);
        builder.setTables(Constants.STORY_LOCATION_TABLE);
        builder.setTables(Constants.STORY_EVENT_TABLE);

        if ( id != null ) {
            builder.appendWhere(Constants.DB_ID + " = " + id);
        }

        Cursor cursor = builder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public long addEntry(ContentValues values) throws SQLException {
        long id = getWritableDatabase().insert(Constants.TABLE_ENTRY, "", values);

        if ( id <= 0 ) {
            throw new SQLException("Failed to add entry");
        }
        return id;
    }

    public int deleteEntry(String id) {
        if ( id == null ) {
            return getWritableDatabase().delete(Constants.TABLE_ENTRY, null, null);
        } else {
            return getWritableDatabase().delete(
                    Constants.TABLE_ENTRY,
                    Constants.DB_ID + "=?",
                    new String[] { id });
        }
    }

    public int updateEntry(String id, ContentValues values) {
        if ( id == null ) {
            return getWritableDatabase().update(Constants.TABLE_ENTRY, values, null, null);
        } else {
            return getWritableDatabase().update(
                    Constants.TABLE_ENTRY,
                    values,
                    Constants.DB_ID + "=?",
                    new String[] { id });
        }
    }
}
