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
 * Fragment to show saved plot info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowEvent extends Fragment {

    public ShowEvent() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_add_event, container, false);

        // Grab the bundle info from the class, Story
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements
        final EditText title = (EditText) layout.findViewById(R.id.sb_event_name);
        final EditText desc = (EditText) layout.findViewById(R.id.sb_event);
        final EditText characters = (EditText) layout.findViewById(R.id.sb_event_characters);
        final EditText summary = (EditText) layout.findViewById(R.id.sb_event_summary);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_event_notes);

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_EVENT_TABLE, name, id) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    title.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_LINER)));
                    desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_DESC)));
                    characters.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_CHARACTERS)));
                    summary.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_SUMMARY)));
                    notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_NOTES)));
                }
            }
        }.execute();

        // On the save button click, update the db row
        Button update = (Button) layout.findViewById(R.id.add_the_event);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the table to insert the values into
                Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_EVENT_TABLE);

                ContentValues values = new ContentValues();

                values.put(Constants.STORY_EVENT_LINER, title.getText().toString());
                values.put(Constants.STORY_EVENT_DESC, desc.getText().toString());
                values.put(Constants.STORY_EVENT_CHARACTERS, characters.getText().toString());
                values.put(Constants.STORY_EVENT_SUMMARY, summary.getText().toString());
                values.put(Constants.STORY_EVENT_NOTES, notes.getText().toString());

                // Call the update method of StoryProvider, through the ContentResolver
                getActivity().getApplicationContext().getContentResolver()
                        .update(uri, values, Constants.ID + "=?", new String[] { String.valueOf(id) });

                Toast.makeText(getActivity().getApplicationContext(), "Updating...",
                        Toast.LENGTH_LONG).show();

                // Leave the fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
