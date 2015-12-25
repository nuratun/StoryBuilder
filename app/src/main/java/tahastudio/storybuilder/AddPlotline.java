package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private Context context;

    public AddPlotline() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_plotline_layout = inflater.inflate(R.layout.fragment_add_plotline, container,
                false);

        // Find the ListView for this layout
        add_plotline_listview = (ListView) add_plotline_layout
                .findViewById(R.id.add_plotline_list);

        // Get an instance of the application context
        context = getActivity().getApplicationContext();

        // TODO -> Create async task for background thread

        // Instantiate the db and get the context
        SQLDatabase db = new SQLDatabase(context);

        // Create a cursor object to hold the rows
        final Cursor cursor = db.getRows(Constants.GRAB_PLOTLINE_DETALIS);

        // Get the columns
        String[] columns = new String[] {
                Constants.STORY_MAIN_PLOTLINE,
                Constants.STORY_PLOTLINE
        };

        // Get the layout
        int[] widgets = new int[] {
                R.id.add_plotline_list
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                context,
                R.layout.fragment_add_plotline,
                cursor,
                columns,
                widgets,
                0);

        add_plotline_listview.setAdapter(cursorAdapter);

        // Find the FAB to create the pop-up window
        FloatingActionButton the_plotline_fab = (FloatingActionButton)
                add_plotline_layout.findViewById(R.id.add_plotline_fab);
        the_plotline_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.add_plotline_tab, new AddPlotlineElements())
                        .addToBackStack("add_the_plot")
                        .commit();
            }
        });

        // Return the layout
        return add_plotline_layout;
    }

}
