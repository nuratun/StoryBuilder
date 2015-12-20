package tahastudio.storybuilder;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


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
                Constants.STORY_PLACE_NAME,
                Constants.STORY_PLACE_LOCATION
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

        // Find and initialize the FAB on button click
        FloatingActionButton add_a_place = (FloatingActionButton) add_place_layout
                .findViewById(R.id.add_place_fab);
        add_a_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlaceElements(v);
            }
        });

        // Return the layout
        return add_place_layout;
    }

    // Create the pop-up window to start creating a place/location in the story
    public void addPlaceElements(View view) {
        // Set activity of pop-up window
        final PopupWindow popup = new PopupWindow(getActivity().getApplicationContext());

        // Inflate the layout to use in this pop-up window
        final View layout = getActivity().getLayoutInflater().inflate(R.layout
                .activity_add_place,
                null);

        // Set the view inside the pop-up
        popup.setContentView(layout);

        // Set height/width of the pop-up
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // Set touch parameter and focusable -> both true
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);

        // Set the location
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0, 0);

        // Now start finding the elements
        final EditText place_name = (EditText) layout.findViewById(R.id.sb_place_name);
        final EditText place_location = (EditText) layout.findViewById(R.id.sb_place_location);
        final EditText place_description = (EditText) layout.findViewById(R.id.sb_place_desc);
        final EditText place_notes = (EditText) layout.findViewById(R.id.sb_place_notes);
        final Button add_place = (Button) layout.findViewById(R.id.add_the_place);
        // TODO -> Add a cancel button

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert user input to text
                String sb_name = place_name.getText().toString();
                String sb_location = place_location.getText().toString();
                String sb_description = place_description.getText().toString();
                String sb_notes = place_notes.getText().toString();

                // Make sure name field is a non-empty value
                if ( sb_name.length() < 1 ) {
                    Toast.makeText(getActivity().getApplicationContext(), "Name is a required "
                            + "field", Toast.LENGTH_LONG).show();
                }

                else {
                    // Format values into their respective db fields
                    ContentValues values = new ContentValues();
                    values.put(Constants.STORY_PLACE_NAME, sb_name);
                    values.put(Constants.STORY_PLACE_LOCATION, sb_location);
                    values.put(Constants.STORY_PLACE_DESC, sb_description);
                    values.put(Constants.STORY_PLACE_NOTES, sb_notes);

                    // Initialize db
                    db = new SQLDatabase(getActivity().getApplicationContext());

                    // Insert rows
                    db.insertRow(values, Constants.STORY_PLACES_TABLE);

                    // Dismiss the pop-up window
                    popup.dismiss();
                }
            }
        });
    }
}
