package tahastudio.storybuilder.tasks;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.ShowStory;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * Separate AsyncTask to add a new story to the database. Will pass three strings
 * from SBDialog.
 * Return: the db id for this story
 */
public class CreateStoryTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    String title;
    String genre;
    String desc;


    // We need to be able to pass more than one string to background
    public CreateStoryTask(Context context, String title, String genre, String desc) {
        this.context = context;
        this.title = title;
        this.genre = genre;
        this.desc = desc;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Show message while creating the story in the background
        Toast.makeText(context, "Creating story. Please wait...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Integer doInBackground(String... params) {
        SQLDatabase db = SQLDatabase.getInstance(context);

        // Instantiate a ContentValues instance to add in data
        ContentValues values = new ContentValues();
        values.put(Constants.STORY_NAME, title);
        values.put(Constants.STORY_GENRE, genre);
        values.put(Constants.STORY_DESC, desc);

        // Insert into the database
        db.insertRow(values, Constants.STORY_TABLE);

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
        // The activity is for the onActivityResult method in StoryBuilderMain
        // Need to pass in an integer, though if this is successful, the
        // automatic assumption is that a story has been created.
        ((Activity) context).startActivityForResult(intent, 1);
    }
}