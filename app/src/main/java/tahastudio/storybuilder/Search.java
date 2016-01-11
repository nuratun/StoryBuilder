package tahastudio.storybuilder;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        }
    }

    // AsyncTask to query the database with the intent
    private class searchTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground (String...params) {

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
        }
    }
}
