package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

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


    // We need to be able to pass more than one string to this background task
    public CreateStoryTask(Context context, String title, String genre, String desc) {
        this.context = context;
        this.title = title;
        this.genre = genre;
        this.desc = desc;
    }

     @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        // Instantiate new db instance
        SQLDatabase db = new SQLDatabase(context);
        db.getWritableDatabase();

        // Instantiate a ContentValues instance to add in the data
        ContentValues values = new ContentValues();
        values.put(Constants.STORY_NAME, title);
        values.put(Constants.STORY_GENRE, genre);
        values.put(Constants.STORY_NOTES, desc);

        // Insert into the database
        db.insertRow(values, Constants.STORY_TABLE);

        // Now get the id of the story to send as an Intent to the CreateStory class
        db.getReadableDatabase();
        return db.getStoryID();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        // Now call the Intent creator, passing in the story id and title
        Intent intent = new Intent(context, CreateStory.class);
        intent.putExtra("title", title);
        intent.putExtra("id", result);

        // Need to add a flag or it will give an error
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Call the CreateStory class from this context
        context.startActivity(intent);
    }
}