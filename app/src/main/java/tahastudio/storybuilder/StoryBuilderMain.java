package tahastudio.storybuilder;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;
import tahastudio.storybuilder.tasks.ShowStoryTask;
import tahastudio.storybuilder.ui.SBDialog;

/**
 * Main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {
    FloatingActionButton the_fab;
    ListView story_list;
    TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_builder_main);

        // TODO -> Set up a drawer

        TextView textView = (TextView) findViewById(R.id.quote); // TextView used for quotes
        // randomQuoteTask will generate a random quote and place it in the above TextView
        randomQuoteTask randomQuoteTask = new randomQuoteTask(textView);
        randomQuoteTask.execute();

        story_list = (ListView) findViewById(R.id.story_list); // Stories go here
        empty = (TextView) findViewById(R.id.empty); // TextView used if stories == null

        // AsyncTask to find list of stories in db. If not null, return the list
        // Otherwise, set up the empty TextView. Call the public method.
        callStoryListTask(story_list, empty);

        // When a user clicks on a story, grab the title, and pass it to
        // ShowStory to return the story details from the db
        story_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicking on a ListView row will return a cursor
                // Get the position of the user click
                Cursor cursor = (Cursor) story_list.getItemAtPosition(position);

                // From the cursor, we can grab the title, going by column name
                String title = cursor.getString(
                        cursor.getColumnIndex(Constants.STORY_NAME));

                // ShowStoryTask class will grab the the story _id from the db
                // and create an intent to call the ShowStory class
                ShowStoryTask showStoryTask = new ShowStoryTask(getBaseContext(), title);
                showStoryTask.execute();
            }
        });

        // When FAB is clicked, a dialog box appears
        // Returns: SBDialog class
        the_fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        the_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSBDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_story_builder_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.about) {
            startActivity(new Intent(this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To update the ListView whenever a new story is created
    // The integers are not used, as the dialog box will exit
    // if the story has not been created
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        callStoryListTask(story_list, empty);

    }

    // Calls SBDialog class to create the story creation dialog
    // User inputs data and creates a new story. Data is sent to the CreateStoryTask class
    // Initialized from the FAB
    private void showSBDialog() {
        SBDialog sbDialog = new SBDialog();
        sbDialog.show(getSupportFragmentManager(), "story_creation");
    }

    // Makes the storyListTask public, so fragments can access it
    public void callStoryListTask(ListView list, TextView empty) {
        storyListTask storyListTask = new storyListTask(list, empty);
        storyListTask.execute();
    }

    // AsyncTask to generate random quote on start of activity
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
            // Grab the array from the quotes resources file
            quote = getResources().getStringArray(R.array.quotes);
        }

        @Override
        protected String doInBackground(Void... params) {
            return quote[generator.nextInt(quote.length)];
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result); // Set the quote on the TextView
        }
    }

    // AsyncTask will return a list of stories saved in the db
    // Clicking on one will return the saved data for that story
    // by calling the ShowStory task
    // Returns: A list of stories in db. On null, sets up an empty TextView
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
                        Constants.STORY_GENRE,
                        Constants.STORY_DESC
                };

                // Get the TextView widgets
                int[] widgets = {
                        R.id.name_info,
                        R.id.extra_info,
                        R.id.desc
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

                // Notify thread the data has changed
                cursorAdapter.notifyDataSetChanged();
                cursorAdapter.notifyDataSetInvalidated();
            }
        }
    }
}