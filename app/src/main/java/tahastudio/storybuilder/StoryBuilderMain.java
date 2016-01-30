package tahastudio.storybuilder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import tahastudio.storybuilder.ui.SBDeleteDialog;
import tahastudio.storybuilder.ui.SBDialog;

/**
 * Main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    FloatingActionButton the_fab;
    private ListView list;
    private SimpleCursorAdapter cursorAdapter; // For the LoaderManager

    // From String[] for the cursor
    String[] from = {
            Constants.DB_ID,
            Constants.STORY_NAME,
            Constants.STORY_GENRE,
            Constants.STORY_DESC };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_builder_main);

        // TODO -> Set up a drawer

        TextView textView = (TextView) findViewById(R.id.quote); // TextView used for quotes
        // randomQuoteTask will generate a random quote and place it in the TextView
        new randomQuoteTask(textView).execute();

        list = (ListView) findViewById(R.id.story_list); // Stories go here
        TextView empty = (TextView) findViewById(R.id.empty); // If saved stories == null
        list.setEmptyView(empty); // Set the view to the empty TextView

        // To int[] for the SimpleCursorAdapter
        int[] to = {
                R.id.element_id,
                R.id.name_info,
                R.id.extra_info,
                R.id.desc
        };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.tab_view, null, from, to, 0);
        list.setAdapter(cursorAdapter);

        // To initialize the LoaderManager
        getSupportLoaderManager().initLoader(Constants.LOADER, null, this);

        // When a user clicks on a story, grab the title, and pass it to
        // ShowStory to return the story details from the db
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Clicking on a ListView row will return a cursor
                // Get the position of the user click to generate the cursor
                Cursor cursor = (Cursor) list.getItemAtPosition(position);

                // From the cursor, we can grab the story id and title, going by db column names
                int story_id = cursor.getInt(cursor.getColumnIndex(Constants.DB_ID));
                String title = cursor.getString(cursor.getColumnIndex(Constants.STORY_NAME));

                // Show a message while loading the story
                Toast.makeText(getApplicationContext(), "Loading story. Please wait...",
                        Toast.LENGTH_LONG).show();

                // Create a new Intent to pass info to ShowStory
                Intent intent = new Intent(getApplicationContext(), ShowStory.class);
                intent.putExtra("id", story_id);  // Pass the story id
                intent.putExtra("title", title); // Pass the story title

                // Add a flag
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Call the intent from this context
                getApplicationContext().startActivity(intent);
            }
        });

        // On long click, bring up the delete dialog box
        // Returns: SBDeleteDialog class
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) list.getItemAtPosition(position);

                deleteSBDialog(cursor.getInt(cursor.getColumnIndex(
                        Constants.DB_ID)), // Get the _id
                        Constants.STORY_TABLE, // Send over table...
                        Constants.DB_ID); //  ...and the column name
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
    private void deleteSBDialog(int position, String table, String column) {
        // Bundle the story id for the delete dialog
        Bundle bundle = new Bundle();
        bundle.putInt("id", position); // The id is the _id for the story entry in the db
        bundle.putString("table", table); // The db table name
        bundle.putString("column", column); // The column for the where clause

        SBDeleteDialog deleteDialog = new SBDeleteDialog();
        deleteDialog.setArguments(bundle); // Send the bundle over to the dialog

        deleteDialog.show(getSupportFragmentManager(), "delete_story");
    }

    // Must implement the below methods for the LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int num, Bundle state) {
        // This URI will be sent to a switch statement in the StoryProvider. It will
        // set the tables on setTables() method in the db to pull the data for the ListView
        Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_TABLE);

        // Send the URI and the string[] to StoryProvider to interface with the db
        // This will be returned to onLoadFinished
        return new android.support.v4.content.CursorLoader(this, uri, from, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);

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
}