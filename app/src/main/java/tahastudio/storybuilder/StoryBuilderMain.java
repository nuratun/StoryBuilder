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

public class StoryBuilderMain extends AppCompatActivity {

    // Add in MD toolbar and drawer to main activity XML
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawer_layout;

    // Add the FAB
    FloatingActionButton the_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set contentView first before finding elements
        setContentView(R.layout.fragment_story_builder_main);

        // Call function to check if app has run before
        checkFirstRun();

        // Attach toolbar
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Attach drawer
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setTitle("StoryBuilder");

        // Now call pop-up box when FAB is clicked
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

        boolean isFirstRun;
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // Put dialog here


            // Update the preferences so this dialog isn't run again until updated
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public void addStoryPopUp(View view) {

        // Set the popup window's activity, which will be this since
        // we're just overlaying a window over the main activity
        PopupWindow popup = new PopupWindow(StoryBuilderMain.this);

        // Inflate the view we plan on using for the popup
        View layout = getLayoutInflater().inflate(R.layout.activity_add_story, null);

        // Let's set the view inside the popup
        popup.setContentView(layout);

        // Set the width/height of the content
        popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Get the button in the inflated layout. Must find by View -> layout
        Button add_the_story = (Button) layout.findViewById(R.id.add_the_story);

        add_the_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createStory = new Intent(StoryBuilderMain.this, CreateStory.class);
                startActivity(createStory);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_story_builder_main, menu);
        //return super.onCreateOptionsMenu(menu);

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
