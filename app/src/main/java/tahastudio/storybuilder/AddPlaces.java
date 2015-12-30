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
 * Second tab for SB
 */
public class AddPlaces extends Fragment {
    // Need to make the AsyncTask global for this class in order
    // to stop it when needed
    setPlaceList placeList;
    private View add_place_layout;

    public AddPlaces() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_place_layout = inflater.inflate(
                R.layout.fragment_add_places,
                container,
                false);

        // Call the AsyncTask to populate the ListView
        placeList = new setPlaceList();
        placeList.execute();

        // Return the layout
        return add_place_layout;
    }

    // Need to stop the AsyncTask when the back button is pressed otherwise
    // it will continue running and mess up the global SB_ID variable
    public void onStop() {
        super.onStop();

        if ( placeList != null && placeList.getStatus() == AsyncTask.Status.RUNNING ) {
            placeList.cancel(true);
        }
    }

    // This AsyncTask will populate the ListView with the places in this story
    private class setPlaceList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            // Get a new db instance and set the context
            db = new SQLDatabase(context);

            try {
                // Create a Cursor object to hold the rows
                return db.getRows(Constants.GRAB_PLACES_DETAILS);
            } catch (Exception e) {
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
                String[] columns = new String[]{
                        Constants.DB_ID,
                        Constants.STORY_PLACE_NAME,
                        Constants.STORY_PLACE_LOCATION
                };

                // Get the TextView widgets
                int[] widgets = new int[]{
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

                // Find the ListView in the layout
                ListView add_places_listview = (ListView) add_place_layout
                        .findViewById(R.id.places_listview);
                add_places_listview.setAdapter(cursorAdapter);
            }
        }
    }
}
