package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Separate AsyncTask to add a new story to the database. Will pass three strings
 * from SBDialog.
 */
public class CreateStoryTask extends AsyncTask<String, Void, String> {
    String one;
    String two;
    String three;
    private Context context;

    // We need to be able to pass more than one string to this background task
    public CreateStoryTask(Context context, String one, String two, String three) {
        this.context = context;
        this.one = one;
        this.two = two;
        this.three = three;
    }

     @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        // Instantiate new db instance
        SQLDatabase db = new SQLDatabase(context);
        db.getWritableDatabase();

        // Instantiate a ContentValues instance to add in the data
        ContentValues values = new ContentValues();
        values.put(Constants.STORY_NAME, one);
        values.put(Constants.STORY_GENRE, two);
        values.put(Constants.STORY_NOTES, three);

        // Insert into the database
        db.insertRow(values, Constants.STORY_TABLE);

        // Now return the story title to send to
        // StoryBuilderMain and then CreateStory
        return one;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }


}
