package tahastudio.storybuilder.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.UpdateElementsTask;

/**
 * Fragment to show saved character info from db
 * Calls: UpdateElementsTask
 * Overrides: UpdateElementsTask.onPostExecute()
 */
public class ShowCharacter extends Fragment {

    public ShowCharacter() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View show_character_layout =
                inflater.inflate(R.layout.activity_add_character, container, false);

        // TODO -> Update fields on button click
        // Find the elements
        final EditText character_name =
                (EditText) show_character_layout.findViewById(R.id.sb_character_name);
        final EditText character_age =
                (EditText) show_character_layout.findViewById(R.id.sb_character_age);
        final RadioButton character_protagonist =
                (RadioButton) show_character_layout.findViewById(R.id.sb_character_main);
        RadioButton character_antagonist =
                (RadioButton) show_character_layout.findViewById(R.id.sb_character_antagonist);
        // TODO -> Add in gender radiobutton
        final EditText character_birthplace =
                (EditText) show_character_layout.findViewById(R.id.sb_character_birthplace);
        final EditText character_personality =
                (EditText) show_character_layout.findViewById(R.id.sb_character_personality);
        final EditText character_notes =
                (EditText) show_character_layout.findViewById(R.id.sb_character_notes);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Int 0 == characters table in db
        // Name is string from bundle
        new UpdateElementsTask(getContext(), 0, name) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result != null ) {
                    result.moveToFirst();

                    character_name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
                    character_age.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_AGE)));
                    character_birthplace.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_BIRTHPLACE)));
                    character_personality.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_PERSONALITY)));
                    character_notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_NOTES)));
                }
            }
        }.execute();

        return show_character_layout;
    }
}
