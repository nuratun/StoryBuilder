package tahastudio.storybuilder.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.tasks.ShowElementsTask;

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
        final View layout = inflater.inflate(R.layout.fragment_add_character, container, false);

        // Grab the bundle info from the class, Story
        Bundle bundle = this.getArguments();
        String title = bundle.getString("name");
        final int id = bundle.getInt("id");

        // Find the elements: text
        final EditText name = (EditText) layout.findViewById(R.id.sb_character_name);
        final EditText age = (EditText) layout.findViewById(R.id.sb_character_age);
        final EditText birthplace = (EditText) layout.findViewById(R.id.sb_character_birthplace);
        final EditText history = (EditText) layout.findViewById(R.id.sb_character_history);
        final EditText goals = (EditText) layout.findViewById(R.id.sb_character_goals);
        final EditText conflicts = (EditText) layout.findViewById(R.id.sb_character_conflicts);
        final EditText epiphany = (EditText) layout.findViewById(R.id.sb_character_epiphany);
        final EditText personality = (EditText) layout.findViewById(R.id.sb_character_personality);
        final EditText notes = (EditText) layout.findViewById(R.id.sb_character_notes);

        // Find the elements: radio buttons
        final RadioButton protagonist = (RadioButton) layout.findViewById(R.id.sb_character_main);
        final RadioButton antagonist = (RadioButton) layout.findViewById(R.id.sb_character_antagonist);
        final RadioButton gender_male = (RadioButton) layout.findViewById(R.id.male_gender);
        final RadioButton gender_female = (RadioButton) layout.findViewById(R.id.female_gender);
        final RadioButton gender_other = (RadioButton) layout.findViewById(R.id.other_gender);

        final RadioGroup character_type = (RadioGroup) layout.findViewById(R.id.add_character_position);
        // Get radio button values
        character_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_type.getCheckedRadioButtonId();

                if ( id == R.id.sb_character_main ) {
                    type = 1; }
                else if ( id == R.id.sb_character_antagonist ) {
                    type = 2; }
                else {
                    type = 0; }
            }
        });

        final RadioGroup character_gender = (RadioGroup) layout.findViewById(R.id.gender);
        character_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_gender.getCheckedRadioButtonId();

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
        // Title is string from bundle
        new ShowElementsTask(getContext(), Constants.STORY_CHARACTER_TABLE, title, id) {
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
                    name.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_NAME)));
                    age.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_AGE)));
                    birthplace.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_BIRTHPLACE)));
                    history.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_HISTORY)));
                    goals.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_GOALS)));
                    conflicts.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_CONFLICTS)));
                    epiphany.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_EPIPHANY)));
                    personality.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_PERSONALITY)));
                    notes.setText(result.getString(
                            result.getColumnIndex(Constants.STORY_CHARACTER_NOTES)));
                }
            }
        }.execute();

        // On button click, update the db row
        Button update = (Button) layout.findViewById(R.id.add_the_character);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the table to insert the values into
                Uri uri = Uri.parse(Constants.CONTENT_URI + "/" + Constants.STORY_CHARACTER_TABLE);

                ContentValues values = new ContentValues();

                values.put(Constants.STORY_CHARACTER_NAME, name.getText().toString());
                values.put(Constants.STORY_CHARACTER_AGE, age.getText().toString());
                values.put(Constants.STORY_CHARACTER_TYPE, type);
                values.put(Constants.STORY_CHARACTER_GENDER, gender);
                values.put(Constants.STORY_CHARACTER_BIRTHPLACE, birthplace.getText().toString());
                values.put(Constants.STORY_CHARACTER_HISTORY, history.getText().toString());
                values.put(Constants.STORY_CHARACTER_GOALS, goals.getText().toString());
                values.put(Constants.STORY_CHARACTER_CONFLICTS, conflicts.getText().toString());
                values.put(Constants.STORY_CHARACTER_EPIPHANY, epiphany.getText().toString());
                values.put(Constants.STORY_CHARACTER_PERSONALITY, personality.getText().toString());
                values.put(Constants.STORY_CHARACTER_NOTES, notes.getText().toString());

                // Call the update method of StoryProvider, through the ContentResolver
                getActivity().getApplicationContext().getContentResolver().update(uri, values,
                        Constants.STORY_CHARACTER_ID + "=?", new String[] { String.valueOf(id) });

                Toast.makeText(getActivity().getApplicationContext(), "Updating...",
                        Toast.LENGTH_LONG).show();

                // Go back to the previous fragment
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
