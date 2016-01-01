package tahastudio.storybuilder.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * An AsyncTask to grab the data from a row and query the db. Will pass in
 * an int to determine what table to grab data from.
 * 0 -> Characters
 * 1 -> Places
 * 2 -> Plots
 * Location: AddCharacterElements, AddPlaceElements, AddPlotElements
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
                return db.getElementRow(Constants.GRAB_PLOT_ROW_DETAILS, data);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);

        // Override this method in the following classes:
        // ShowCharacters, ShowPlaces, ShowPlots

        // Do nothing here
    }
}
