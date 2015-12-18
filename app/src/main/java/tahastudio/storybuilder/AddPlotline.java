package tahastudio.storybuilder;

import android.database.Cursor;
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
    // Get an instance of the SQLDatabase and the listview to populate
    private SQLDatabase db;
    private ListView add_plotline_listview;

    public AddPlotline() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_plotline_layout = inflater.inflate(R.layout.fragment_add_plotline, container,
                false);

        // Find the ListView for this layout
        add_plotline_listview = (ListView) add_plotline_layout.findViewById(R.id.add_plotline_list);

        // TODO -> Create async task for background thread

        // Instantiate the db and get the context
        db = new SQLDatabase(getActivity().getApplicationContext());

        // Create a cursor object to hold the rows
        final Cursor cursor = db.getRows(Constants.GRAB_PLOTLINE_DETALIS);

        // Get the columns
        String[] columns = new String[] {
                Constants.STORY_MAIN_PLOTLINE,
                Constants.STORY_SECONDARY_PLOTLINE,
                Constants.STORY_THIRD_PLOTLINE
        };

        // Get the layout
        int[] widgets = new int[] {
                R.id.add_plotline_list
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.fragment_add_plotline,
                cursor,
                columns,
                widgets,
                0);

        add_plotline_listview.setAdapter(cursorAdapter);

        // TODO -> Setup the FAB

        // Return the layout
        return add_plotline_layout;
    }
}
