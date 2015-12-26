package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    private ListView add_characters_listview;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_character_layout = inflater.inflate(
                R.layout.fragment_add_characters,
                container,
                false);

        // Find the ListView in the layout
        add_characters_listview = (ListView) add_character_layout
                .findViewById(R.id.add_characters_list);

        // Run the AsyncTask to fill in the ListView
        setCharacterList characterList = new setCharacterList();
        characterList.execute();

        // Get the FAB for the story creation. Clicking on this FAB should
        // instantiate the AddCharacterElements fragment
        FloatingActionButton the_character_fab = (FloatingActionButton)
                add_character_layout.findViewById(R.id.add_character_fab);
        the_character_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To programmatically add in the AddCharacterElements fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Need to get APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction
                        .add(R.id.add_characters_tab, new AddCharacterElements())
                        .addToBackStack("add_the_character")
                        .commit();
            }
        });

        // Return this view
        return add_character_layout;
    }

    // This AsyncTask will populate the ListView with a list of characters saved for this story
    private class setCharacterList extends AsyncTask<Void, Void, Cursor> {
        private Context context;
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
            // Get an instance of the application context for the db
            context = getActivity().getApplicationContext();
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

            // Get the column names
            String[] columns = new String[] {
                    Constants.DB_ID,
                    Constants.STORY_CHARACTER,
                    Constants.STORY_AGE
            };

            // Get the ListView widget
            int[] widgets = new int[] {
                    R.id.add_characters_list
            };

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    context,
                    R.layout.fragment_add_characters,
                    result,
                    columns,
                    widgets,
                    0);

            add_characters_listview.setAdapter(cursorAdapter);
        }
    }
}
