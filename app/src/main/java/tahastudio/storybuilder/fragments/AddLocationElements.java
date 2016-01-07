package tahastudio.storybuilder.fragments;

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

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.ShowStory;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * Fragment to replace ListView in AddLocations. Is called by AddLocations.
 */
public class AddLocationElements extends Fragment {

    // TODO -> Combine all *Element classes into one, with variable

    public AddLocationElements() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View place_elements_layout = inflater.inflate(
                R.layout.activity_add_location,
                container,
                false);

        // Find layout elements
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
        Button place_cancel = (Button) place_elements_layout
                .findViewById(R.id.place_cancel);

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure name field != null
                if ( place_name.length() < 1 ) {
                    Toast.makeText(getContext(), "Name is a required "
                            + "field", Toast.LENGTH_LONG).show();
                }

                else {

                    // Send converted string to background thread
                    addPlacesTask placesTask = new addPlacesTask(
                            getContext(),
                            place_name.getText().toString(),
                            place_location.getText().toString(),
                            place_description.getText().toString(),
                            place_notes.getText().toString());
                    placesTask.execute();
                }
                // Return to AddLocations on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        place_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to AddLocations, without saving anything
                getFragmentManager().popBackStackImmediate();
            }
        });

        return place_elements_layout;
    }

    // Pass multiple values to AsyncTask constructor
    private class addPlacesTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db;
        String name;
        String location;
        String description;
        String notes;

        // Constructor
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
                values.put(Constants.DB_ID, ShowStory.SB_ID);
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
