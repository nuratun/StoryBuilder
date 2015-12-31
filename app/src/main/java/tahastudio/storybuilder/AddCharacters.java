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
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // Make the AsyncTask global to stop it onPause
    private setCharacterList characterList;
    private View add_character_layout;
    private ListView add_characters_listview;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_character_layout = inflater.inflate(
                R.layout.fragment_add_characters,
                container,
                false);

        // Run an AsyncTask to fill in the ListView from the db
        characterList = new setCharacterList();
        characterList.execute();

        // TODO -> The below code is initialized twice. Need to refactor
        add_characters_listview = (ListView) add_character_layout
                .findViewById(R.id.characters_listview);

        // When a user clicks on a character row, bring up a new fragment with edit fields
        add_characters_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Click on a row will return a cursor. The cursor will hold that row data
                Cursor cursor = (Cursor) add_characters_listview.getItemAtPosition(position);

                // Grab the first field from the row to send to UpdateElementsTask
                String name = cursor.getString(cursor.getColumnIndex(Constants.STORY_CHARACTER));

                // Send the context, table int, and character name to UpdateElementsTask
                UpdateElementsTask updateElementsTask =
                        new UpdateElementsTask(getContext(), 0, name);
                updateElementsTask.execute();
            }
        });

        return add_character_layout;
    }

    // Stop the AsyncTask when user pauses the fragment
    @Override
    public void onPause() {
        super.onPause();

        if ( characterList != null && characterList.getStatus() == AsyncTask.Status.RUNNING ) {
            characterList.cancel(true);
        }
    }

    // Resume on return
    @Override
    public void onResume() {
        super.onResume();

        if ( characterList == null && characterList.getStatus() == AsyncTask.Status.FINISHED ) {
            characterList.execute();
        }
    }

    // Populate the ListView with a list of characters saved for this story
    // Otherwise, returns null
    private class setCharacterList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db;

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

            // If this task hasn't been cancelled yet (see onPause)
            if ( !isCancelled() ) {

                // Get the column names
                String[] columns = new String[] {
                        Constants.STORY_CHARACTER,
                        Constants.STORY_AGE
                };

                // Get the TextView widgets
                int[] widgets = new int[] {
                        R.id.name_info,
                        R.id.extra_info
                };

                // Set up the adapter
                SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                        context,
                        R.layout.tab_view,
                        result,
                        columns,
                        widgets,
                        0);

                // Notify thread the data has changed
                cursorAdapter.notifyDataSetChanged();

                add_characters_listview = (ListView) add_character_layout
                        .findViewById(R.id.characters_listview);
                add_characters_listview.setAdapter(cursorAdapter);
            }
        }
    }
}
