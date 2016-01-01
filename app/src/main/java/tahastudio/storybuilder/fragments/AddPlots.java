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
public class AddPlots extends Fragment {
    // Make the AsyncTask global to stop it when paused
    private setPlotList plotList;

    private View add_plotline_layout;
    private ListView add_plotline_listview;

    plotListener plotCallback;

    // Interface to send ListView click back to ShowStory
    public interface plotListener {
        void onPlotSelected(String name);
    }

    public AddPlots() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_plotline_layout = inflater.inflate(
                R.layout.fragment_add_plots,
                container,
                false);

        // AsyncTask to populate the ListView
        plotList = new setPlotList();
        plotList.execute();

        // TODO -> The below code is initialized twice. Need to refactor
        add_plotline_listview =
                (ListView) add_plotline_layout.findViewById(R.id.plotline_listview);
        add_plotline_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Return a cursor with the row data
                Cursor cursor = (Cursor) add_plotline_listview.getItemAtPosition(position);

                // Grab the first field from the row and cast it to a string
                // Send to the interface. Implemented in ShowStory
                plotCallback.onPlotSelected
                        (cursor.getString(cursor.getColumnIndex(Constants.STORY_PLOT_TITLE)));
            }
        });

        return add_plotline_layout;
    }

    // Ensure ShowStory implements the interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            plotCallback = (plotListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement plotListener");
        }
    }

    // Stop the AsyncTask when user pauses fragment
    @Override
    public void onPause() {
        super.onPause();

        if ( plotList != null && plotList.getStatus() == AsyncTask.Status.RUNNING ) {
            plotList.cancel(true);
        }
    }

    // Resume on return
    @Override
    public void onResume() {
        super.onResume();

        if ( plotList == null && plotList.getStatus() == AsyncTask.Status.FINISHED ) {
            plotList.execute();
        }
    }

    // Populate the ListView with the plotlines for this story. Otherwise, return null
    private class setPlotList extends AsyncTask<Void, Void, Cursor> {
        private Context context = getActivity().getApplicationContext();
        private SQLDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            // Get a new db instance and set the context
            db = new SQLDatabase(context);

            try {
                // Return a cursor object that holds the rows
                return db.getRows(Constants.GRAB_PLOT_DETALIS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);

            // If this task hasn't been cancelled yet (see onPause)
            if ( !isCancelled() ) {

                // Get the columns
                String[] columns = new String[]{
                        Constants.STORY_PLOT_TITLE
                };

                // Get the widget list
                int[] widgets = new int[]{
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
                add_plotline_listview = (ListView) add_plotline_layout
                        .findViewById(R.id.plotline_listview);
                add_plotline_listview.setAdapter(cursorAdapter);
            }
        }
    }
}