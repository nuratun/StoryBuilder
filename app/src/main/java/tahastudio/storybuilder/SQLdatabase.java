package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
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

    }

    public void insertRow(ContentValues values, String db) {
        sbDatabase = this.getWritableDatabase();
        sbDatabase.insert(db, null, values);
        //sbDatabase.setTransactionSuccessful();
        sbDatabase.close();
    }

    public void runQuery(String query) {
        sbDatabase = this.getReadableDatabase();
        sbDatabase.execSQL(query);
        sbDatabase.close();
    }
}
