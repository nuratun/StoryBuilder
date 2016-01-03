package tahastudio.storybuilder.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;

/**
 * Fragment to show saved character info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
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
        new ShowElementsTask(getContext(), 0, name) {
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

        // On button click, update the db row
        Button update_character =
                (Button) show_character_layout.findViewById(R.id.add_the_character);
        update_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO --> Get boolean (1 | 0) for radiobutton
                ContentValues values = new ContentValues();
                values.put(Constants.STORY_CHARACTER_NAME,
                        character_name.getText().toString());
                values.put(Constants.STORY_CHARACTER_AGE,
                        character_age.getText().toString());
                values.put(Constants.STORY_CHARACTER_BIRTHPLACE,
                        character_birthplace.getText().toString());
                values.put(Constants.STORY_CHARACTER_PERSONALITY,
                        character_personality.getText().toString());
                values.put(Constants.STORY_CHARACTER_NOTES,
                        character_notes.getText().toString());

            }
        });

        return show_character_layout;
    }
}
