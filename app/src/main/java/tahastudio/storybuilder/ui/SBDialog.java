package tahastudio.storybuilder.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.tasks.CreateStoryTask;

/**
 * Fragment to bring up dialog box where user can input story details
 * and create a new story
 */
public class SBDialog extends DialogFragment {
    // The string for the genre array
    String sb_story_genre;

    public SBDialog() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // API to create a dialog box with different content.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popup = inflater.inflate(R.layout.fragment_sbdialog, null); // Find the custom layout
        builder.setView(popup); // Set the view

        // Grab user input from the inflated view
        final EditText the_story_title = (EditText) popup.findViewById(R.id.sb_title);
        final Spinner the_story_genre = (Spinner) popup.findViewById(R.id.sb_genre);
        final EditText the_story_notes = (EditText) popup.findViewById(R.id.sb_notes);

        // Create an ArrayAdapter for the spinner, using the genres array
        // Use the default layout from the resource library
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (getContext(), R.array.genre, android.R.layout.simple_spinner_item);

        // Use the default layout from the resource library and set the adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        the_story_genre.setAdapter(adapter);

        // On click, grab the selection and convert to string
        the_story_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sb_story_genre = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sb_story_genre = "General"; // If user selects nothing, general is the default
            }
        });

        // Inflate layout for the dialog box. Parent view set to null. Add buttons to the dialog
        // box dynamically, to either create the story or cancel it.
        builder.setPositiveButton(R.string.create_story, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Make sure both title and genre != null
                if (the_story_title.length() < 1 || sb_story_genre.length() < 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Both the title"
                            + " and genre are required fields", Toast.LENGTH_LONG).show();
                } else {
                    // Execute CreateStoryTask to add entry into database
                    // CreateStoryTask will pass values needed back to ShowStory
                            CreateStoryTask task = new CreateStoryTask(
                            getContext(),
                            the_story_title.getText().toString().replace("'","\'"), // Pass string
                            sb_story_genre, // Already a string
                            the_story_notes.getText().toString().replace("'","\'")); // Pass string
                    task.execute();
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
