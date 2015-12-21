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

    public void insertElements(ContentValues values, String table, int num) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.execSQL("INSERT INTO "
                + table + " VALUES "
                + values + " WHERE "
                + Constants.DB_ID + " = "
                + num);
    }

    // Insert ID into the characters, plotline, and places table
    public void insertTheID(int id) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.execSQL("INSERT INTO "
                + Constants.STORY_CHARACTER_TABLE + " ("
                + Constants.DB_ID + ") VALUES ("
                + id + ");");

        sbDatabase.execSQL("INSERT INTO "
                + Constants.STORY_PLOTLINE_TABLE + " ("
                + Constants.DB_ID + ") VALUES ("
                + id + ");");

        sbDatabase.execSQL("INSERT INTO "
                + Constants.STORY_PLACES_TABLE + " ("
                + Constants.DB_ID + ") VALUES ("
                + id + ");");
    }

    public boolean updateRow(ContentValues values, String db) {

        return true;
    }

    public void runQuery(String query) {
        sbDatabase = this.getReadableDatabase();
        sbDatabase.execSQL(query);
    }

    public int getStoryID(String title) {
        sbDatabase = this.getReadableDatabase();
        Cursor cursor = sbDatabase.rawQuery("SELECT "
                + Constants.DB_ID + " FROM "
                + Constants.STORY_TABLE + " WHERE "
                + Constants.STORY_NAME + " = '"
                + title + "'",
                null);

        // Move to first index, otherwise OutOfBoundsException will occur
        if ( !cursor.moveToFirst() ) {
            cursor.moveToFirst();
        }

        return cursor.getInt( cursor.getColumnIndexOrThrow(Constants.DB_ID) );
    }

    public Cursor getRows(String query) {
        sbDatabase = this.getReadableDatabase();
        Cursor result = sbDatabase.rawQuery(query, null);
        return result;
    }
}
