package tahastudio.storybuilder.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.adapters.StoryAdapter;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.ui.SBDeleteDialog;
import tahastudio.storybuilder.ui.SBDialog;

/**
 * Main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton fab;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private StoryAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

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
        recyclerView = (RecyclerView) findViewById(R.id.story_list);

        TextView textView = (TextView) findViewById(R.id.quote); // TextView used for quotes
        // randomQuoteTask will generate a random quote and place it in the TextView
        new randomQuoteTask(textView).execute();

        // Set the layout manager for the RecyclerView
        recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        // Set the adapter (Cursor) for the RecyclerView
        recyclerAdapter = new StoryAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);

        // To initialize the LoaderManager
        getSupportLoaderManager().restartLoader(Constants.LOADER, null, this);

        // TODO -> Set up a drawer

        // When an item on the RecyclerView is clicked, load the story
        recyclerAdapter.setOnItemClickListener(new StoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor) {
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

        // When long clicked, bring up the delete dialog box
        recyclerAdapter.setOnItemLongClickListener(new StoryAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(Cursor cursor) {
                SBDeleteDialog deleteDialog = new SBDeleteDialog();
                deleteDialog.delete(cursor.getInt(cursor.getColumnIndex(
                        Constants.DB_ID)),
                        Constants.STORY_TABLE,
                        Constants.DB_ID);
                deleteDialog.show(getSupportFragmentManager(), "delete");
            }
        });

        // When FAB is clicked, a dialog box appears
        // Returns: SBDialog class
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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

    // Must implement the below methods for the LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int num, Bundle state) {
        // This URI will be sent to a switch statement in the StoryProvider. It will
        // set the tables on setTables() method in the db to pull the data for the ListView
        Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_TABLE);

        // Call the query method on the ContentProvider
        cursor = getContentResolver().query(uri, from, null, null, null);

        // Send the URI and the string[] to StoryProvider to interface with the db
        // This will be returned to onLoadFinished
        return new android.support.v4.content.CursorLoader(this, uri, from, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        recyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerAdapter.swapCursor(null);
    }

    // Calls SBDialog class to create the story creation dialog
    // User inputs data and creates a new story. Data is sent to the CreateStoryTask class
    // Initialized from the FAB
    private void showSBDialog() {
        SBDialog sbDialog = new SBDialog();
        sbDialog.show(getSupportFragmentManager(), "story_creation");
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