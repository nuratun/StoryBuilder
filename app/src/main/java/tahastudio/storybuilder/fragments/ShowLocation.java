package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;
import tahastudio.storybuilder.tasks.UpdateElementsTask;

/**
 * Fragment to show saved location info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowLocation extends Fragment {

    public ShowLocation() { }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View add_location_layout =
                inflater.inflate(R.layout.activity_add_location, container, false);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements
        final EditText location_name =
                (EditText) add_location_layout.findViewById(R.id.sb_location_name);
        final EditText location_location =
                (EditText) add_location_layout.findViewById(R.id.sb_location_location);
        final EditText location_desc =
                (EditText) add_location_layout.findViewById(R.id.sb_location_desc);
        final EditText location_importance =
                (EditText) add_location_layout.findViewById(R.id.sb_location_importance);
        final EditText location_events =
                (EditText) add_location_layout.findViewById(R.id.sb_location_events);
        final EditText location_notes =
                (EditText) add_location_layout.findViewById(R.id.sb_location_notes);

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_LOCATION_TABLE, name, id) {
            @Override
            public void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    location_name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_NAME)));
                    location_location.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_LOCATION)));
                    location_desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_DESC)));
                    location_importance.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_IMPORTANCE)));
                    location_events.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_EVENTS)));
                    location_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_LOCATION_NOTES)));
                }
            }
        }.execute();

        // On the save button click, update the db row
        Button location_add =  (Button) add_location_layout.findViewById(R.id.add_the_location);
        location_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(Constants.STORY_LOCATION_NAME,
                        location_name.getText().toString());
                values.put(Constants.STORY_LOCATION_LOCATION,
                        location_location.getText().toString());
                values.put(Constants.STORY_LOCATION_DESC,
                        location_desc.getText().toString());
                values.put(Constants.STORY_LOCATION_IMPORTANCE,
                        location_importance.getText().toString());
                values.put(Constants.STORY_LOCATION_EVENTS,
                        location_events.getText().toString());
                values.put(Constants.STORY_LOCATION_NOTES,
                        location_notes.getText().toString());

                UpdateElementsTask updateElementsTask = new UpdateElementsTask
                        (getContext(),
                                values,
                                Constants.STORY_LOCATION_TABLE,
                                Constants.STORY_LOCATION_ID,
                                id);
                updateElementsTask.execute();

                // Return to previous fragment, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return add_location_layout;
    }
}
