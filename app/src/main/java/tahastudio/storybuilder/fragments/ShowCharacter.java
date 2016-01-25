package tahastudio.storybuilder.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;
import tahastudio.storybuilder.tasks.UpdateElementsTask;

/**
 * Fragment to show saved character info from db
 * Calls: ShowElementsTask
 * Overrides: ShowElementsTask.onPostExecute()
 */
public class ShowCharacter extends Fragment {
    private int type;
    private int gender;

    public ShowCharacter() { }

    // TODO -> Fix up radiogroup methods

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
        final RadioButton protagonist = (RadioButton) show_character_layout
                .findViewById(R.id.sb_character_main);
        final RadioButton antagonist = (RadioButton) show_character_layout
                .findViewById(R.id.sb_character_antagonist);
        final RadioButton gender_male = (RadioButton) show_character_layout
                .findViewById(R.id.male_gender);
        final RadioButton gender_female = (RadioButton) show_character_layout
                .findViewById(R.id.female_gender);
        final RadioButton gender_other = (RadioButton) show_character_layout
                .findViewById(R.id.other_gender);
        final RadioGroup character_type = (RadioGroup) show_character_layout
                .findViewById(R.id.add_character_position);

        // Get radio button values
        character_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_type.getCheckedRadioButtonId();

                Log.d("just type", String.valueOf(id));
                if ( id == R.id.sb_character_main ) {
                    type = 1; }
                else if ( id == R.id.sb_character_antagonist ) {
                    type = 2; }
                else {
                    type = 0; }
            }
        });

        final RadioGroup character_gender = (RadioGroup) show_character_layout
                .findViewById(R.id.gender);
        character_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_gender.getCheckedRadioButtonId();

                Log.d("just gender", String.valueOf(id));
                if ( id == R.id.male_gender ) {
                    gender = 1; }
                else if ( id == R.id.female_gender ) {
                    gender = 2; }
                else if ( id == R.id.other_gender ) {
                    gender = 3; }
                else { gender = 0; }
            }
        });

        // The following AsyncTask is contained in its own class.
        // Therefore, to receive the return value, override onPostExecute
        // Name is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_CHARACTER_TABLE, name, id) {
            @Override
            protected void onPostExecute(Cursor result) {
                if ( result.moveToFirst() ) {

                    // Check if character is the protagonist.
                    if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_CHARACTER_TYPE)) == 1 ) {
                        protagonist.setChecked(true); // If true, select the radio button.
                    } // Check if character is the antagonist.
                    else if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_CHARACTER_TYPE)) == 2 ) {
                        antagonist.setChecked(true); // If true, select the radio button.
                    }

                    // Check if character gender is male
                    if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) == 1 ) {
                        gender_male.setChecked(true); // If true, select the radio button.
                    } // Check if character gender is female
                    else if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) == 2 ) {
                        gender_female.setChecked(true); // If true, select the radio button.
                    } // Check if character gender is other
                    else if ( result.getInt(result.getColumnIndex
                            (Constants.STORY_CHARACTER_GENDER)) == 3 ) {
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

                Log.d("type and gender", String.valueOf(type) + " " + String.valueOf(gender));
                values.put(Constants.STORY_CHARACTER_NAME,
                        character_name.getText().toString());
                values.put(Constants.STORY_CHARACTER_AGE,
                        character_age.getText().toString());
                values.put(Constants.STORY_CHARACTER_TYPE, type);
                values.put(Constants.STORY_CHARACTER_GENDER, gender);
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

        return show_character_layout;
    }
}
