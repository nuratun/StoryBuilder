package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


/**
 * 3rd tab for SB
 */
public class AddPlotline extends Fragment {
    // Get an instance of the SQLDatabase and the ListView to populate
    private SQLDatabase db;
    private ListView add_plotline_listview;
    // Get fragment-wide context
    private Context context = getActivity().getApplicationContext();

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

        // TODO -> Create async task for background thread

        // Instantiate the db and get the context
        db = new SQLDatabase(context);

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
                addPlotlineElements(v);
            }
        });

        // Return the layout
        return add_plotline_layout;
    }

    // Create the pop-up window from FAB click to add a plotline
    public void addPlotlineElements(View view) {

        // Create pop-up window with above context
        final PopupWindow popup = new PopupWindow(context);

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

        Button add_the_plot = (Button) layout.findViewById(R.id.add_the_plotline);

        add_the_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Find the elements in activity_add_plotline.xml

                // Check if it's the main plotline or a secondary plotline
                CheckBox main_plot_checked = (CheckBox) layout.findViewById(
                        R.id.main_character_checkbox);

                // Convert to String value
                String main_plot = String.valueOf(main_plot_checked);

                EditText plotline = (EditText) layout.findViewById(R.id.sb_plotline);
                EditText notes = (EditText) layout.findViewById(R.id.sb_plotline_notes);

                // Ensure the plotline field is a non-empty value
                if (plotline.length() < 1) {
                    Toast.makeText(context, "You must enter at least"
                            + " a summary of the plot", Toast.LENGTH_LONG).show();
                } else {
                    // Instantiate instance of AsyncTask to send input to background thread
                    addPlotlineTask plotlineTask = new addPlotlineTask();

                    // If this is the main plot
                    if (main_plot.equals("true")) {
                        // Send to background thread
                        // TODO -> Need to add db entry stating this is the main plotline
                        plotlineTask.execute(plotline, notes);

                    } else {
                        plotlineTask.execute(plotline, notes);
                    }
                }
            }
        });

        // Dismiss this pop-up
        popup.dismiss();
    }

    private class addPlotlineTask extends AsyncTask<EditText, Void, Boolean> {
        private SBValues send = new SBValues();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(EditText... params) {
            Boolean completed = true;

            send.processValues(context, Constants.STORY_PLOTLINE, params[0],
                    Constants.STORY_PLOTLINE_TABLE);
            send.processValues(context, Constants.STORY_PLOTLINE_NOTES, params[1],
                    Constants.STORY_PLOTLINE_TABLE);

            return completed;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

    }
}
