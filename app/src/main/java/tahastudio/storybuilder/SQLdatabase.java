package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDatabase extends SQLiteOpenHelper {
    private SQLiteDatabase sbDatabase;

    public SQLDatabase(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sbDatabase) {
        sbDatabase.execSQL(Constants.SQL_CREATE_STORY_TABLE);
        sbDatabase.execSQL(Constants.SQL_CREATE_CHARACTERS);
        sbDatabase.execSQL(Constants.SQL_CREATE_PLACES);
        sbDatabase.execSQL(Constants.SQL_CREATE_PLOTLINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sbDatabase, int old_ver, int new_ver) {
        // Remove when going into production
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_CHARACTER_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_PLACES_TABLE);
        sbDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.STORY_PLOTLINE_TABLE);
        onCreate(sbDatabase);
    }

    public void insertRow(ContentValues values, String db) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.insert(db, null, values);
    }

    public boolean updateRow(ContentValues values, String db) {

        return true;
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
        Cursor cursor =  sbDatabase.rawQuery(Constants.FIND_STORY_ID + "'"
                + result + "';" , null);

        cursor.moveToFirst(); // Move to the first position
        int the_id = cursor.getInt(0);
        cursor.close(); // Close the cursor when done

        return the_id;
    }

    // Need to dynamically add in the SB_ID to this query, as it will
    // change depending on user selection
    // Location: AddCharacters, AddPlaces, AddPlotline
    public Cursor getRows(String query) {
        sbDatabase = this.getReadableDatabase();
        return sbDatabase.rawQuery(query + ShowStory.SB_ID + ";", null);
    }

    // Get the story element user clicked on
    // Location: AddCharacters, AddPlaces, AddPlotline
    public Cursor getElementRow(String query, String name) {
        sbDatabase = this.getReadableDatabase();
        return sbDatabase.rawQuery(query + "'" + name + "';", null);
    }
}
