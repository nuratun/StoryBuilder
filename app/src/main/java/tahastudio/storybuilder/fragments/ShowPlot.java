package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;
import tahastudio.storybuilder.tasks.UpdateElementsTask;

/**
 * Fragment to show saved plot info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowPlot extends Fragment {
    // For the main plot checkbox
    private String main;

    public ShowPlot() { }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View show_plot_layout =
                inflater.inflate(R.layout.activity_add_plot, container, false);

        // Find the elements
        final CheckBox plot_main =
                (CheckBox) show_plot_layout.findViewById(R.id.main_plot_checkbox);
        final EditText plot_title =
                (EditText) show_plot_layout.findViewById(R.id.sb_plotline_name);
        final EditText plot_desc =
                (EditText) show_plot_layout.findViewById(R.id.sb_plotline);
        final EditText plot_notes =
                (EditText) show_plot_layout.findViewById(R.id.sb_plotline_notes);
        Button add_plot =
                (Button) show_plot_layout.findViewById(R.id.add_the_plotline);
        Button cancel =
                (Button) show_plot_layout.findViewById(R.id.plot_cancel);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.PLOTS_TABLE, name) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result != null ) {
                    result.moveToFirst();

                    // If the main plot checkbox is set in db
                    if ( result.getString(result.getColumnIndex
                            (Constants.STORY_MAIN_PLOT)).equals(Constants.PLOT_MAIN)) {
                        plot_main.setChecked(true); // Set it as checked
                    }

                    plot_title.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_TITLE)));
                    plot_desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_DESC)));
                    plot_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_NOTES)));
                }
            }
        }.execute();

        // On button click, update the db row
        add_plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();

                if ( plot_main.isChecked() ) {
                    // Set the class wide string to the constant
                    main = Constants.PLOT_MAIN;
                } else {
                    main = "False";
                }

                values.put(Constants.STORY_MAIN_PLOT, main);
                values.put(Constants.STORY_PLOT_TITLE, plot_title.getText().toString());
                values.put(Constants.STORY_PLOT_DESC, plot_desc.getText().toString());
                values.put(Constants.STORY_PLOT_NOTES, plot_notes.getText().toString());

                UpdateElementsTask updateElementsTask = new UpdateElementsTask
                        (getContext(), values, Constants.STORY_PLOT_TABLE);
                updateElementsTask.execute();

                // Leave the fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        // Cancel button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Leave this fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return show_plot_layout;
    }
}
