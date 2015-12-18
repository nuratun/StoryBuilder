package tahastudio.storybuilder;

import android.database.Cursor;
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
    // Get an instance of the SQLDatabase and the listview to populate
    private SQLDatabase db;
    private ListView add_places_listview;

    public AddPlaces() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        View add_place_layout = inflater.inflate(R.layout.fragment_add_places, container, false);

        // Find the ListView in the layout
        add_places_listview = (ListView) add_place_layout.findViewById(R.id.add_places_list);

        // TODO -> Create async task for background threads

        // Instantiate the db and get the context
        db = new SQLDatabase(getActivity().getApplicationContext());

        // Create a Cursor object to hold the rows
        final Cursor cursor = db.getRows(Constants.GRAB_PLACES_DETAILS);

        // Get the column names
        String[] columns = new String[] {
                Constants.STORY_MAIN_PLACE,
                Constants.STORY_SEC_PLACE
        };

        int[] widgets = new int[] {
                R.id.add_places_list
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.fragment_add_places,
                cursor,
                columns,
                widgets,
                0);

        add_places_listview.setAdapter(cursorAdapter);

        // TODO -> Get the FAB

        // Return the layout
        return add_place_layout;
    }
}
