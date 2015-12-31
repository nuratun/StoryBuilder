package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // Need to make the AsyncTask global for this class in order
    // to stop it when needed
    private setCharacterList characterList;
    private View add_character_layout;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_character_layout = inflater.inflate(
                R.layout.fragment_add_characters,
                container,
                false);

        // Run the AsyncTask to fill in the ListView
        characterList = new setCharacterList();
        characterList.execute();

        // Return this view
        return add_character_layout;
    }

    // Need to stop the AsyncTask when activity is paused
    @Override
    public void onStop() {
        super.onStop();

        if ( characterList != null && characterList.getStatus() == AsyncTask.Status.RUNNING ) {
            characterList.cancel(true);
        }
    }

    // This AsyncTask will populate the ListView with a list of characters saved for this story
    private class setCharacterList extends AsyncTask<Void, Void, Cursor> {
        private SQLDatabase db;
        private Context context = getActivity().getApplicationContext();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            // Get a new db instance and set the context
            db = new SQLDatabase(context);

            try {
                // Return a Cursor object that holds the rows
                return db.getRows(Constants.GRAB_CHARACTER_DETAILS);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // If this task hasn't been cancelled yet
            if ( !isCancelled() ) {

                // Get the column names
                String[] columns = new String[] {
                        Constants.DB_ID,
                        Constants.STORY_CHARACTER,
                        Constants.STORY_AGE
                };

                // Get the TextView widgets
                int[] widgets = new int[] {
                        R.id.db_id,
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

                // Notify thread the data has changed
                cursorAdapter.notifyDataSetChanged();

                ListView add_characters_listview = (ListView) add_character_layout
                        .findViewById(R.id.characters_listview);
                add_characters_listview.setAdapter(cursorAdapter);

            }
        }
    }
}
