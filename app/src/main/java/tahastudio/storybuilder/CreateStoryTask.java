package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Separate AsyncTask to add a new story to the database.
 */
public class CreateStoryTask extends AsyncTask<String, Void, Integer> {
    String one;
    String two;
    String three;
    private Context context;

    public CreateStoryTask(Context context, String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.context = context;
    }

    // Interface for this class
    public interface StoryTask{
        void onPostResult(Integer result);
    }

    StoryTask addStory;

    // Constructor for this class
    public void CreateTheStory(StoryTask addStory) {
        this.addStory = addStory;
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
        values.put(Constants.STORY_NAME, one);
        values.put(Constants.STORY_GENRE, two);
        values.put(Constants.STORY_NOTES, three);

        // Insert into the database
        db.insertRow(values, Constants.STORY_TABLE);

        // Now get the db id back
        db.getReadableDatabase();

        try {
            return db.getStoryID();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        // TODO -> Causing an error. Send the story title back, and grab the id in the
        // next class
        addStory.onPostResult(result);
    }


}
