package tahastudio.storybuilder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.ui.SBDeleteDialog;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // To update the ListView through the LoaderManager
    private SimpleCursorAdapter cursorAdapter;
    private android.support.v4.content.CursorLoader cursorLoader;
    private ListView characters_listview;

    // From String[] for the cursor
    private String[] from = new String[] {
            Constants.DB_ID,
            Constants.STORY_CHARACTER_ID,
            Constants.STORY_CHARACTER_NAME,
            Constants.STORY_CHARACTER_AGE,
            Constants.STORY_CHARACTER_BIRTHPLACE };

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
        View layout = inflater.inflate(R.layout.story_elements, container, false);

         characters_listview = (ListView) layout.findViewById(R.id.element_listview);

        // To int[] for the SimpleCursorAdapter
        int[] to = new int[] {
                R.id.story_id,
                R.id.element_id,
                R.id.name_info,
                R.id.extra_info,
                R.id.desc };

        // Set up the adapter to add to the ListView
        cursorAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.tab_view,
                null,
                from,
                to,
                0);
        characters_listview.setAdapter(cursorAdapter);

        // To initialize the LoaderManager
        getLoaderManager().initLoader(Constants.LOADER, null, this);

        // Clicking on a character row will bring up a new fragment with info
        characters_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) characters_listview.getItemAtPosition(position);

                // Grab the id and name from the row and send to the interface.
                // Implemented in ShowStory
                characterCallback.onCharacterSelected
                        (cursor.getInt(cursor.getColumnIndex(Constants.STORY_CHARACTER_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
            }
        });

        // Bring up the delete dialog box on long click
        characters_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) characters_listview.getItemAtPosition(position);

                SBDeleteDialog deleteDialog = new SBDeleteDialog();
                deleteDialog.delete(cursor.getInt(cursor.getColumnIndex(
                        Constants.STORY_CHARACTER_ID)),
                        Constants.STORY_CHARACTER_TABLE,
                        Constants.STORY_CHARACTER_ID);
                deleteDialog.show(getFragmentManager(), "delete");

                return true;
            }
        });

        return layout;
    }

    // Ensure ShowStory implements the interface
    @Override
    public void onAttach(Context context) {
       super.onAttach(context);
        try {
            characterCallback = (characterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement characterListener");
        }
    }

    // Must implement the below methods for the LoaderManager
    public Loader<Cursor> onCreateLoader(int num, Bundle state) {
        // This URI will be sent to a switch statement in the StoryProvider. It will
        // set the tables on setTables() method in the db to pull the data for the ListView
        Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_CHARACTER_TABLE);

        // Send the URI and the string[] to StoryProvider to interface with the db
        // This will be returned to onLoadFinished
        cursorLoader = new android.support.v4.content.CursorLoader(
                getActivity().getApplicationContext(), uri, from,
                Constants.DB_ID + "=?", new String[] { String.valueOf(Constants.SB_ID) }, null);

        return cursorLoader;
    }

    // Once data is returned from onCreateLoader, swap the empty cursor
    // from onCreateView with a fresh one
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    // Reset the entire cursor when the fragment starts from the beginning
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
