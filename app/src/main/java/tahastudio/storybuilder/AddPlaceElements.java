package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment to replace ListView in AddPlaces. Is called by AddPlaces.
 */
public class AddPlaceElements extends Fragment {
    private Context context;

    public AddPlaceElements() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View place_elements_layout = inflater.inflate(
                R.layout.activity_add_place,
                container,
                false);

        // Get an instance of this application context
        context = getActivity().getApplicationContext();

        // Now start finding the elements
        final EditText place_name = (EditText) place_elements_layout
                .findViewById(R.id.sb_place_name);
        final EditText place_location = (EditText) place_elements_layout
                .findViewById(R.id.sb_place_location);
        final EditText place_description = (EditText) place_elements_layout
                .findViewById(R.id.sb_place_desc);
        final EditText place_notes = (EditText) place_elements_layout
                .findViewById(R.id.sb_place_notes);
        Button add_place = (Button) place_elements_layout
                .findViewById(R.id.add_the_place);

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure name field is a non-empty value
                if ( place_name.length() < 1 ) {
                    Toast.makeText(context, "Name is a required "
                            + "field", Toast.LENGTH_LONG).show();
                }

                else {
                    // Convert EditText fields to equivalent strings
                    String convert_place_name = place_name.getText().toString();
                    String convert_place_location = place_location.getText().toString();
                    String convert_place_description = place_description.getText().toString();
                    String convert_place_notes = place_notes.getText().toString();

                    // Send to background thread
                    addPlacesTask placesTask = new addPlacesTask(
                            context,
                            convert_place_name,
                            convert_place_location,
                            convert_place_description,
                            convert_place_notes);
                    placesTask.execute();
                }
                // Return to AddPlaces class
                getFragmentManager().popBackStackImmediate();
            }
        });
        // Return the layout
        return place_elements_layout;
    }

    // Need an AsyncTask with a constructor to pass in multiple values
    private class addPlacesTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db;
        String name;
        String location;
        String description;
        String notes;

        public addPlacesTask(Context context,
                             String name,
                             String location,
                             String description,
                             String notes) {
            this.context = context;
            this.name = name;
            this.location = location;
            this.description = description;
            this.notes = notes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            db = new SQLDatabase(context);
            values = new ContentValues();

            try {
                values.put(Constants.DB_ID, CreateStory.SB_ID);
                values.put(Constants.STORY_PLACE_NAME, name);
                values.put(Constants.STORY_PLACE_LOCATION, location);
                values.put(Constants.STORY_PLACE_DESC, description);
                values.put(Constants.STORY_PLACE_NOTES, notes);

                // Insert the rows
                db.insertRow(values, Constants.STORY_PLACES_TABLE);

                return true; // If successfully inserted values
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false; // If unsuccessful
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}
