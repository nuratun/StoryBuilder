package tahastudio.storybuilder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * AsyncTask to grab story details from db and send to CreateStory
 */
public class ShowStoryTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    String title;

    // Need to be able to pass the context and string from StoryBuilderMain
    public ShowStoryTask(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show a message to the user while loading the story in the background
        Toast.makeText(context, "Loading story. Please wait...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Integer doInBackground(String... params) {
        SQLDatabase db = new SQLDatabase(context);
        db.getReadableDatabase(); // Get a read on the db to grab the id

        return db.findStoryID(title); // Return the Integer from the db
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        // Create a new Intent to pass info to CreateStory
        Intent intent = new Intent(context, CreateStory.class);
        intent.putExtra("title", title); // Pass the string that was passed to this task
        intent.putExtra("id", result);  // Pass the Integer that was passed from db

        // Need to add a flag or it will result an exception
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Call the intent from this context
        context.startActivity(intent);
    }
}
