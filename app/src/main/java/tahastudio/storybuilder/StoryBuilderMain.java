package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class StoryBuilderMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call function to check if app has run before
        checkFirstRun();

        setContentView(R.layout.fragment_story_builder_main);

        // Attach toolbar
        Toolbar tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(tool_bar);
        setTitle("StoryBuilder");
    }

    /* Method for checking if app is being run for the first time,
    /* or if it's just been updated.
     */
    public void checkFirstRun() {

        boolean isFirstRun;
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // Put dialog here


            // Update the preferences so this dialog isn't run again
            // until updated
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
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
