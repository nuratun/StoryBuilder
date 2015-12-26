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
 * Second tab for SB
 */
public class AddPlaces extends Fragment {
    private ListView add_places_listview;

    public AddPlaces() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        View add_place_layout = inflater.inflate(
                R.layout.fragment_add_places,
                container,
                false);

        // Find the ListView in the layout
        add_places_listview = (ListView) add_place_layout
                .findViewById(R.id.add_places_list);

        // Call the AsyncTask to populate the ListView
        setPlaceList placeList = new setPlaceList();
        placeList.execute();

        // Find the FAB button and on click, grab the AddPlaceElements fragment
        FloatingActionButton add_a_place = (FloatingActionButton) add_place_layout
                .findViewById(R.id.add_place_fab);
        add_a_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To programmatically add in the AddPlaceElements fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Need to get APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction.add(R.id.add_places_tab, new AddPlaceElements())
                        .addToBackStack("add_the_place")
                        .commit();
            }
        });
        // Return the layout
        return add_place_layout;
    }

    // This AsyncTask will populate the ListView with the places in this story
    private class setPlaceList extends AsyncTask<Void, Void, Cursor> {
        private Context context;
        private SQLDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            // Get an instance of the application context for the db
            context = getActivity().getApplicationContext();
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

            // Get the column names
            String[] columns = new String[] {
                    Constants.DB_ID,
                    Constants.STORY_PLACE_NAME,
                    Constants.STORY_PLACE_LOCATION
            };

            // Get the ListView widget
            int[] widgets = new int[] {
                    R.id.add_places_list
            };

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    context,
                    R.layout.fragment_add_places,
                    result,
                    columns,
                    widgets,
                    0);

            add_places_listview.setAdapter(cursorAdapter);
        }
    }
}
