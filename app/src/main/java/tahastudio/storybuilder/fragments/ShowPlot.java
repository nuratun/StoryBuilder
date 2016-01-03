package tahastudio.storybuilder.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;

/**
 * Fragment to show saved plot info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowPlot extends Fragment {

    public ShowPlot() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View show_plot_layout =
                inflater.inflate(R.layout.activity_add_plot, container, false);

        // TODO -> Add in the checkbox
        // TODO -> Update fields on button click
        // Find the elements
        CheckBox plot_main = (CheckBox) show_plot_layout.findViewById(R.id.main_plot_checkbox);
        final EditText plot_title = (EditText) show_plot_layout.findViewById(R.id.sb_plotline_name);
        final EditText plot_desc = (EditText) show_plot_layout.findViewById(R.id.sb_plotline);
        final EditText plot_notes = (EditText) show_plot_layout.findViewById(R.id.sb_plotline_notes);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Int 2 == plots table in db
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.PLOTS_TABLE, name) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result != null ) {
                    result.moveToFirst();

                    plot_title.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_TITLE)));
                    plot_desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_DESC)));
                    plot_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_NOTES)));
                }
            }
        }.execute();

        return show_plot_layout;
    }
}
