package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


/**
 * 3rd tab for SB
 */
public class AddPlotline extends Fragment {
    // Get an instance of the SQLDatabase and the listview to populate
    private SQLDatabase db;
    private ListView add_plotline_listview;
    private SBValues send = new SBValues();

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
                Constants.STORY_SECONDARY_PLOTLINE
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

        // Find the FAB to create the pop-up window
        FloatingActionButton the_plotline_fab = (FloatingActionButton)
                add_plotline_layout.findViewById(R.id.add_plotline_fab);
        the_plotline_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlotlineElements(v);
            }
        });

        // Return the layout
        return add_plotline_layout;
    }

    // Create the pop-up window from FAB click to add a plotline
    public void addPlotlineElements(View view) {
        // Get an instance of the context
        final Context context = getActivity().getApplicationContext();

        // Create pop-up window with above context
        PopupWindow popup = new PopupWindow(context);

        // Get the layout
        final View layout = getActivity().getLayoutInflater().inflate(
                R.layout.activity_add_plotline,
                null);

        // Set view in pop-up window
        popup.setContentView(layout);

        // Set height and width of pop-up window
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        // Set focusable and outside touchable -> true
        popup.setFocusable(true);
        popup.setOutsideTouchable(true);

        // Set location
        popup.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);

        // Check if this is the main plot -> true/false
        boolean main_plot_checked = CreateStory.mPlotline(view);

        // Now get the elements
        final String main_plot = String.valueOf(main_plot_checked);
        final EditText plotline = (EditText) layout.findViewById(R.id.sb_plotline);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_plotline_notes);
        Button add_the_plot = (Button) layout.findViewById(R.id.add_the_plotline);

        add_the_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure the plotline field is a non-empty value
                if ( plotline.length() < 1 ) {
                    Toast.makeText(context, "You must enter at least"
                        + " a summary of the plot", Toast.LENGTH_LONG).show();
                }

                else {
                    // Send to SBValues

                    // If this is the main plot
                    if ( main_plot.equals("true") ) {
                        // Put in the main plotline field in the db
                        send.processValues(context, Constants.STORY_MAIN_PLOTLINE, plotline,
                                Constants.STORY_PLOTLINE_TABLE);
                    }
                    else {
                        // Otherwise it's a secondary plotline
                        send.processValues(context, Constants.STORY_SECONDARY_PLOTLINE, plotline,
                                Constants.STORY_PLOTLINE_TABLE);
                    }
                    send.processValues(context, Constants.STORY_PLOTLINE_NOTES, notes,
                            Constants.STORY_PLOTLINE_TABLE);
                }
            }
        });

        popup.dismiss();
    }
}
