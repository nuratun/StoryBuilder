package tahastudio.storybuilder.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.utils.Constants;
import tahastudio.storybuilder.utils.SQLDatabase;

/**
 * An AsyncTask to grab the data from a row and query the db. Will pass in
 * an int to determine what table to grab data from.
 * 0 -> Characters
 * 1 -> Places
 * 2 -> Plotlines
 * Location: AddCharacterElements, AddPlaceElements, AddPlotlineElements
 */
public class UpdateElementsTask extends AsyncTask<String, Void, Cursor> {
    private Context context;
    private int table;
    private String data;

    public UpdateElementsTask(Context context, int table, String data) {
        this.context = context;
        this.table = table;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, data, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Cursor doInBackground(String... params) {
        SQLDatabase db = new SQLDatabase(context);

        switch (table) {
            case 0:
                return db.getElementRow(Constants.GRAB_CHARACTER_ROW_DETAILS, data);
            case 1:
                return db.getElementRow(Constants.GRAB_PLACE_ROW_DETAILS, data);
            case 2:
                return db.getElementRow(Constants.GRAB_PLOTLINE_ROW_DETAILS, data);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);

        // TODO -> Create separate class to show values. On button click, edit values
    }
}
