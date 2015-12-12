package tahastudio.storybuilder;

import android.database.sqlite.SQLiteDatabase;

public class SQLDatabase {
    private SQLiteDatabase sbDatabase;

    public SQLDatabase() {

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.SQL_CREATE_DB);
    }

}
