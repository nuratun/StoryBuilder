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
 * Fragment to replace ListView in AddPlots. Is called by AddPlots.
 */
public class AddPlotElements extends Fragment {

    public AddPlotElements() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the layout for this view
        View plotline_elements_layout = inflater.inflate(
                R.layout.activity_add_plot,
                container,
                false);

        // Find the elements in the layout
        final EditText plotline_title = (EditText) plotline_elements_layout
                .findViewById(R.id.sb_plotline_name);
        final EditText plotline = (EditText) plotline_elements_layout
                .findViewById(R.id.sb_plotline);
        final EditText notes = (EditText) plotline_elements_layout
                .findViewById(R.id.sb_plotline_notes);
        Button add_the_plot = (Button) plotline_elements_layout
                .findViewById(R.id.add_the_plotline);
        Button plot_cancel = (Button) plotline_elements_layout
                .findViewById(R.id.plot_cancel);

        add_the_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure plotline field != null
                if (plotline_title.length() < 1) {
                    Toast.makeText(getContext(), "You must enter at least"
                            + " a short description of the plot", Toast.LENGTH_LONG).show();
                } else {
                    // Send converted strings to background thread
                    addPlotlineTask plotlineTask = new addPlotlineTask(
                            getContext(),
                            ShowStory.PLOT_TYPE, // This is set in the ShowStory method, mPlotline
                            plotline_title.getText().toString(),
                            plotline.getText().toString(),
                            notes.getText().toString());
                    plotlineTask.execute();
                }
                // Return to AddPlots on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        plot_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to AddPlots without saving anything
                getFragmentManager().popBackStackImmediate();
            }
        });

        return plotline_elements_layout;
    }

    // Pass in multiple values to constructor
    private class addPlotlineTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db;
        String plot;
        String plot_title;
        String plotline;
        String notes;

        // Constructor
        public addPlotlineTask(Context context,
                               String plot,
                               String plot_title,
                               String plotline,
                               String notes) {
            this.context = context;
            this.plot = plot;
            this.plot_title = plot_title;
            this.plotline = plotline;
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
                values.put(Constants.STORY_MAIN_PLOT, plot);
                values.put(Constants.STORY_PLOT_TITLE, plot_title);
                values.put(Constants.STORY_PLOT_DESC, plotline);
                values.put(Constants.STORY_PLOT_NOTES, notes);

                // Insert the rows
                db.insertRow(values, Constants.STORY_PLOT_TABLE);

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
