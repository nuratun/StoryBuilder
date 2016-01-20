package tahastudio.storybuilder.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import tahastudio.storybuilder.ShowStory;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * AsyncTask to grab story details from db and send to ShowStory
 */
public class ShowStoryTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    String title;

    // Pass the context and string from StoryBuilderMain
    public ShowStoryTask(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show a message while loading the story in the background
        Toast.makeText(context, "Loading story. Please wait...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Integer doInBackground(String... params) {
        SQLDatabase db = SQLDatabase.getInstance(context);

        return db.findStoryID(title); // Return the Integer from the db, querying by title
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        // Create a new Intent to pass info to ShowStory
        Intent intent = new Intent(context, ShowStory.class);
        intent.putExtra("title", title); // Pass the string that was passed to from main activity
        intent.putExtra("id", result);  // Pass the Integer that was passed from background thread

        // Add a flag or get an exception raised
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Call the intent from this context
        context.startActivity(intent);
    }
}
