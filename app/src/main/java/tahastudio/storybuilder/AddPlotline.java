package tahastudio.storybuilder;

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


/**
 * 3rd tab for SB
 */
public class AddPlotline extends Fragment {
    // Need to make the AsyncTask global for this class in order
    // to stop it when needed
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

        // Call the AsyncTask to populate the ListView
        plotlineList = new setPlotlineList();
        plotlineList.execute();

        // Return the layout
        return add_plotline_layout;
    }

    // Need to stop the AsyncTask when user moves away from this fragment
    @Override
    public void onStop() {
        super.onStop();

        if ( plotlineList != null && plotlineList.getStatus() == AsyncTask.Status.RUNNING ) {
            plotlineList.cancel(true);
        }
    }

    // Use the AsyncTask to populate the ListView with the plotlines for this story
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

            // If this task hasn't been cancelled yet
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

                SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                        context,
                        R.layout.tab_view,
                        result,
                        columns,
                        widgets,
                        0);

                // Notify thread the data has changed
                cursorAdapter.notifyDataSetChanged();

                ListView add_plotline_listview = (ListView) add_plotline_layout
                        .findViewById(R.id.plotline_listview);
                add_plotline_listview.setAdapter(cursorAdapter);
            }
        }
    }
}
