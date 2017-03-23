package tahastudio.storybuilder.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.db.SQLDatabase;

/**
 * Archive a story, or a story element (character, location, event)
 * Called from StoryBuilderMain, AddCharacters, AddEvents, AddLocations, AddPlots
 */
public class ArchiveElementTask extends AsyncTask<String, Void, Cursor> {
    private Context context;
    private boolean story;
    private String table;
    private int id;

    public ArchiveElementTask(Context context, boolean story, String table, int id) {
        this.context = context;
        this.story = story;
        this.table = table;
        this.id = id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(context, "Archiving...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Cursor doInBackground(String... params) {
        SQLDatabase db = new SQLDatabase(context);

        return db.archiveElement(story, table, id);
    }

    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);

    }

}
