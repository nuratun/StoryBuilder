package tahastudio.storybuilder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // To update the ListView
    SimpleCursorAdapter cursorAdapter;

    // Make view components accessible across the class
    private View add_character_layout;
    private ListView add_characters_listview;

    // For interface method
    characterListener characterCallback;

    public AddCharacters() { }

    // Interface to send ListView click back to ShowStory
    public interface characterListener {
        void onCharacterSelected(int id, String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_character_layout =
                inflater.inflate(R.layout.fragment_add_characters, container, false);

        // Run an AsyncTask to fill in the ListView from the db
        new setCharacterList().execute();

        // TODO -> The below code is initialized twice. Need to refactor
        add_characters_listview =
                (ListView) add_character_layout.findViewById(R.id.characters_listview);

        // Clicking on a character row will bring up a new fragment with info
        // TODO --> Long click brings up the delete option
        add_characters_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) add_characters_listview.getItemAtPosition(position);

                // Grab the id and name from the row and send to the interface.
                // Implemented in ShowStory
                characterCallback.onCharacterSelected
                        (cursor.getInt(cursor.getColumnIndex(Constants.STORY_CHARACTER_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
            }
        });

        Log.d("the_resume", "characters on layout");

        return add_character_layout;
    }

    // Ensure ShowStory implements the interface
    @Override
    public void onAttach(Context context) {
       super.onAttach(context);
        Log.d("the_resume", "characters on attach");

        try {
            characterCallback = (characterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement characterListener");
        }
    }

    // Start, the AsyncTask when fragment is resumed
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d("the_resume", "characters on isvisible");

        if ( cursorAdapter != null) {
            cursorAdapter.notifyDataSetChanged();
        }
    }

    // Populate the ListView with characters saved for this story
    // Otherwise, return null
    private class setCharacterList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db = SQLDatabase.getInstance(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("the_resume", "characters on preexecute");
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            try {
                // Return a Cursor object that holds the rows
                // Need to add in the _id of the story, as the
                // GRAB_CHARACTER_DETAILS string is a final static string
                return db.getRows(Constants.GRAB_CHARACTER_DETAILS + Constants.SB_ID);
            }
            catch (Exception e) {
                e.printStackTrace(); }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // Get the column names
            String[] columns = new String[] {
                    Constants.STORY_CHARACTER_ID,
                    Constants.STORY_CHARACTER_NAME,
                    Constants.STORY_CHARACTER_AGE,
                    Constants.STORY_CHARACTER_BIRTHPLACE
            };

            // Get the TextView widgets
            int[] widgets = new int[] {
                    R.id.element_id,
                    R.id.name_info,
                    R.id.extra_info,
                    R.id.desc
            };

            // Set up the adapter
            cursorAdapter = new SimpleCursorAdapter(
                    context,
                    R.layout.tab_view,
                    result,
                    columns,
                    widgets,
                    0);

            // Notify thread the data has changed
            cursorAdapter.notifyDataSetChanged();

            add_characters_listview =
                    (ListView) add_character_layout.findViewById(R.id.characters_listview);
            add_characters_listview.setAdapter(cursorAdapter);
        }
    }
}
