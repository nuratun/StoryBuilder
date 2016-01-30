package tahastudio.storybuilder.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;

/**
 * Delete a story, or a story element (character, location, event)
 * Called from StoryBuilderMain, AddCharacter, AddEvent, AddLocation
 */
public class SBDeleteDialog extends DialogFragment {

    public SBDeleteDialog() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // API to create a dialog box with different content.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popup = inflater.inflate(R.layout.fragment_sbdeletedialog, null);
        builder.setView(popup); // Set the view

        // Set the story _id and table in the onClick method
        final int story_id = getArguments().getInt("id");
        final String table = getArguments().getString("table");
        final String column = getArguments().getString("column");

        // Inflate layout for the dialog box. Add buttons to the dialog box dynamically,
        // to either delete the entry or not.
        builder.setPositiveButton(R.string.delete_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                // Set the table for the uri to send to the StoryProvider
                Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + table);

                // Confirm the deletion to the user
                Toast.makeText(getActivity().getApplicationContext(), "Deleting Entry...",
                        Toast.LENGTH_LONG).show();

                // Call the delete method of StoryProvider, through the ContentResolver
                getActivity().getApplicationContext().getContentResolver()
                        .delete(uri, column + "=?", new String[] { String.valueOf(story_id) });

            }

        }).setNegativeButton(R.string.delete_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SBDeleteDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }
}
