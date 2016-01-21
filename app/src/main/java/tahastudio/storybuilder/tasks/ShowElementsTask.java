package tahastudio.storybuilder.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * An AsyncTask to grab the data from a row and query the db. Will pass in
 * an int to determine what table to grab data from, and populate fields.
 * Override onPostExecute in separate class files
 * Location: AddCharacterElements, AddLocationElements, AddEventElements
 */
public class ShowElementsTask extends AsyncTask<String, Void, Cursor> {
    private Context context;
    private String table;
    private String data;
    private int id;

    // Takes the table name, the title of the element, and the element id and finds them in the db
    // Called from: ShowCharacter, ShowLocation, ShowEvent
    public ShowElementsTask(Context context, String table, String data, int id) {
        this.context = context;
        this.table = table;
        this.data = data;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, data, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Cursor doInBackground(String... params) {
        SQLDatabase db = SQLDatabase.getInstance(context);

        switch (table) {
            case Constants.STORY_CHARACTER_TABLE:
                return db.getElementRow(Constants.GRAB_CHARACTER_ROW_DETAILS, data, id);
            case Constants.STORY_LOCATION_TABLE:
                return db.getElementRow(Constants.GRAB_LOCATION_ROW_DETAILS, data, id);
            case Constants.STORY_EVENT_TABLE:
                return db.getElementRow(Constants.GRAB_EVENT_ROW_DETAILS, data, id);
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);

        // Override this method in the following classes:
        // ShowCharacters, ShowLocations, ShowEvents

        // Do nothing here
    }
}
