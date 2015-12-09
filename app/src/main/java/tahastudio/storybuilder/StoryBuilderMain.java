package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * This is the main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {

    // Add in MD toolbar, drawer, and FAB to the view
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawer_layout;
    FloatingActionButton the_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity's view before finding elements
        setContentView(R.layout.fragment_story_builder_main);

        // Call function to check if app has been run before.
        // Otherwise, call popup intro box
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
                addStoryPopUp(v);
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

            // Once run, update the preferences
            // Only run again when app is updated
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public void addStoryPopUp(View view) {

        // Set the popup window's activity, which will be this activity,
        // since we're just overlaying a window over the main activity
        PopupWindow popup = new PopupWindow(StoryBuilderMain.this);

        // Inflate the view we plan on using for the pop-up box
        final View layout = getLayoutInflater().inflate(R.layout.activity_add_story, null);

        // Let's set the view inside the pop-up box
        popup.setContentView(layout);

        // Set the width/height of the content in the pop-up box
        // This will take up all of the screen
        popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        // Can the user touch outside the pop-up box to exit it? -> true
        // Is it focused when it pops up? -> true
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        // Set the location of where it'll show.
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Get the button in the inflated layout. Must find by View first,
        // which is -> layout
        final Button add_the_story = (Button) layout.findViewById(R.id.add_the_story);

        // Create a new intent to launch the CreateStory activity
        add_the_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add an intent for CreateStory
                Intent callCreateStory = new Intent(StoryBuilderMain.this, CreateStory.class);
                // Call it
                startActivity(callCreateStory);
            }
        });

        // TODO -> Save the title and genre to database
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
