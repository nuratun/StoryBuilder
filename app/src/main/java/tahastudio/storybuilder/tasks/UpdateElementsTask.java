package tahastudio.storybuilder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * AsyncTask to update saved db row
 */
public class UpdateElementsTask extends AsyncTask<Void, Void, Boolean> {
    Context context;
    ContentValues values;
    String table;

    public UpdateElementsTask(Context context, ContentValues values, String table) {
        this.context = context;
        this.values = values;
        this.table = table;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
    }
}
