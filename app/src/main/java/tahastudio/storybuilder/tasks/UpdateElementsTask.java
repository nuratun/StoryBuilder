package tahastudio.storybuilder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.db.SQLDatabase;

/**
 * AsyncTask to update saved db row
 * Parameters: ContentValues, the table name
 * Returns: boolean
 */
public class UpdateElementsTask extends AsyncTask<Void, Void, Boolean> {
    Context context;
    ContentValues values;
    String table;
    String column;
    int id;

    public UpdateElementsTask(Context context,
                              ContentValues values,
                              String table,
                              String column,
                              int id) {
        this.context = context;
        this.values = values;
        this.table = table;
        this.id = id;
        this.column = column;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, "Updating...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            SQLDatabase db = SQLDatabase.getInstance(context);
            db.updateRow(values, table, column, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
