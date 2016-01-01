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
import android.widget.ListView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.utils.Constants;
import tahastudio.storybuilder.utils.SQLDatabase;


/**
 * 3rd tab for SB
 */
public class AddPlotline extends Fragment {
    // Make the AsyncTask global to stop it when paused
    private setPlotlineList plotlineList;
    private View add_plotline_layout;

    public AddPlotline() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        add_plotline_layout = inflater.inflate(
                R.layout.fragment_add_plotline,
                container,
                false);

        // AsyncTask to populate the ListView
        plotlineList = new setPlotlineList();
        plotlineList.execute();

        return add_plotline_layout;
    }

    // Stop the AsyncTask when user pauses fragment
    @Override
    public void onPause() {
        super.onPause();

        if ( plotlineList != null && plotlineList.getStatus() == AsyncTask.Status.RUNNING ) {
            plotlineList.cancel(true);
        }
    }

    // Resume on return
    @Override
    public void onResume() {
        super.onResume();

        if ( plotlineList == null && plotlineList.getStatus() == AsyncTask.Status.FINISHED ) {
            plotlineList.execute();
        }
    }

    // Populate the ListView with the plotlines for this story. Otherwise, return null
    private class setPlotlineList extends AsyncTask<Void, Void, Cursor> {
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
                return db.getRows(Constants.GRAB_PLOTLINE_DETALIS);
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
                        Constants.STORY_MAIN_PLOTLINE,
                        Constants.STORY_PLOTLINE
                };

                // Get the widget list
                int[] widgets = new int[]{
                        R.id.name_info,
                        R.id.extra_info
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
                ListView add_plotline_listview = (ListView) add_plotline_layout
                        .findViewById(R.id.plotline_listview);
                add_plotline_listview.setAdapter(cursorAdapter);
            }
        }
    }
}
