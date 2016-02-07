package tahastudio.storybuilder.shared;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import tahastudio.storybuilder.ui.SBDeleteDialog;

/**
 * Shared class to bring up the delete dialog
 */
public class DeleteDialogBox extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public DeleteDialogBox() { }

    public void deleteSBDialog(int position, String table, String column) {
        // Bundle the story id for the delete dialog
        Bundle bundle = new Bundle();
        bundle.putInt("id", position); // The id is the _id for the character entry in the db
        bundle.putString("table", table); // The db table name
        bundle.putString("column", column); // The column for the where clause

        SBDeleteDialog deleteDialog = new SBDeleteDialog();
        deleteDialog.setArguments(bundle); // Send the bundle over to the dialog

        deleteDialog.show(fragmentManager, "delete");
    }
}
