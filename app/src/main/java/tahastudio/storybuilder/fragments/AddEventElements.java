package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;

/**
 * Fragment to replace ListView in AddEvents. Is called by AddEvents.
 */
public class AddEventElements extends Fragment {

    public AddEventElements() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the layout for this view
        View layout = inflater.inflate(R.layout.activity_add_event, container, false);

        // Find the elements in the layout
        final EditText event_title = (EditText) layout.findViewById(R.id.sb_event_name);
        final EditText event = (EditText) layout.findViewById(R.id.sb_event);
        final EditText characters = (EditText) layout.findViewById(R.id.sb_event_characters);
        final EditText summary = (EditText) layout.findViewById(R.id.sb_event_summary);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_event_notes);
        Button add_the_event = (Button) layout.findViewById(R.id.add_the_event);

        add_the_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> refactor into one method
                // Close the keyboard
                if ( getActivity().getCurrentFocus() != null ) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getActivity().getApplicationContext().getSystemService
                                    (Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow
                            (getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                // Ensure event field != null
                if (event_title.length() < 1) {
                    Toast.makeText(getContext(), "You must enter a short description of this event",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Create the URI for the insert method in StoryProvider
                    Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_EVENT_TABLE);

                    ContentValues values = new ContentValues();

                    values.put(Constants.DB_ID, Constants.SB_ID);
                    values.put(Constants.STORY_EVENT_LINER, event_title.getText().toString());
                    values.put(Constants.STORY_EVENT_DESC, event.getText().toString());
                    values.put(Constants.STORY_EVENT_CHARACTERS, characters.getText().toString());
                    values.put(Constants.STORY_EVENT_SUMMARY, summary.getText().toString());
                    values.put(Constants.STORY_EVENT_NOTES, notes.getText().toString());

                    // Call the insert method on StoryProvider, through the ContentResolver
                    // This will allow for automatic updates on the ListView
                    getActivity().getApplicationContext().getContentResolver().insert(uri, values);
                }
                // Return to AddEvents on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
