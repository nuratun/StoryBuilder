package tahastudio.storybuilder.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.CreateStoryTask;

/**
 * Fragment to bring up dialog box where user can input story details
 * and create a new story
 */
public class SBDialog extends DialogFragment {
    // The string for the genre array
    private String genre;

    public SBDialog() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // API to create a dialog box with different content.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popup = inflater.inflate(R.layout.fragment_sbdialog, null); // Find the custom layout
        builder.setView(popup); // Set the view

        // Grab user input from the inflated view
        final EditText story_title = (EditText) popup.findViewById(R.id.sb_title);
        final Spinner story_genre = (Spinner) popup.findViewById(R.id.sb_genre);
        final EditText story_notes = (EditText) popup.findViewById(R.id.sb_notes);

        // Create an ArrayAdapter for the spinner, using the genres array
        // Use the default layout from the resource library
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getContext(), R.array.genre, android.R.layout.simple_spinner_item);

        // Use the default layout from the resource library and set the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        story_genre.setAdapter(adapter);

        // On click, grab the selection and convert to string
        story_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genre = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genre = "General"; // If user selects nothing, general is the default
            }
        });

        // Inflate layout for the dialog box. Parent view set to null. Add buttons to the dialog
        // box dynamically, to either create the story or cancel it.
        builder.setPositiveButton(R.string.create_story, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                View view = getActivity().getCurrentFocus();

                if ( view != null ) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getActivity().getApplicationContext().getSystemService
                                    (Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                // Make sure both title and genre != null
                if ( story_title.length() < 1 || genre.length() < 1 ) {
                    Toast.makeText(getActivity().getApplicationContext(), "Both title"
                            + " and genre are required fields", Toast.LENGTH_LONG).show();
                } else {
                    // Execute CreateStoryTask to add entry into database
                    // CreateStoryTask will pass values needed back to ShowStory
                    ContentValues values = new ContentValues();
                    values.put(Constants.STORY_NAME, story_title.getText().toString());
                    values.put(Constants.STORY_GENRE, genre);
                    values.put(Constants.STORY_DESC, story_notes.getText().toString());

                    // Send everything to the AsyncTask, which will take care of sending the
                    // data to the ContentProvider, and returning the story _id
                    new CreateStoryTask(getContext().getApplicationContext(),
                            values, story_title.getText().toString()).execute();
                }
            }
        }).setNegativeButton(R.string.cancel_story, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SBDialog.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
