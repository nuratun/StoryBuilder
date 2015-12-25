package tahastudio.storybuilder;

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
                    // Send to background thread
                    addPlacesTask placesTask = new addPlacesTask(
                            context,
                            place_name,
                            place_location,
                            place_description,
                            place_notes);
                    placesTask.execute();
                }
                // Return to AddPlaces class
                getActivity().getFragmentManager().popBackStack();
            }
        });
        // Return the layout
        return place_elements_layout;
    }

    // Need an AsyncTask with a constructor to pass in multiple values
    private class addPlacesTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        EditText name;
        EditText location;
        EditText description;
        EditText notes;

        public addPlacesTask(Context context,
                             EditText name,
                             EditText location,
                             EditText description,
                             EditText notes) {
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
            SBValues send = new SBValues();

            try {
                // Send to SBValues to process into text and add to db
                send.processValues(context, Constants.STORY_PLACE_NAME, name,
                        Constants.STORY_PLACES_TABLE);
                send.processValues(context, Constants.STORY_PLACE_LOCATION, location,
                        Constants.STORY_PLACES_TABLE);
                send.processValues(context, Constants.STORY_PLACE_DESC, description,
                        Constants.STORY_PLACES_TABLE);
                send.processValues(context, Constants.STORY_PLACE_NOTES, notes,
                        Constants.STORY_PLACES_TABLE);
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
