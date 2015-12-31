package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

/**
 * This is the main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {
    // Add in toolbar, drawer, and FAB to the view
    android.support.v7.widget.Toolbar toolbar;
    //android.support.v4.widget.DrawerLayout drawer_layout;
    FloatingActionButton the_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_builder_main);

        // TODO -> Call function to check if app has been run before.
        //checkFirstRun();

        toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar); // Find the toolbar in the view
        setSupportActionBar(toolbar); // Set toolbar as the actionbar
        setTitle("StoryBuilder"); // Set title on actionbar

        //drawer_layout = (android.support.v4.widget.DrawerLayout)
        //        findViewById(R.id.drawer_layout); // Find the drawer
        // TODO -> Set the drawer

        // Find the ListView and TextView to send to storyTaskList
        // The empty TextView will be used if there are no stories to return
        final ListView story_list = (ListView) findViewById(R.id.story_list);
        TextView empty = (TextView) findViewById(R.id.empty);
        storyListTask storyListTask = new storyListTask(story_list, empty);
        storyListTask.execute();

        // Find the TextView in the layout and then run the
        // randomQuoteTask to generate a random quote in it
        TextView textView = (TextView) findViewById(R.id.quote);
        randomQuoteTask randomQuoteTask = new randomQuoteTask(textView);
        randomQuoteTask.execute();

        // When a user clicks on an item, grab the db_id, and pass it to
        // CreateStory to return the story details from the db
        story_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicking on a ListView row will return a cursor
                // Get the position of the user click
                Cursor get_item = (Cursor) story_list.getItemAtPosition(position);

                // From the cursor, we can grab the title, so long as
                // we know the db column name
                String title = get_item.getString(
                        get_item.getColumnIndex(Constants.STORY_NAME));

                //Toast.makeText(getBaseContext(), title, Toast.LENGTH_LONG).show();

                // Call the showStoryTask to grab the id from the db and the
                // AsyncTask will then create an intent to call CreateStory
                ShowStoryTask showStoryTask = new ShowStoryTask(getBaseContext(), title);
                showStoryTask.execute();
            }
        });

        // Now find the FAB and call the dialog box when clicked
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

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Call the SBDialog class to create the dialog box. User will input data to the
    // dialog box to create a new story from the FAB. It will send the data to CreateStoryTask
    private void showSBDialog() {
        SBDialog sbDialog = new SBDialog();
        sbDialog.show(getSupportFragmentManager(), "story_creation");
    }

    // AsyncTask used to generate random quote on start of activity
    private class randomQuoteTask extends AsyncTask<Void, Void, String> {
        private Random generator = new Random();
        private TextView textView;
        private String[] quote;

        // Pass in the layout and TextView
        public randomQuoteTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Grab the array from the quotes.xml file
            quote = getResources().getStringArray(R.array.quotes);
        }

        @Override
        protected String doInBackground(Void... params) {
            return quote[generator.nextInt(quote.length)];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);
        }
    }

    // This AsyncTask will return a list of stories saved in the db
    // A user can click on one to return the saved data for that story
    private class storyListTask extends AsyncTask<Void, Void, Cursor> {
        private Context context = getBaseContext();
        private SQLDatabase db;
        private ListView listView;
        private TextView textView;

        public storyListTask(ListView listView, TextView textView) {
            this.listView = listView;
            this.textView = textView;

            listView.setEmptyView(textView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            db = new SQLDatabase(context);

            // Try to get the list of stories in the db
            try {
                return db.getRows(Constants.GRAB_STORY_DETAILS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            if (result != null) {
                // Get the column names
                String[] columns = {
                        Constants.STORY_NAME,
                        Constants.STORY_GENRE
                };

                // Get the TextView widgets
                int[] widgets = {
                        R.id.name_info,
                        R.id.extra_info
                };

                SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                        context,
                        R.layout.tab_view,
                        result,
                        columns,
                        widgets,
                        0);

                // Set the adapter on the ListView
                listView.setAdapter(cursorAdapter);
            }
        }
    }
}