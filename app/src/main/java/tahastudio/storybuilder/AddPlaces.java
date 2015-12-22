package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
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
    private Context context = getActivity().getApplicationContext();

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
        add_places_listview = (ListView) add_place_layout.findViewById(R.id.add_places_list);

        // TODO -> Create async task for background threads

        // Instantiate the db and get the context
        db = new SQLDatabase(context);

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
                context,
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
        final PopupWindow popup = new PopupWindow(context);

        // Inflate the layout to use in this pop-up window
        final View layout = getActivity().getLayoutInflater().inflate(
                R.layout.activity_add_place,
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
        popup.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);

        Button add_place = (Button) layout.findViewById(R.id.add_the_place);
        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> Add a cancel button

                // Instantiate AsyncTask to send to background thread
                addPlacesTask addPlacesTask = new addPlacesTask();

                // Now start finding the elements
                EditText place_name = (EditText) layout.findViewById(R.id.sb_place_name);
                EditText place_location = (EditText) layout.findViewById(R.id.sb_place_location);
                EditText place_description = (EditText) layout.findViewById(R.id.sb_place_desc);
                EditText place_notes = (EditText) layout.findViewById(R.id.sb_place_notes);

                // Make sure name field is a non-empty value
                if ( place_name.length() < 1 ) {
                    Toast.makeText(context, "Name is a required "
                            + "field", Toast.LENGTH_LONG).show();
                }

                else {
                    // Send to background thread
                    addPlacesTask.execute(place_name,
                            place_location,
                            place_description,
                            place_notes);
                }
            }
        });
        popup.dismiss();
    }

    private class addPlacesTask extends AsyncTask<EditText, Void, Boolean> {
        private SBValues send = new SBValues();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(EditText... params) {
            Boolean completed = true;

            // Send to SBValues to process into text and add to db
            send.processValues(context, Constants.STORY_PLACE_NAME, params[0],
                    Constants.STORY_PLACES_TABLE);
            send.processValues(context, Constants.STORY_PLACE_LOCATION, params[1],
                    Constants.STORY_PLACES_TABLE);
            send.processValues(context, Constants.STORY_PLACE_DESC, params[2],
                    Constants.STORY_PLACES_TABLE);
            send.processValues(context, Constants.STORY_PLACE_NOTES, params[3],
                    Constants.STORY_PLACES_TABLE);

            return completed;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}
