package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This is the main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {
    // Add in toolbar, drawer, and FAB to the view
    android.support.v7.widget.Toolbar toolbar;
    android.support.v4.widget.DrawerLayout drawer_layout;
    FloatingActionButton the_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO -> Set the top view to be a random quote about writing
        // Set the activity's view to start finding elements
        setContentView(R.layout.activity_story_builder_main);

        // TODO -> Call function to check if app has been run before.
        //checkFirstRun();

        toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar); // Find the toolbar in the view
        setSupportActionBar(toolbar); // Set toolbar as the actionbar
        setTitle("StoryBuilder"); // Set title on actionbar

        drawer_layout = (android.support.v4.widget.DrawerLayout)
                findViewById(R.id.drawer_layout); // Find the drawer
        // TODO -> Set the drawer

        // Now find the FAB and call the dialog box when it's clicked
        the_fab = (FloatingActionButton) findViewById(R.id.fab);
        the_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSBDialog();
            }
        });
    }

    // Method for checking if app has been run for the first time.
    // If so, provide message. Otherwise, skip.
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

    // Call the SBDialog class to create the dialog box. User will input data to the
    // dialog box to create a new story from the FAB
    public void showSBDialog() {
        SBDialog newDialog = new SBDialog();
        newDialog.show(getSupportFragmentManager(), "story_creation");
    }

    // The following is being called from SBDialog. It's calling AsyncTask -> CreateStoryTask
    public void onPostResult(String result) {
        // Add an intent for CreateStory
        Intent callCreateStory = new Intent(StoryBuilderMain.this, CreateStory.class);

        // TODO -> Grab the story id in db from another asynctask in CreateStory class
        // Send the story title to the new activity
        callCreateStory.putExtra("title", result);

        // Call the new activity
        startActivity(callCreateStory);
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

}