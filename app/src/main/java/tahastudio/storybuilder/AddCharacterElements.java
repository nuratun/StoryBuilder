package tahastudio.storybuilder;

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

/**
 * Fragment to replace ListView in AddCharacters. Is called by AddCharacters.
 */
public class AddCharacterElements extends Fragment {
    private Context context;

    public AddCharacterElements() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this view
        View character_elements_layout = inflater.inflate(
                R.layout.activity_add_character,
                container,
                false);

        // Get an instance of the application context
        context = getActivity().getApplicationContext();

        // Now find the elements
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

        // On button click, ensure that the required fields are filled out
        add_the_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> Add a cancel button
                // Make sure the name field is not empty
                if ( name.length() < 1 ) {
                    Toast.makeText(context, "The character's name "
                            + "is a required field", Toast.LENGTH_LONG).show();
                }
                else {
                    // Send them to a background thread to process in the SBValues
                    addCharactersTask charactersTask =
                            new addCharactersTask(
                                    context,
                                    name,
                                    age,
                                    birthplace,
                                    personality,
                                    character_notes);
                    charactersTask.execute();
                }
                // Return to AddCharacters class
                getActivity().getFragmentManager().popBackStack();
            }
        });

        // Return the view
        return character_elements_layout;
    }

    // Need to process more than one variable, so create a constructor in the class
    private class addCharactersTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        EditText name;
        EditText age;
        EditText birthplace;
        EditText personality;
        EditText notes;

        // Constructor to pass in more than one value
        public addCharactersTask(Context context,
                                 EditText name,
                                 EditText age,
                                 EditText birthplace,
                                 EditText personality,
                                 EditText notes) {
            this.context = context;
            this.name = name;
            this.age = age;
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
            SBValues send = new SBValues();

            try {
                send.processValues(context, Constants.STORY_CHARACTER, name,
                        Constants.STORY_CHARACTER_TABLE);
                send.processValues(context, Constants.STORY_AGE, age,
                        Constants.STORY_CHARACTER_TABLE);
                send.processValues(context, Constants.STORY_BIRTHPLACE, birthplace,
                        Constants.STORY_CHARACTER_TABLE);
                send.processValues(context, Constants.STORY_PERSONALITY, personality,
                        Constants.STORY_CHARACTER_TABLE);
                send.processValues(context, Constants.STORY_CHARACTER_NOTES, notes,
                        Constants.STORY_CHARACTER_TABLE);
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
