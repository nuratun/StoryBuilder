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
import tahastudio.storybuilder.ShowStory;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;
import tahastudio.storybuilder.tasks.UpdateElementsTask;

/**
 * Fragment to show saved character info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowCharacter extends Fragment {

    public ShowCharacter() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View show_character_layout =
                inflater.inflate(R.layout.activity_add_character, container, false);

        // Grab the bundle info from ShowStory
        Bundle bundle = this.getArguments();
        String name = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements: text
        final EditText character_name =
                (EditText) show_character_layout.findViewById(R.id.sb_character_name);
        final EditText character_age =
                (EditText) show_character_layout.findViewById(R.id.sb_character_age);
        final EditText character_birthplace =
                (EditText) show_character_layout.findViewById(R.id.sb_character_birthplace);
        final EditText character_history =
                (EditText) show_character_layout.findViewById(R.id.sb_character_history);
        final EditText character_goals =
                (EditText) show_character_layout.findViewById(R.id.sb_character_goals);
        final EditText character_conflicts =
                (EditText) show_character_layout.findViewById(R.id.sb_character_conflicts);
        final EditText character_epiphany =
                (EditText) show_character_layout.findViewById(R.id.sb_character_epiphany);
        final EditText character_personality =
                (EditText) show_character_layout.findViewById(R.id.sb_character_personality);
        final EditText character_notes =
                (EditText) show_character_layout.findViewById(R.id.sb_character_notes);

        // Find the elements: radio buttons
        final RadioButton character_protagonist =
                (RadioButton) show_character_layout.findViewById(R.id.sb_character_main);
        final RadioButton character_antagonist =
                (RadioButton) show_character_layout.findViewById(R.id.sb_character_antagonist);
        final RadioButton gender_male =
                (RadioButton) show_character_layout.findViewById(R.id.male_gender);
        final RadioButton gender_female =
                (RadioButton) show_character_layout.findViewById(R.id.female_gender);
        final RadioButton gender_other =
                (RadioButton) show_character_layout.findViewById(R.id.other_gender);

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_CHARACTER_TABLE, name) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    // Check if user saved the character as the protagonist.
                    if ( result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_TYPE)) != null && result.getString
                            (result.getColumnIndex(Constants.STORY_CHARACTER_TYPE))
                            .equals(Constants.CHARACTER_TYPE_PROTAGONIST) ) {
                        character_protagonist.setChecked(true); // If true, select the radio button.
                    } // Check if user saved the character as the antagonist.
                    else if ( result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_TYPE)) != null && result.getString
                            (result.getColumnIndex(Constants.STORY_CHARACTER_TYPE))
                            .equals(Constants.CHARACTER_TYPE_ANTAGONIST) ) {
                        character_antagonist.setChecked(true); // If true, select the radio button.
                    }

                    // Check if user selected character gender as male
                    if ( result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) != null &&
                            result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER))
                            .equals(Constants.CHARACTER_GENDER_MALE) ) {
                        gender_male.setChecked(true); // If true, select the radio button.
                    } // Check if user selected character gender as female
                    else if ( result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) != null &&
                            result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER))
                            .equals(Constants.CHARACTER_GENDER_FEMALE) ) {
                        gender_female.setChecked(true); // If true, select the radio button.
                    } // Check if user selected character gender as other
                    else if ( result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) != null &&
                            result.getString(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER))
                            .equals(Constants.CHARACTER_GENDER_OTHER)) {
                        gender_other.setChecked(true);  // If true, select the radio button.
                    }

                    // Get the saved data and present it to the user
                    character_name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
                    character_age.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_AGE)));
                    character_birthplace.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_BIRTHPLACE)));
                    character_history.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_HISTORY)));
                    character_goals.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_GOALS)));
                    character_conflicts.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_CONFLICTS)));
                    character_epiphany.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_EPIPHANY)));
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
                ContentValues values = new ContentValues();

                values.put(Constants.STORY_CHARACTER_NAME,
                        character_name.getText().toString());
                values.put(Constants.STORY_CHARACTER_AGE,
                        character_age.getText().toString());
                values.put(Constants.STORY_CHARACTER_TYPE,
                        ShowStory.CHARACTER_TYPE);
                values.put(Constants.STORY_CHARACTER_GENDER,
                        ShowStory.CHARACTER_GENDER);
                values.put(Constants.STORY_CHARACTER_BIRTHPLACE,
                        character_birthplace.getText().toString());
                values.put(Constants.STORY_CHARACTER_HISTORY,
                        character_history.getText().toString());
                values.put(Constants.STORY_CHARACTER_GOALS,
                        character_goals.getText().toString());
                values.put(Constants.STORY_CHARACTER_CONFLICTS,
                        character_conflicts.getText().toString());
                values.put(Constants.STORY_CHARACTER_EPIPHANY,
                        character_epiphany.getText().toString());
                values.put(Constants.STORY_CHARACTER_PERSONALITY,
                        character_personality.getText().toString());
                values.put(Constants.STORY_CHARACTER_NOTES,
                        character_notes.getText().toString());

                // Send to the AsyncTask
                UpdateElementsTask updateElementsTask = new UpdateElementsTask
                        (getContext(),
                                values,
                                Constants.STORY_CHARACTER_TABLE,
                                Constants.STORY_CHARACTER_ID, // The ID is needed for the update
                                id); // in the database class
                updateElementsTask.execute();

                // Go back to the previous fragment
                getFragmentManager().popBackStackImmediate();
            }
        });

        Button cancel_update = (Button) show_character_layout.findViewById(R.id.character_cancel);
        cancel_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Leave the fragment immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return show_character_layout;
    }
}
