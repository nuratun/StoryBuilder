package tahastudio.storybuilder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.ui.SBDeleteDialog;

/**
 * 3rd tab for SB
 */
public class AddEvents extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // To update the ListView through the LoadManager
    private SimpleCursorAdapter cursorAdapter;
    private android.support.v4.content.CursorLoader cursorLoader;
    private ListView event_listview; // ListView to update

    // From String[] for the cursor
    private String[] from = new String[] {
            Constants.DB_ID,
            Constants.STORY_EVENT_ID,
            Constants.STORY_EVENT_LINER };

    // For interface method
    eventListener eventCallback;

    public AddEvents() { }

    // Interface to send ListView click back to ShowStory
    public interface eventListener {
        void onEventSelected(int id, String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View event_layout = inflater.inflate(R.layout.fragment_add_events, container, false);

        event_listview = (ListView) event_layout.findViewById(R.id.event_listview);

        // To int[] for the SimpleCursorAdapter
        int[] to = new int[] {
                R.id.story_id,
                R.id.element_id,
                R.id.name_info };

        cursorAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.tab_view,
                null,
                from,
                to,
                0);
        event_listview.setAdapter(cursorAdapter);

        // To initialize the LoaderManager
        getLoaderManager().initLoader(Constants.LOADER, null, this);

        // Clicking on an event row will bring up a new fragment with info
        event_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) event_listview.getItemAtPosition(position);

                // Grab the first field from the row and cast it to a string
                // Send to the interface. Implemented in ShowStory
                eventCallback.onEventSelected
                        (cursor.getInt(cursor.getColumnIndex(Constants.STORY_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_EVENT_LINER)));
            }
        });

        // Bring up the delete dialog box on long click
        event_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) event_listview.getItemAtPosition(position);

                deleteSBDialog(cursor.getInt(cursor.getColumnIndex(
                        Constants.STORY_EVENT_ID)),
                        Constants.STORY_EVENT_TABLE,
                        Constants.STORY_EVENT_ID);
                return true;
            }
        });

        return event_layout;
    }

    // TODO -> Factor this out into one method
    // Calls the SBDeleteDialog class to delete a story, or story element
    private void deleteSBDialog(int position, String table, String column) {
        // Bundle the story id for the delete dialog
        Bundle bundle = new Bundle();
        bundle.putInt("id", position); // The id is the _id for the character entry in the db
        bundle.putString("table", table); // The db table name
        bundle.putString("column", column); // The column for the where clause

        SBDeleteDialog deleteDialog = new SBDeleteDialog();
        deleteDialog.setArguments(bundle); // Send the bundle over to the dialog

        deleteDialog.show(getFragmentManager(), "delete_story");
    }

    // Ensure ShowStory implements the interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("the_resume", "events on attach");

        try {
            eventCallback = (eventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement eventListener");
        }
    }

    // Must implement the below methods for the LoaderManager
    @Override
    public Loader<Cursor> onCreateLoader(int num, Bundle state) {
        // This URI will be sent to a switch statement in the StoryProvider. It will
        // set the tables on setTables() method in the db to pull the data for the ListView
        Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_EVENT_TABLE);
        Log.d("uri_parse", String.valueOf(uri));

        // Send the URI and the string[] to StoryProvider to interface with the db
        // This will be returned to onLoadFinished
        cursorLoader = new android.support.v4.content.CursorLoader(
                getActivity().getApplication(), uri, from,
                Constants.DB_ID + "=?", new String[] { String.valueOf(Constants.SB_ID) }, null);

        return cursorLoader;
    }

    // Once data is returned from onCreateLoader, swap the empty cursor
    // from onCreateView with a fresh one
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    // Reset the entire cursor when the fragment starts from the beginning
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
