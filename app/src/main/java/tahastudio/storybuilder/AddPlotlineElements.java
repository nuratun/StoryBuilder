package tahastudio.storybuilder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment to replace ListView in AddPlotline. Is called by AddPlotline.
 */
public class AddPlotlineElements extends Fragment {
    private Context context;

    public AddPlotlineElements() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the layout for this view
        View plotline_elements_layout = inflater.inflate(
                R.layout.activity_add_plotline,
                container,
                false);

        // Get an instance of this context
        context = getActivity().getApplicationContext();

        // Find the elements in the layout
        final CheckBox main_plot = (CheckBox) plotline_elements_layout
                .findViewById(R.id.main_plot_checkbox);
        final EditText plotline = (EditText) plotline_elements_layout
                .findViewById(R.id.sb_plotline);
        final EditText notes = (EditText) plotline_elements_layout
                .findViewById(R.id.sb_plotline_notes);
        Button add_the_plot = (Button) plotline_elements_layout
                .findViewById(R.id.add_the_plotline);

        add_the_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ensure the plotline field is a non-empty value
                if (plotline.length() < 1) {
                    Toast.makeText(context, "You must enter at least"
                            + " a summary of the plot", Toast.LENGTH_LONG).show();
                } else {
                    // Instantiate instance of AsyncTask to send input to background thread
                    addPlotlineTask plotlineTask = new addPlotlineTask(
                            context,
                            false,
                            plotline,
                            notes);
                    plotlineTask.execute();
                }
                // Return to AddPlotline
                getActivity().getFragmentManager().popBackStack();
            }
        });
        // Return the layout
        return plotline_elements_layout;
    }

    // Need to create a constructor to pass in multiple values
    private class addPlotlineTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        Boolean main_plot;
        EditText plotline;
        EditText notes;

        public addPlotlineTask(
                Context context,
                Boolean main_plot,
                EditText plotline,
                EditText notes) {
            this.context = context;
            this.main_plot = main_plot;
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
            SBValues send = new SBValues();

            try {
                send.processValues(context, Constants.STORY_PLOTLINE, plotline,
                        Constants.STORY_PLOTLINE_TABLE);
                send.processValues(context, Constants.STORY_PLOTLINE_NOTES, notes,
                        Constants.STORY_PLOTLINE_TABLE);
                return true;
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
