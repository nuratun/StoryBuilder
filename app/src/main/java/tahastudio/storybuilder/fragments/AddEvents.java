package tahastudio.storybuilder.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;


/**
 * 3rd tab for SB
 */
public class AddEvents extends Fragment {
    // Make the AsyncTask global to stop it when paused
    private setEventList eventList;

    // Make view components accessible across the class
    private View add_event_layout;
    private ListView add_event_listview;

    // For interface method
    eventListener eventCallback;

    public AddEvents() { }

    // Interface to send ListView click back to ShowStory
    public interface eventListener {
        void onEventSelected(int id, String name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_event_layout = inflater.inflate(R.layout.fragment_add_events, container, false);

        // Run an AsyncTask to fill in the ListView from the db
        eventList = new setEventList();
        eventList.execute();

        // TODO -> The below code is initialized twice. Need to refactor
        add_event_listview =
                (ListView) add_event_layout.findViewById(R.id.event_listview);
        add_event_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) add_event_listview.getItemAtPosition(position);

                // Grab the first field from the row and cast it to a string
                // Send to the interface. Implemented in ShowStory
                eventCallback.onEventSelected
                        (cursor.getInt(cursor.getColumnIndex(Constants.STORY_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndex(Constants.STORY_EVENT_LINER)));
            }
        });

        return add_event_layout;
    }

    // Ensure ShowStory implements the interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            eventCallback = (eventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement eventListener");
        }
    }

    // Start, or restart, the AsyncTask when fragment becomes visible to the user
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if ( isVisibleToUser ) { // isVisibleToUser is set to true as default
            if ( eventList == null || eventList.getStatus() == AsyncTask.Status.FINISHED ) {
                new setEventList().execute();
            }
        }
    }

    // Populate the ListView with the events for this story. Otherwise, return null
    private class setEventList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db = SQLDatabase.getInstance(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            try {
                // Return a cursor object that holds the rows
                // Need to add in the _id of the story, as the
                // GRAB_EVENT_DETAILS string is a final static string
                return db.getRows(Constants.GRAB_EVENT_DETALIS + Constants.SB_ID);
            } catch (Exception e) {
                e.printStackTrace(); }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // Get the columns
            String[] columns = new String[] {
                    Constants.STORY_EVENT_ID,
                    Constants.STORY_EVENT_LINER
            };

            // Get the widget list
            int[] widgets = new int[] {
                    R.id.element_id,
                    R.id.name_info
            };

            // Set up the adapter
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    context,
                    R.layout.tab_view,
                    result,
                    columns,
                    widgets,
                    0);

            // Notify thread the data has changed
            cursorAdapter.notifyDataSetChanged();

            // Initialize
            add_event_listview = (ListView) add_event_layout.findViewById(R.id.event_listview);
            add_event_listview.setAdapter(cursorAdapter);
        }
    }
}
