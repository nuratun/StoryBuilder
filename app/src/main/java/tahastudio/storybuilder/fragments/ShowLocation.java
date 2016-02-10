package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.database.Cursor;
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
import tahastudio.storybuilder.tasks.ShowElementsTask;

/**
 * Fragment to show saved location info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowLocation extends Fragment {

    public ShowLocation() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_location, container, false);

        // Grab the bundle info from the class, Story
        Bundle bundle = this.getArguments();
        String title = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements
        final EditText name = (EditText) layout.findViewById(R.id.sb_location_name);
        final EditText location = (EditText) layout.findViewById(R.id.sb_location_location);
        final EditText desc = (EditText) layout.findViewById(R.id.sb_location_desc);
        final EditText importance = (EditText) layout.findViewById(R.id.sb_location_importance);
        final EditText events = (EditText) layout.findViewById(R.id.sb_location_events);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_location_notes);

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_LOCATION_TABLE, title, id) {
            @Override
            public void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_NAME)));
                    location.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_LOCATION)));
                    desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_DESC)));
                    importance.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_IMPORTANCE)));
                    events.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_EVENTS)));
                    notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_NOTES)));
                }
            }
        }.execute();

        // On the save button click, update the db row
        Button update =  (Button) layout.findViewById(R.id.add_the_location);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the table to insert the values into
                Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_LOCATION_TABLE);

                ContentValues values = new ContentValues();
                values.put(Constants.STORY_LOCATION_NAME, name.getText().toString());
                values.put(Constants.STORY_LOCATION_LOCATION, location.getText().toString());
                values.put(Constants.STORY_LOCATION_DESC, desc.getText().toString());
                values.put(Constants.STORY_LOCATION_IMPORTANCE, importance.getText().toString());
                values.put(Constants.STORY_LOCATION_EVENTS, events.getText().toString());
                values.put(Constants.STORY_LOCATION_NOTES, notes.getText().toString());

                // Call the update method of StoryProvider, through the ContentResolver
                getActivity().getApplicationContext().getContentResolver()
                        .update(uri, values, Constants.STORY_LOCATION_ID + "=?",
                                new String[] { String.valueOf(id) });

                Toast.makeText(getActivity().getApplicationContext(), "Updating...",
                        Toast.LENGTH_LONG).show();

                // Return to previous fragment, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
