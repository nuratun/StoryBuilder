package tahastudio.storybuilder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.StoryAdapter;
import tahastudio.storybuilder.ui.SBDeleteDialog;
import tahastudio.storybuilder.ui.SBDialog;

/**
 * Main activity of StoryBuilder
 **/
public class StoryBuilderMain extends AppCompatActivity {

    FloatingActionButton the_fab;
    private Cursor cursor;
    private RecyclerView recyclerView;
    private StoryAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;
    private Uri uri;

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

        // Get the database table to grab data from
        uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_TABLE);

        // Call the query method on the ContentProvider
        cursor = getContentResolver().query(uri, from, null, null, null);

        // Set the layout manager for the RecyclerView
        recyclerLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayout);

        // Set the adapter (Cursor) for the RecyclerView
        recyclerAdapter = new StoryAdapter(this, cursor);
        recyclerView.setAdapter(recyclerAdapter);

        // TODO -> Set up a drawer

        TextView textView = (TextView) findViewById(R.id.quote); // TextView used for quotes
        // randomQuoteTask will generate a random quote and place it in the TextView
        new randomQuoteTask(textView).execute();

        recyclerAdapter.setClickListener(new StoryAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // Clicking on a ListView row will return a cursor
                // Get the position of the user click to generate the cursor
                Cursor cursor = (Cursor) recyclerAdapter.get

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

            @Override
            public void onItemLongClick(int position, View v) {
                Cursor cursor = (Cursor) recyclerLayout.getItemAtPosition(position);

                deleteSBDialog(cursor.getInt(cursor.getColumnIndex(
                        Constants.DB_ID)), // Get the _id
                        Constants.STORY_TABLE, // Send over table...
                        Constants.DB_ID); //  ...and the column name
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