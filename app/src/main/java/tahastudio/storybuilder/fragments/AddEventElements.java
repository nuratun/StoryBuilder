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
 * Fragment to replace ListView in AddEvents. Is called by AddEvents.
 */
public class AddEventElements extends Fragment {

    public AddEventElements() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the layout for this view
        View event_elements_layout = inflater.inflate(
                R.layout.activity_add_event,
                container,
                false);

        // Find the elements in the layout
        final EditText event_title = (EditText) event_elements_layout
                .findViewById(R.id.sb_event_name);
        final EditText event = (EditText) event_elements_layout
                .findViewById(R.id.sb_event);
        final EditText notes = (EditText) event_elements_layout
                .findViewById(R.id.sb_event_notes);
        Button add_the_event = (Button) event_elements_layout
                .findViewById(R.id.add_the_event);
        Button event_cancel = (Button) event_elements_layout
                .findViewById(R.id.event_cancel);

        add_the_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure event field != null
                if (event_title.length() < 1) {
                    Toast.makeText(getContext(), "You must enter at least"
                            + " a short description of the event", Toast.LENGTH_LONG).show();
                } else {
                    // Send converted strings to background thread
                    addEventTask eventTask = new addEventTask(
                            getContext(),
                            event_title.getText().toString(),
                            event.getText().toString(),
                            notes.getText().toString());
                    eventTask.execute();
                }
                // Return to AddEvents on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        event_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to AddEvents without saving anything
                getFragmentManager().popBackStackImmediate();
            }
        });

        return event_elements_layout;
    }

    // Pass in multiple values to constructor
    private class addEventTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db;
        String event_title;
        String event;
        String notes;

        // Constructor
        public addEventTask(Context context,
                               String event_title,
                               String event,
                               String notes) {
            this.context = context;
            this.event_title = event_title;
            this.event = event;
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
                values.put(Constants.STORY_EVENT_LINER, event_title);
                values.put(Constants.STORY_EVENT_DESC, event);
                values.put(Constants.STORY_EVENT_NOTES, notes);

                // Insert the rows
                db.insertRow(values, Constants.STORY_EVENT_TABLE);

                return true; // If successful
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

    }
}
