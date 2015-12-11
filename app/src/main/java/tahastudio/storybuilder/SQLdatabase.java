package tahastudio.storybuilder;

import android.database.sqlite.SQLiteDatabase;

public class SQLDatabase {
    private SQLiteDatabase sbDatabase;

    // Version number must change if schema changes
    public static final double DATABASE_VERSION = 1.0;
    public static final String DATABASE_NAME = "sql.db";

    // Set up database schema
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_TYPE = ",";

    private static final String SQL_CREATE_ENTRIES = "(";

    public SQLDatabase() {

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

}
