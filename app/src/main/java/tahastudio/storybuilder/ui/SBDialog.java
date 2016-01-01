package tahastudio.storybuilder.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import tahastudio.storybuilder.tasks.CreateStoryTask;
import tahastudio.storybuilder.R;

/**
 * Fragment to bring up dialog box where user can input story details
 * and create a new story
 */
public class SBDialog extends DialogFragment {

    public SBDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //  API to create a dialog box with different content.
        // For this instance, just inflate a custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate layout for the dialog box. Parent view set to null.
        // Add buttons to the dialog box dynamically, to either
        // create the story or cancel it.
        builder.setView(inflater.inflate(R.layout.fragment_sbdialog, null))
                .setPositiveButton(R.string.create_story, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Get dialog view
                        Dialog layout = (Dialog) dialog;

                        // Grab user input from XML
                        EditText the_story_title = (EditText) layout
                                .findViewById(R.id.sb_title);
                        EditText the_story_genre = (EditText) layout
                                .findViewById(R.id.sb_genre);
                        EditText the_story_notes = (EditText) layout
                                .findViewById(R.id.sb_notes);

                        // Get the string conversions of the input
                        String sb_story_title = the_story_title.getText().toString();
                        String sb_story_genre = the_story_genre.getText().toString();
                        String sb_story_notes = the_story_notes.getText().toString();

                        // Make sure both title and genre are non-empty
                        if (sb_story_title.length() < 1 || sb_story_genre.length() < 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "Both the title"
                                    + " and genre are required fields", Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Creating Story", Toast.LENGTH_LONG).show();

                            // Execute CreateStoryTask to add entry into database
                            // CreateStoryTask will take care of passing values needed
                            // back to the ShowStory class
                            CreateStoryTask task = new CreateStoryTask(
                                    getActivity().getApplicationContext(),
                                    sb_story_title,
                                    sb_story_genre,
                                    sb_story_notes);
                            task.execute();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_story, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SBDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
