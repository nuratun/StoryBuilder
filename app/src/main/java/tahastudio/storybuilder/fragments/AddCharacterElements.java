package tahastudio.storybuilder.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.ShowStory;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;

/**
 * Fragment to replace ListView in AddCharacters. Called by AddCharacters.
 */
public class AddCharacterElements extends Fragment {

    public AddCharacterElements() { }

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
        final EditText personality = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_personality);
        final EditText character_notes = (EditText) character_elements_layout
                .findViewById(R.id.sb_character_notes);

        Button add_the_character = (Button) character_elements_layout
                .findViewById(R.id.add_the_character);
        add_the_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure the name field != null
                if ( name.length() < 1 ) {
                    Toast.makeText(getContext(), "The character's name "
                            + "is a required field", Toast.LENGTH_LONG).show();
                }
                else {
                    // Send converted strings to a background thread to process in the db
                    addCharactersTask charactersTask =
                            new addCharactersTask(
                                    getContext(),
                                    name.getText().toString(),
                                    age.getText().toString(),
                                    ShowStory.CHARACTER_TYPE, // Methods for these public strings
                                    ShowStory.CHARACTER_GENDER, // are in the ShowStory class
                                    birthplace.getText().toString(),
                                    personality.getText().toString(),
                                    character_notes.getText().toString());
                    charactersTask.execute();
                }
                // Return to AddCharacters class on button click, immediately
                getFragmentManager().popBackStackImmediate();
            }
        });

        Button cancel = (Button) character_elements_layout
                .findViewById(R.id.character_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to AddCharacters, immediately.
                getFragmentManager().popBackStackImmediate();
            }
        });

        return character_elements_layout;
    }

    // Pass multiple variables to the constructor
    private class addCharactersTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private ContentValues values;
        private SQLDatabase db;
        private String name;
        private String age;
        private String type;
        private String gender;
        private String birthplace;
        private String personality;
        private String notes;

        // Constructor
        public addCharactersTask(Context context,
                                 String name,
                                 String age,
                                 String type,
                                 String gender,
                                 String birthplace,
                                 String personality,
                                 String notes) {
            this.context = context;
            this.name = name;
            this.age = age;
            this.type = type;
            this.gender = gender;
            this.birthplace = birthplace;
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
            db = new SQLDatabase(context);
            values = new ContentValues();

            try {
                values.put(Constants.DB_ID, ShowStory.SB_ID);
                values.put(Constants.STORY_CHARACTER_NAME, name);
                values.put(Constants.STORY_CHARACTER_AGE, age);
                values.put(Constants.STORY_CHARACTER_TYPE, type);
                values.put(Constants.STORY_CHARACTER_GENDER, gender);
                values.put(Constants.STORY_CHARACTER_BIRTHPLACE, birthplace);
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
