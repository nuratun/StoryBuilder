package tahastudio.storybuilder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment to bring up dialog box where user can input story details
 * and create a new story
 */
public class SBDialog extends DialogFragment {

    public SBDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // This is the API to create a dialog box with different content. For this,
        // just inflate a custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get an instance of the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the layout for the dialog box. Parent view is set to null.
        // Then add the buttons to the dialog box, dynamically, to create the
        // story and to cancel it.
        builder.setView(inflater.inflate(R.layout.fragment_sbdialog, null))
                .setPositiveButton(R.string.create_story, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Get the dialog view
                        Dialog layout = (Dialog) dialog;

                        // Grab the user input from the XML
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

                        // Make sure both title and genre are non-empty strings
                        if (sb_story_title.length() < 1 || sb_story_genre.length() < 1) {
                            Toast.makeText(getActivity().getApplicationContext(), "Both the title"
                                    + " and genre are required fields", Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Creating Story", Toast.LENGTH_LONG).show();

                            // Execute CreateStoryTask, to add entry into database. CreateStoryTask
                            // will take care of passing the values needed to StoryBuilderMain and
                            // starting the new activity on the postExecute method
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
