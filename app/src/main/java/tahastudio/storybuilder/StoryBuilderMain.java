package tahastudio.storybuilder;

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
import android.widget.Toast;

import java.util.Random;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;
import tahastudio.storybuilder.ui.SBDeleteDialog;
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
        // randomQuoteTask will generate a random quote and place it in the TextView
        new randomQuoteTask(textView).execute();

        story_list = (ListView) findViewById(R.id.story_list); // Stories go here
        empty = (TextView) findViewById(R.id.empty); // TextView used if saved stories == null

        // AsyncTask to find list of stories in db. If not null, return the list
        // Otherwise, set up the empty TextView. Call the public method.
        callStoryListTask(story_list, empty);

        // When a user clicks on a story, grab the title, and pass it to
        // ShowStory to return the story details from the db
        story_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicking on a ListView row will return a cursor
                // Get the position of the user click to generate the cursor
                Cursor cursor = (Cursor) story_list.getItemAtPosition(position);

                // From the cursor, we can grab the story id and title, going by column names
                int story_id = cursor.getInt(cursor.getColumnIndex(Constants.DB_ID));
                String title = cursor.getString(cursor.getColumnIndex(Constants.STORY_NAME));

                // Show a message while loading the story
                Toast.makeText(getApplicationContext(), "Loading story. Please wait...",
                        Toast.LENGTH_LONG).show();

                // Create a new Intent to pass info to ShowStory
                Intent intent = new Intent(getApplicationContext(), ShowStory.class);
                intent.putExtra("id", story_id);  // Pass the story id
                intent.putExtra("title", title); // Pass the story title

                // Add a flag or get an exception raised
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Call the intent from this context
                getApplicationContext().startActivity(intent);
            }
        });

        // On long click, bring up the delete dialog box
        story_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) story_list.getItemAtPosition(position);

                deleteSBDialog(cursor.getInt(cursor.getColumnIndex(Constants.DB_ID)), // Get the _id
                        Constants.STORY_TABLE); // Send over the table to delete the story from
                return true;
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
        // Inflate the menu for the action bar
        getMenuInflater().inflate(R.menu.menu_story_builder_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Calls SBDialog class to create the story creation dialog
    // User inputs data and creates a new story. Data is sent to the CreateStoryTask class
    // Initialized from the FAB
    private void showSBDialog() {
        SBDialog sbDialog = new SBDialog();
        sbDialog.show(getSupportFragmentManager(), "story_creation");
    }

    // Calls the SBDeleteDialog class to delete a story, or story element
    private void deleteSBDialog(int position, String table) {
        // Bundle the story id for the delete dialog
        Bundle bundle = new Bundle();
        bundle.putString("table", table);
        bundle.putInt("id", position); // The id is the _id for the story entry in the db

        SBDeleteDialog deleteDialog = new SBDeleteDialog();
        deleteDialog.setArguments(bundle); // Send the bundle over to the dialog

        deleteDialog.show(getSupportFragmentManager(), "delete_story");
    }

    // To update the ListView whenever a new story is created
    // The integers are not used, as the dialog box will exit
    // if the story has not been created
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        callStoryListTask(story_list, empty);
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
        private SQLDatabase db = SQLDatabase.getInstance(context);
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
                        Constants.DB_ID,
                        Constants.STORY_NAME,
                        Constants.STORY_GENRE,
                        Constants.STORY_DESC
                };

                // Get the TextView widgets
                int[] widgets = {
                        R.id.element_id,
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