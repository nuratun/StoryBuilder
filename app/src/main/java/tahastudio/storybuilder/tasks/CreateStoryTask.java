package tahastudio.storybuilder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.activities.ShowStory;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * Separate AsyncTask to add a new story to the database. Will pass three strings
 * from SBDialog.
 * Return: the db id for this story
 */
public class CreateStoryTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    private ContentValues values;
    private String title;

    public CreateStoryTask(Context context, ContentValues values, String title) {
        this.context = context;
        this.values = values;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Show message while creating the story in the background
        Toast.makeText(context, "Creating story. Please wait...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Integer doInBackground(String... params) {
        SQLDatabase db = new SQLDatabase(context);

        Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_TABLE);
        context.getContentResolver().insert(uri, values); // Insert method from StoryProvider

        // Return the id of the story to send as an Intent to the ShowStory class
        return db.getStoryID();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        // Call the Intent creator, passing in the story id and title
        Intent intent = new Intent(context, ShowStory.class);
        intent.putExtra("title", title);
        intent.putExtra("id", result);

        // Add a flag or get an exception raised
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Create the story, calling the ShowStory class from this context
        context.startActivity(intent);
    }
}