package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.shared.SharedKeyboard;

/**
 * Fragment to replace ListView in AddCharacters. Called by AddCharacters.
 */
public class AddPlotElements extends Fragment {
    private int type;

    public AddPlotElements() { }

    // TODO -> Fix up radiogroup methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this view
        View layout = inflater.inflate(
                R.layout.fragment_add_plot, container, false);

        // Find the layout elements
        final EditText title = (EditText) layout.findViewById(R.id.sb_plot_title);
        final EditText desc = (EditText) layout.findViewById(R.id.sb_plot_desc);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_plot_notes);

        // Find the RadioGroup selections
        final RadioGroup plot_type = (RadioGroup) layout.findViewById(R.id.add_plot_type);
        plot_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = plot_type.getCheckedRadioButtonId();

                if ( id == R.id.sb_plot_main ) { type = 1; }
                else if ( id == R.id.sb_plot_sub ) { type = 2; }
                else { type = 0; }
            }
        });

        // On the add button
        Button add = (Button) layout.findViewById(R.id.add_the_plot);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the shared keyboard class to close the virtual keyboard
                SharedKeyboard.closeKeyboard(getActivity(), v);

                // Make sure the name field != null
                if ( title.length() < 1 ) { Toast.makeText(getContext(),
                        "The title of this plot is a required field", Toast.LENGTH_LONG).show();
                }
                else {
                    // The table to insert the ContentValues into
                    Uri uri = Uri.parse(Constants.CONTENT_URI + "/"
                            + Constants.STORY_PLOT_TABLE);

                    ContentValues values = new ContentValues(); // Send data to the ContentProvider

                    values.put(Constants.DB_ID, Constants.SB_ID);
                    values.put(Constants.STORY_PLOT_TITLE, title.getText().toString());
                    values.put(Constants.STORY_PLOT_DESC, desc.getText().toString());
                    values.put(Constants.STORY_PLOT_MAIN, type);
                    values.put(Constants.STORY_PLOT_NOTES, notes.getText().toString());

                    // Call the insert method on StoryProvider, through the ContentResolver
                    // This will allow for automatic updates on the ListView
                    getActivity().getApplicationContext().getContentResolver().insert(uri, values);
                }
                // Return to AddCharacters class on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
