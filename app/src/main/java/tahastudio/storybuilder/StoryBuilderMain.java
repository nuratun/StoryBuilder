package tahastudio.storybuilder;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This is the main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {
    // Add in MD toolbar, drawer, and FAB to the view
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawer_layout;
    FloatingActionButton the_fab;
    // Get an instance of the FragmentManager
    FragmentManager fragment = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity's view before finding elements
        setContentView(R.layout.activity_story_builder_main);

        // Call function to check if app has been run before.
        checkFirstRun();

        // Find and attach the toolbar to the view
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set title on actionbar
        setTitle("StoryBuilder");

        // Find the drawer
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // TODO -> Set the drawer

        // Now find the FAB call pop-up box when it's clicked
        the_fab = (FloatingActionButton) findViewById(R.id.fab);
        the_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction =
                        fragment.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.fragment_container,
                                        new StoryBuilderMainFragment());
                                transaction.commit();
            }
        });
    }

    //
    // Method for checking if app has been run for the first time.
    // If so, provide message. Otherwise, skip.
    //
    public void checkFirstRun() {

        // Set the variable to true/false
        boolean isFirstRun;

        // First run is initially set to true
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        // TODO -> Implement the pop-up box
        if (isFirstRun) {
            // Put dialog here

            // Once run, update the preference. Only run again when app is updated
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_builder_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // To return the Integer from the AsyncTask and use it
    public Integer callAddStoryTask(String zero, String one, String two) {
        // This is being called from StoryBuilderMainFragment,
        // to add a new story to the database.
        // Return an Integer from addStoryTask.
        try {
            return new addStoryTask(zero, one, two).execute().get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Need to start background thread to do the db work
    private class addStoryTask extends AsyncTask<Void, Void, Integer> {
        // Because we're passing multiple variables to the AsyncTask, we should wrap them
        // in a constructor. Then AsyncTask can parse them out. The public class
        // callAddStoryTask will be called from StoryBuilderMainFragment, and that
        // will instantiate this class.
        // Return: Integer, which will be db id for this entry
        String zero;
        String one;
        String two;

        addStoryTask(String zero, String one, String two) {
            this.zero = zero;
            this.one = one;
            this.two = two;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();

        }

        @Override
        protected Integer doInBackground(Void... params) {
            // Store in a ContentValues instance to prepare for
            // db entry
            ContentValues values = new ContentValues();

            // Grab the Strings from addStoryParams instance
            String story_name = zero;
            String story_genre = one;
            String story_notes = two;

            // Sent 3 strings to this AsyncTask, which puts
            // them into an array. Grab them by their index.
            values.put(Constants.STORY_NAME, story_name);
            values.put(Constants.STORY_GENRE, story_genre);
            values.put(Constants.STORY_NOTES, story_notes);

            // Instantiate an instance of the SQLDatabase class
            SQLDatabase db = new SQLDatabase(StoryBuilderMain.this);

            // Call the insertRow method of SQLDatabase to insert the values
            db.insertRow(values, Constants.STORY_TABLE);

            // Now grab the primary key of this entry to return
            // to the UI thread during postExecute
            Integer story_id = db.getStoryID(story_name);

            return story_id;
        }

        @Override
        protected void onPostExecute(Integer story_id) {
            super.onPostExecute(story_id);
        }
    }
}