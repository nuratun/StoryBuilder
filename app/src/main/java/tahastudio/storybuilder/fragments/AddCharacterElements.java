package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

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
        View character_elements_layout = inflater.inflate(
                R.layout.activity_add_character,
                container,
                false);

        // Find the layout elements
        final EditText name = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_name);
        final EditText age = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_age);
        final EditText birthplace = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_birthplace);
        final EditText history = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_history);
        final EditText goals = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_goals);
        final EditText conflicts = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_conflicts);
        final EditText epiphany = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_epiphany);
        final EditText personality = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_personality);
        final EditText character_notes = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_notes);

        final RadioGroup character_type = (RadioGroup) character_elements_layout
                .findViewById(R.id.add_character_position);
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

        final RadioGroup character_gender = (RadioGroup) character_elements_layout
                .findViewById(R.id.gender);
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

        Button add_the_character = (Button) character_elements_layout
                .findViewById(R.id.add_the_character);
        add_the_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> refactor into one method
                if ( getActivity().getCurrentFocus() != null ) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getActivity().getApplicationContext().getSystemService
                                    (Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow
                            (getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                // Make sure the name field != null
                if ( name.length() < 1 ) {
                    Toast.makeText(getContext(), "The character's name "
                            + "is a required field", Toast.LENGTH_LONG).show();
                }
                else {

                    Log.d("type", String.valueOf(type));
                    Log.d("gender", String.valueOf(gender));
                    // Send converted strings and ints to a background thread to process in the db
                    addCharactersTask charactersTask =
                            new addCharactersTask(
                                    getContext(),
                                    name.getText().toString(),
                                    age.getText().toString(),
                                    type,
                                    gender,
                                    birthplace.getText().toString(),
                                    history.getText().toString(),
                                    goals.getText().toString(),
                                    conflicts.getText().toString(),
                                    epiphany.getText().toString(),
                                    personality.getText().toString(),
                                    character_notes.getText().toString());
                    charactersTask.execute();
                }
                // Return to AddCharacters class on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        return character_elements_layout;
    }

    // Pass multiple variables to the constructor
    private class addCharactersTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db = SQLDatabase.getInstance(context);
        private String name;
        private String age;
        private int type;
        private int gender;
        private String birthplace;
        private String history;
        private String goals;
        private String conflicts;
        private String epiphany;
        private String personality;
        private String notes;

        // Constructor
        public addCharactersTask(Context context,
                                 String name,
                                 String age,
                                 int type,
                                 int gender,
                                 String birthplace,
                                 String history,
                                 String goals,
                                 String conflicts,
                                 String epiphany,
                                 String personality,
                                 String notes) {
            this.context = context;
            this.name = name;
            this.age = age;
            this.type = type;
            this.gender = gender;
            this.birthplace = birthplace;
            this.history = history;
            this.goals = goals;
            this.conflicts = conflicts;
            this.epiphany = epiphany;
            this.personality = personality;
            this.notes = notes;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            values = new ContentValues();

            try {
                values.put(Constants.DB_ID, Constants.SB_ID);
                values.put(Constants.STORY_CHARACTER_NAME, name);
                values.put(Constants.STORY_CHARACTER_AGE, age);
                values.put(Constants.STORY_CHARACTER_TYPE, type);
                values.put(Constants.STORY_CHARACTER_GENDER, gender);
                values.put(Constants.STORY_CHARACTER_BIRTHPLACE, birthplace);
                values.put(Constants.STORY_CHARACTER_HISTORY, history);
                values.put(Constants.STORY_CHARACTER_GOALS, goals);
                values.put(Constants.STORY_CHARACTER_CONFLICTS, conflicts);
                values.put(Constants.STORY_CHARACTER_EPIPHANY, epiphany);
                values.put(Constants.STORY_CHARACTER_PERSONALITY, personality);
                values.put(Constants.STORY_CHARACTER_NOTES, notes);

                // Insert the rows
                db.insertRow(values, Constants.STORY_CHARACTER_TABLE);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}
