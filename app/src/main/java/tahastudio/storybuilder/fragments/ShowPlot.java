package tahastudio.storybuilder.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;

/**
 * Fragment to show saved plot info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowPlot extends Fragment {
    private int type;

    public ShowPlot() { }

    // TODO -> Fix up radiogroup methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout = inflater.inflate(R.layout.fragment_add_plot, container, false);

        // Grab the bundle info from the class, Story
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements: text
        final EditText title = (EditText) layout.findViewById(R.id.sb_plot_title);
        final EditText desc = (EditText) layout.findViewById(R.id.sb_plot_desc);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_plot_notes);

        // Find the elements: radio buttons
        final RadioButton main = (RadioButton) layout.findViewById(R.id.sb_plot_main);
        final RadioButton sub = (RadioButton) layout.findViewById(R.id.sb_plot_sub);

        final RadioGroup plot_type = (RadioGroup) layout.findViewById(R.id.add_plot_type);
        // Get radio button values
        plot_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = plot_type.getCheckedRadioButtonId();

                if ( id == R.id.sb_plot_main ) {
                    type = 1; }
                else if ( id == R.id.sb_plot_sub ) {
                    type = 2; }
                else {
                    type = 0; }
            }
        });

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Title is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_PLOT_TABLE, name, id) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    // Check if character is the protagonist.
                    if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_PLOT_MAIN)) == 1 ) {
                        main.setChecked(true); // If true, select the radio button.
                    } // Check if character is the antagonist.
                    else if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_PLOT_MAIN)) == 2 ) {
                        sub.setChecked(true); // If true, select the radio button.
                    }


                    // Get the saved data and present it to the user
                    title.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_TITLE)));
                    desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_DESC)));
                    notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLOT_NOTES)));
                }
            }
        }.execute();

        // On button click, update the db row
        Button update = (Button) layout.findViewById(R.id.add_the_plot);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the table to insert the values into
                Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_PLOT_TABLE);

                ContentValues values = new ContentValues();

                values.put(Constants.STORY_PLOT_TITLE, title.getText().toString());
                values.put(Constants.STORY_PLOT_MAIN, type);
                values.put(Constants.STORY_PLOT_DESC, desc.getText().toString());
                values.put(Constants.STORY_PLOT_NOTES, notes.getText().toString());

                // Call the update method of StoryProvider, through the ContentResolver
                getActivity().getApplicationContext().getContentResolver().update(uri, values,
                        Constants.STORY_PLOT_ID + "=?", new String[] { String.valueOf(id) });

                Toast.makeText(getActivity().getApplicationContext(), "Updating...",
                        Toast.LENGTH_LONG).show();

                // Go back to the previous fragment
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
