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
 * Fragment to show saved plot info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowEvent extends Fragment {

    public ShowEvent() { }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View show_event_layout =
                inflater.inflate(R.layout.activity_add_event, container, false);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements
        final EditText event_title =
                (EditText) show_event_layout.findViewById(R.id.sb_event_name);
        final EditText event_desc =
                (EditText) show_event_layout.findViewById(R.id.sb_event);
        final EditText event_characters =
                (EditText) show_event_layout.findViewById(R.id.sb_event_characters);
        final EditText event_summary =
                (EditText) show_event_layout.findViewById(R.id.sb_event_summary);
        final EditText event_notes =
                (EditText) show_event_layout.findViewById(R.id.sb_event_notes);

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_EVENT_TABLE, name) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    event_title.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_LINER)));
                    event_desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_DESC)));
                    event_characters.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_CHARACTERS)));
                    event_summary.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_SUMMARY)));
                    event_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_EVENT_NOTES)));
                }
            }
        }.execute();

        // On the save button click, update the db row
        Button add_event = (Button) show_event_layout.findViewById(R.id.add_the_event);
        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                values.put(Constants.STORY_EVENT_LINER,
                        event_title.getText().toString());
                values.put(Constants.STORY_EVENT_DESC,
                        event_desc.getText().toString());
                values.put(Constants.STORY_EVENT_CHARACTERS,
                        event_characters.getText().toString());
                values.put(Constants.STORY_EVENT_SUMMARY,
                        event_summary.getText().toString());
                values.put(Constants.STORY_EVENT_NOTES,
                        event_notes.getText().toString());

                UpdateElementsTask updateElementsTask = new UpdateElementsTask
                        (getContext(),
                                values,
                                Constants.STORY_EVENT_TABLE,
                                Constants.STORY_EVENT_ID,
                                id);
                updateElementsTask.execute();

                // Leave the fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        // Cancel button
        Button cancel = (Button) show_event_layout.findViewById(R.id.event_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Leave this fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return show_event_layout;
    }
}
