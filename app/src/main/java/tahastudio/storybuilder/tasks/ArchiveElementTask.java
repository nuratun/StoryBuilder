package tahastudio.storybuilder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Archive a story, or a story element (character, location, event)
 * Called from StoryBuilderMain, AddCharacters, AddEvents, AddLocations, AddPlots
 */
public class ArchiveElementTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    private ContentValues values;
    private String table;
    private int id;

    public ArchiveElementTask(Context context, ContentValues values, String table, int id) {
        this.context = context;
        this.values = values;
        this.table = table;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, "Archiving...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Integer doInBackground(String... params) {

    }

    @Override
    protected void onPostExecute(Integer result) {

    }

}
