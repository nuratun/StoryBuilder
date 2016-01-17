package tahastudio.storybuilder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import tahastudio.storybuilder.ShowStory;

/**
 * This class holds all the SQL queries used elsewhere in the app
 */
public class SQLDatabase extends SQLiteOpenHelper {
    private SQLiteDatabase sbDatabase;

    public SQLDatabase(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
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

    public void insertRow(ContentValues values, String db) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.insert(db, null, values);
    }

    public void updateRow(ContentValues values, String db, int id) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.update(db, values, "_id = " + id, null);
    }

    // Get the story ID back from database to create the other tables
    // Location: CreateStoryTask
    public Integer getStoryID() {
        sbDatabase = this.getReadableDatabase();
        Cursor cursor = sbDatabase.rawQuery(Constants.GET_STORY_ID, null);

        cursor.moveToFirst(); // Move to the first position
        int the_id = cursor.getInt(0);
        cursor.close(); // Close the cursor when done

        return the_id;
    }

    // Get a story ID from database depending on what row user clicked
    // on in StoryBuilderMain
    // Location: showStoryTask
    public Integer findStoryID(String result) {
        sbDatabase = this.getReadableDatabase();
        Cursor cursor =  sbDatabase.rawQuery(Constants.FIND_STORY_ID, new String[] { result });

        cursor.moveToFirst(); // Move to the first position
        int the_id = cursor.getInt(0);
        cursor.close(); // Close the cursor when done

        return the_id;
    }

    // Need to dynamically add in the SB_ID to this query, as it will
    // change depending on user story selection
    // Location: AddCharacters, AddLocations, AddEvents
    public Cursor getRows(String query) {
        sbDatabase = this.getReadableDatabase();
        return sbDatabase.rawQuery(query + ShowStory.SB_ID + ";", null);
    }

    // Get the story element user clicked on
    // Location: AddCharacters, AddLocations, AddEvents
    public Cursor getElementRow(String query, String name) {
        sbDatabase = this.getReadableDatabase();
        return sbDatabase.rawQuery(query + "'" + name + "';", null);
    }

    // Get the story genre to set the drawable
    // Location: ShowStory
    public Cursor getStoryGenre() {
        sbDatabase = this.getReadableDatabase();
        return sbDatabase.rawQuery(Constants.GET_STORY_GENRE + ShowStory.SB_ID + ";", null);
    }

    // For user search queries
    public Cursor getSearchResults(String find, String[] columns) {
        sbDatabase = this.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        columns = new String[] {
                Constants.STORY_CHARACTER_NAME,
                Constants.STORY_CHARACTER_BIRTHPLACE,
                Constants.STORY_CHARACTER_HISTORY,
                Constants.STORY_CHARACTER_CONFLICTS,
                Constants.STORY_CHARACTER_PERSONALITY,
                Constants.STORY_CHARACTER_STORYLINE,
                Constants.STORY_EVENT_LINER,
                Constants.STORY_EVENT_DESC,
                Constants.STORY_LOCATION_NAME,
                Constants.STORY_LOCATION_EVENTS,
                Constants.STORY_LOCATION_DESC };

        // Tables to search
        builder.setTables(Constants.STORY_TABLE);
        builder.setTables(Constants.STORY_CHARACTER_TABLE);
        builder.setTables(Constants.STORY_LOCATION_TABLE);
        builder.setTables(Constants.STORY_EVENT_TABLE);

        Cursor cursor = builder.query(sbDatabase,
                columns,
                "find="+find,
                new String[] { find },
                Constants.DB_ID,
                null,
                Constants.DB_ID);

        if ( cursor == null ) {
            return null;
        }
        else if ( !cursor.moveToFirst() ) {
            cursor.close();
            return null;
        }

        return cursor;
    }
}
