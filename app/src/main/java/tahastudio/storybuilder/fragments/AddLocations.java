package tahastudio.storybuilder.fragments;

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

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;


/**
 * Second tab for SB
 */
public class AddLocations extends Fragment {
    // Make the AsyncTask global to stop it on onPause
    private setLocationList locationList;

    private View add_location_layout;
    private ListView add_locations_listview;

    // For interface method
    locationListener locationCallback;

    public AddLocations() { }

    // Interface to send ListView click back to ShowStory
    public interface locationListener {
        void onLocationSelected(int id, String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_location_layout = inflater.inflate(R.layout.fragment_add_locations, container, false);

        // Run an AsyncTask to fill in the ListView from the db
        locationList = new setLocationList();
        locationList.execute();

        // TODO -> The below code is initialized twice. Need to refactor
        add_locations_listview =
                (ListView) add_location_layout.findViewById(R.id.locations_listview);
        // Clicking on a location row will bring up a new fragment with info
        // TODO --> Long click brings up the delete option
        add_locations_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) add_locations_listview.getItemAtPosition(position);

                // Grab the first field from the row and cast it to a string
                // Send to the interface. Implemented in ShowStory
                locationCallback.onLocationSelected
                        (cursor.getInt(cursor.getColumnIndex(Constants.STORY_LOCATION_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_LOCATION_NAME)));
            }
        });

        return add_location_layout;
    }

    // Ensure ShowStory implements the interface
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            locationCallback = (locationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement locationListener");
        }
    }

    // Start, or restart, AsyncTask when user views the fragment
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ( isVisibleToUser ) { // isVisibleToUser is set to true as default
            if ( locationList == null || locationList.getStatus() == AsyncTask.Status.FINISHED ) {
                new setLocationList().execute();
            }
        }
    }

    // Populate the ListView with the locations in this story. Otherwise, return null
    private class setLocationList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db = SQLDatabase.getInstance(context); // An instance of the database

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {

            try {
                // Create a Cursor object to hold the rows
                // Need to add in the _id of the story, as the
                // GRAB_LOCATION_DETAILS string is a final static string
                return db.getRows(Constants.GRAB_LOCATION_DETAILS + Constants.SB_ID);
            } catch (Exception e) {
                e.printStackTrace(); }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // Get the column names
            String[] columns = new String[] {
                    Constants.STORY_LOCATION_ID,
                    Constants.STORY_LOCATION_NAME,
                    Constants.STORY_LOCATION_LOCATION
            };

            // Get the TextView widgets
            int[] widgets = new int[] {
                    R.id.element_id,
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

            // Initialize
            add_locations_listview = (ListView) add_location_layout
                    .findViewById(R.id.locations_listview);
            add_locations_listview.setAdapter(cursorAdapter);
        }
    }
}
