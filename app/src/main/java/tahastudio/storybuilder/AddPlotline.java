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
    private ListView add_plotline_listview;

    public AddPlotline() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_plotline_layout = inflater.inflate(
                R.layout.fragment_add_plotline,
                container,
                false);

        // Find the ListView for this layout
        add_plotline_listview = (ListView) add_plotline_layout
                .findViewById(R.id.add_plotline_list);

        // Call the AsyncTask to populate the ListView
        setPlotlineList plotlineList = new setPlotlineList();
        plotlineList.execute();

        // Return the layout
        return add_plotline_layout;
    }

    // Use the AsyncTask to populate the ListView with the plotlines for this story
    private class setPlotlineList extends AsyncTask<Void, Void, Cursor> {
        private Context context;
        private SQLDatabase db;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            // Get an instance of the application context
            context = getActivity().getApplicationContext();
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

            // Get the columns
            String[] columns = new String[] {
                    Constants.DB_ID,
                    Constants.STORY_MAIN_PLOTLINE,
                    Constants.STORY_PLOTLINE
            };

            // Get the widget list
            int[] widgets = new int[] {
                    R.id.add_plotline_list
            };

            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                    context,
                    R.layout.fragment_add_plotline,
                    result,
                    columns,
                    widgets,
                    0);

            add_plotline_listview.setAdapter(cursorAdapter);
        }
    }
}
