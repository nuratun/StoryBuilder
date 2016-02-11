package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.shared.SharedKeyboard;

/**
 * Fragment to replace ListView in AddLocations. Is called by AddLocations.
 */
public class AddLocationElements extends Fragment {

    // TODO -> Combine all *Element classes into one, with variable

    public AddLocationElements() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_location, container, false);

        // Find layout elements
        final EditText location_name = (EditText) layout.findViewById(R.id.sb_location_name);
        final EditText location_location = (EditText) layout.findViewById(R.id.sb_location_location);
        final EditText location_description = (EditText) layout.findViewById(R.id.sb_location_desc);
        final EditText location_importance = (EditText) layout.findViewById(R.id.sb_location_importance);
        final EditText location_events = (EditText) layout.findViewById(R.id.sb_location_events);

        final EditText location_notes = (EditText) layout.findViewById(R.id.sb_location_notes);
        Button add_location = (Button) layout.findViewById(R.id.add_the_location);

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the shared keyboard class to close the virtual keyboard
                SharedKeyboard.closeKeyboard(getActivity(), v);

                // Make sure name field != null
                if ( location_name.length() < 1 ) { Toast.makeText(getContext(),
                        "Name is a required field", Toast.LENGTH_LONG).show();
                }
                else {
                    // Create a URI to send to the StoryProvider insert method
                    Uri uri = Uri.parse(Constants.CONTENT_URI + "/"
                            + Constants.STORY_LOCATION_TABLE);

                    ContentValues values = new ContentValues();

                    values.put(Constants.DB_ID, Constants.SB_ID);
                    values.put(Constants.STORY_LOCATION_NAME,
                            location_name.getText().toString());
                    values.put(Constants.STORY_LOCATION_LOCATION,
                            location_location.getText().toString());
                    values.put(Constants.STORY_LOCATION_DESC,
                            location_description.getText().toString());
                    values.put(Constants.STORY_LOCATION_IMPORTANCE,
                            location_importance.getText().toString());
                    values.put(Constants.STORY_LOCATION_EVENTS,
                            location_events.getText().toString());
                    values.put(Constants.STORY_LOCATION_NOTES,
                            location_notes.getText().toString());

                    getActivity().getApplicationContext().getContentResolver().insert(uri, values);
                }
                // Return to AddLocations on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}