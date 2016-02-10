package tahastudio.storybuilder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.adapters.StoryAdapter;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.ui.SBDeleteDialog;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private RecyclerView recyclerView;
    private StoryAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

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

    // Interface to send ListView click back to the class, Story
    public interface characterListener {
        void onCharacterSelected(int id, String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.story_elements, container, false);

        // Set the context
        context = getActivity().getApplicationContext();

        // Set the layout manager for the RecyclerView
        recyclerView = (RecyclerView) layout.findViewById(R.id.element_list);
        recyclerLayout = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerLayout);

        // Set the adapter (Cursor) for the RecyclerView
        recyclerAdapter = new StoryAdapter(
                context,
                Constants.STORY_CHARACTER_NAME,
                Constants.STORY_CHARACTER_AGE,
                Constants.STORY_CHARACTER_BIRTHPLACE);
        recyclerView.setAdapter(recyclerAdapter);

        // To initialize the LoaderManager
        getLoaderManager().initLoader(Constants.LOADER, null, this);

        // When an item on the RecyclerView is clicked, load the element
        recyclerAdapter.setOnItemClickListener(new StoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Cursor cursor) {
                // Grab the id and name from the row and send to the interface.
                // Implemented in the class, Story
                characterCallback.onCharacterSelected(cursor.getInt(
                        cursor.getColumnIndex(Constants.STORY_CHARACTER_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
            }
        });

        // When long clicked, bring up the delete dialog box
        recyclerAdapter.setOnItemLongClickListener(new StoryAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(Cursor cursor) {

                SBDeleteDialog deleteDialog = new SBDeleteDialog();
                deleteDialog.delete(cursor.getInt(cursor.getColumnIndex(
                        Constants.STORY_CHARACTER_ID)),
                        Constants.STORY_CHARACTER_TABLE,
                        Constants.STORY_CHARACTER_ID);
                deleteDialog.show(getFragmentManager(), "delete");
            }
        });

        return layout;
    }

    // Ensure the class, Story, implements the interface
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

        // Call the query method on the ContentProvider
        context.getContentResolver().query(uri, from, null, null, null);

        // Send the URI and the string[] to StoryProvider to interface with the db
        // This will be returned to onLoadFinished
        return new android.support.v4.content.CursorLoader(context, uri, from,
                Constants.DB_ID + "=?", new String[] { String.valueOf(Constants.SB_ID) }, null);
    }

    // Once data is returned from onCreateLoader, swap the empty cursor
    // from onCreateView with a fresh one
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        recyclerAdapter.swapCursor(cursor);
    }

    // Reset the entire cursor when the fragment starts from the beginning
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerAdapter.swapCursor(null);
    }
}
