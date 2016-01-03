package tahastudio.storybuilder.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;

/**
 * Fragment to show saved place info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowPlace extends Fragment {

    public ShowPlace() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View add_place_layout =
                inflater.inflate(R.layout.activity_add_place, container, false);

        // TODO -> Update fields on button click
        // Find the elements
        final EditText place_name =
                (EditText) add_place_layout.findViewById(R.id.sb_place_name);
        final EditText place_location =
                (EditText) add_place_layout.findViewById(R.id.sb_place_location);
        final EditText place_desc =
                (EditText) add_place_layout.findViewById(R.id.sb_place_desc);
        final EditText place_notes =
                (EditText) add_place_layout.findViewById(R.id.sb_place_notes);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Int 1 == places table in db
        // Name is string from bundle
        new ShowElementsTask(getContext(), 1, name) {
            @Override
            public void onPostExecute(Cursor result) {
                if ( result != null ) {
                    result.moveToFirst();

                    place_name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLACE_NAME)));
                    place_location.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLACE_LOCATION)));
                    place_desc.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLACE_DESC)));
                    place_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_PLACE_NOTES)));
                }
            }
        }.execute();

        return add_place_layout;
    }
}
