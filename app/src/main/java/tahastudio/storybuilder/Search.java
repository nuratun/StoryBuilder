package tahastudio.storybuilder;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * This activity will launch when a user searches for a dataset.
 * It will run an AsyncTask to populate the ListView with the user's
 * query results.
 */
public class Search extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if ( intent.ACTION_SEARCH.equals(intent.getAction()) ) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchTask searchTask = new searchTask(query);
            searchTask.execute();
        }
    }

    // AsyncTask to query the database with the intent
    private class searchTask extends AsyncTask<String, Void, Cursor> {
        SQLDatabase db = new SQLDatabase(getBaseContext());
        ListView listView = (ListView) findViewById(R.id.search_listview);
        TextView textView = (TextView) findViewById(R.id.search_textview);
        String query;


        public searchTask(String query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listView.setEmptyView(textView); // Set the view to a text string at the start
        }

        @Override
        protected Cursor doInBackground (String...params) {
            return db.getSearchResults(query, null);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            if (result != null) {
                // Get the column names
                String[] columns = {
                        Constants.STORY_NAME,
                };

                // Get the TextView widgets
                int[] widgets = {
                        R.id.search_textview,
                };

                SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                        getBaseContext(),
                        R.layout.search,
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
