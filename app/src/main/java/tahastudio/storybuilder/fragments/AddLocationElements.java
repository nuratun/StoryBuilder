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
 * Fragment to relocation ListView in AddLocations. Is called by AddLocations.
 */
public class AddLocationElements extends Fragment {

    // TODO -> Combine all *Element classes into one, with variable

    public AddLocationElements() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View location_elements_layout = inflater.inflate(
                R.layout.activity_add_location,
                container,
                false);

        // Find layout elements
        final EditText location_name = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_name);
        final EditText location_location = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_location);
        final EditText location_description = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_desc);
        final EditText location_importance = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_importance);
        final EditText location_events = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_events);

        final EditText location_notes = (EditText) location_elements_layout
                .findViewById(R.id.sb_location_notes);
        Button add_location = (Button) location_elements_layout
                .findViewById(R.id.add_the_location);
        Button location_cancel = (Button) location_elements_layout
                .findViewById(R.id.location_cancel);

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure name field != null
                if ( location_name.length() < 1 ) {
                    Toast.makeText(getContext(), "Name is a required "
                            + "field", Toast.LENGTH_LONG).show();
                }
                else {
                    // Send converted string to background thread
                    addLocationsTask locationsTask = new addLocationsTask(
                            getContext(),
                            location_name.getText().toString(),
                            location_location.getText().toString(),
                            location_description.getText().toString(),
                            location_importance.getText().toString(),
                            location_events.getText().toString(),
                            location_notes.getText().toString());
                    locationsTask.execute();
                }
                // Return to AddLocations on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        location_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to AddLocations, without saving anything
                getFragmentManager().popBackStackImmediate();
            }
        });

        return location_elements_layout;
    }

    // Pass multiple values to AsyncTask constructor
    private class addLocationsTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db = SQLDatabase.getInstance(context);
        String name;
        String location;
        String description;
        String importance;
        String events;
        String notes;

        // Constructor
        public addLocationsTask(Context context,
                                String name,
                                String location,
                                String description,
                                String importance,
                                String events,
                                String notes) {
            this.context = context;
            this.name = name;
            this.location = location;
            this.description = description;
            this.importance = importance;
            this.events = events;
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
            values = new ContentValues();

            try {
                values.put(Constants.DB_ID, ShowStory.SB_ID);
                values.put(Constants.STORY_LOCATION_NAME, name);
                values.put(Constants.STORY_LOCATION_LOCATION, location);
                values.put(Constants.STORY_LOCATION_DESC, description);
                values.put(Constants.STORY_LOCATION_IMPORTANCE, importance);
                values.put(Constants.STORY_LOCATION_EVENTS, events);
                values.put(Constants.STORY_LOCATION_NOTES, notes);

                // Insert the rows
                db.insertRow(values, Constants.STORY_LOCATION_TABLE);

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
