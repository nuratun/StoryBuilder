package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.shared.SharedKeyboard;

/**
 * Fragment to replace ListView in AddCharacters. Called by AddCharacters.
 */
public class AddCharacterElements extends Fragment {
    private int type;
    private int gender;

    public AddCharacterElements() { }

    // TODO -> Fix up radiogroup methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this view
        View layout = inflater.inflate(
                R.layout.activity_add_character,
                container,
                false);

        // Find the layout elements
        final EditText name = (EditText) layout.findViewById(R.id.sb_character_name);
        final EditText age = (EditText) layout.findViewById(R.id.sb_character_age);
        final EditText birthplace = (EditText) layout.findViewById(R.id.sb_character_birthplace);
        final EditText history = (EditText) layout.findViewById(R.id.sb_character_history);
        final EditText goals = (EditText) layout.findViewById(R.id.sb_character_goals);
        final EditText conflicts = (EditText) layout.findViewById(R.id.sb_character_conflicts);
        final EditText epiphany = (EditText) layout.findViewById(R.id.sb_character_epiphany);
        final EditText personality = (EditText) layout.findViewById(R.id.sb_character_personality);
        final EditText character_notes = (EditText) layout.findViewById(R.id.sb_character_notes);

        // Find the RadioGroup selections
        final RadioGroup character_type = (RadioGroup) layout.findViewById(R.id.add_character_position);
        character_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_type.getCheckedRadioButtonId();

                if ( id == R.id.sb_character_main ) { type = 1; }
                else if ( id == R.id.sb_character_antagonist ) { type = 2; }
                else { type = 0; }
            }
        });

        // Find the RadioGroup selections
        final RadioGroup character_gender = (RadioGroup) layout.findViewById(R.id.gender);
        character_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = character_gender.getCheckedRadioButtonId();

                if ( id == R.id.male_gender ) { gender = 1; }
                else if ( id == R.id.female_gender ) { gender = 2; }
                else if ( id == R.id.other_gender ) { gender = 3; }
                else { gender = 0; }
            }
        });

        // On the add button
        Button add = (Button) layout.findViewById(R.id.add_the_character);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the shared keyboard class to close the virtual keyboard
                SharedKeyboard.closeKeyboard(getActivity(), v);

                // Make sure the name field != null
                if ( name.length() < 1 ) {
                    Toast.makeText(getContext(), "The character's name is a required field",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    // The table to insert the ContentValues into
                    Uri uri = Uri.parse(Constants.CONTENT_URI + "/"
                            + Constants.STORY_CHARACTER_TABLE);

                    ContentValues values = new ContentValues(); // Send data to the ContentProvider

                    values.put(Constants.DB_ID, Constants.SB_ID);
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
                    values.put(Constants.STORY_CHARACTER_NOTES, character_notes.getText().toString());

                    // Call the insert method on StoryProvider, through the ContentResolver
                    // This will allow for automatic updates on the ListView
                    getActivity().getApplicationContext().getContentResolver().insert(uri, values);
                }
                // Return to AddCharacters class on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return layout;
    }
}
