package tahastudio.storybuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class StoryBuilderMain extends AppCompatActivity {

    // Add in MD toolbar and drawer to main activity XML
    android.support.v7.widget.Toolbar toolbar;
    DrawerLayout drawer_layout;

    // Add the FAB
    FloatingActionButton the_fab;

    // Get the editText box to add a story
    //LinearLayout add_story;

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

    private void addStoryPopUp(View view) {
        // Get ready to inflate the activity_add_story XML
        LayoutInflater get_story_layout = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popSwitchView = get_story_layout.inflate(R.layout.activity_add_story, null, false);

        final PopupWindow popWindow = new PopupWindow(popSwitchView);
        popWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setOutsideTouchable(false);
        popWindow.setFocusable(true);

        Button add_story = (Button) popSwitchView.findViewById(R.id.add_the_story);

        add_story.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });

        popWindow.showAtLocation(popSwitchView, Gravity.CENTER, 0, 0);

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
