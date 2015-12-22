package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    private ListView add_characters_listview;
    private Context context = getActivity().getApplicationContext();

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_character_layout = inflater.inflate(R.layout.fragment_add_characters,
                container,
                false);
        add_characters_listview = (ListView) add_character_layout.findViewById(
                R.id.add_characters_list);

        // Instantiate the db and get the context
        SQLDatabase db = new SQLDatabase(context);

        // Create a Cursor object to hold the rows
        final Cursor cursor = db.getRows(Constants.GRAB_CHARACTER_DETAILS);

        // Get the column names
        String[] columns = new String[] {
                Constants.DB_ID,
                Constants.STORY_CHARACTER,
                Constants.STORY_AGE
        };

        // Get the ListView widget
        int[] widgets = new int[] {
                R.id.add_characters_list
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                context,
                R.layout.fragment_add_characters,
                cursor,
                columns,
                widgets,
                0);

        add_characters_listview.setAdapter(cursorAdapter);

        // Get the FAB for the story creation. Clicking on this FAB should bring
        // a small pop-up list with the ability to add a new character, plotline, or place
        FloatingActionButton the_character_fab = (FloatingActionButton)
                add_character_layout.findViewById(R.id.add_character_fab);
        the_character_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacterElements(v);
            }
        });

        // Return the layout
        return add_character_layout;
    }

    // Create pop-up box to start adding characters to the story
    // TODO -> Factor out pop-up windows into separate class
    public void addCharacterElements(View view) {
        // Set activity of pop-up box
        final PopupWindow popup = new PopupWindow(context);

        // Inflate the layout to use in this pop-up window
        final View layout = getActivity().getLayoutInflater().inflate(
                R.layout.activity_add_character,
                null);

        // Set view in the pop-up
        popup.setContentView(layout);

        // Set height and width for pop-up
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        // Set touch parameter and focusable -> both true
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        // Set location
        popup.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);

        Button add_the_character = (Button) layout.findViewById(R.id.add_the_character);
        add_the_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Now find the elements
                EditText name = (EditText) layout.findViewById(R.id.sb_character_name);
                EditText age = (EditText) layout.findViewById(R.id.sb_character_age);
                EditText birthplace = (EditText) layout.findViewById(R.id.sb_character_birthplace);
                EditText personality = (EditText) layout.findViewById(R.id.sb_character_personality);
                EditText character_notes = (EditText) layout.findViewById(R.id.sb_character_notes);

                // TODO -> Add a cancel button
                // Make sure the name field is not empty
                if ( name.length() < 1 ) {
                    Toast.makeText(context, "The character's name "
                            + "is a required field", Toast.LENGTH_LONG).show();
                }
                else {
                    // Send them to a background thread to process in the SBValues
                    addCharactersTask charactersTask = new addCharactersTask();
                    charactersTask.execute(name, age, birthplace, personality, character_notes);
                }
            }
        });

        // Dismiss the pop-up window
        popup.dismiss();
    }

    private class addCharactersTask extends AsyncTask<EditText, Void, Boolean> {
        private SBValues send = new SBValues();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(EditText... params) {
            Boolean completed = true;

            send.processValues(context, Constants.STORY_CHARACTER, params[0],
                    Constants.STORY_CHARACTER_TABLE);
            send.processValues(context, Constants.STORY_AGE, params[1],
                    Constants.STORY_CHARACTER_TABLE);
            send.processValues(context, Constants.STORY_BIRTHPLACE, params[2],
                    Constants.STORY_CHARACTER_TABLE);
            send.processValues(context, Constants.STORY_PERSONALITY, params[3],
                    Constants.STORY_CHARACTER_TABLE);
            send.processValues(context, Constants.STORY_CHARACTER_NOTES, params[4],
                    Constants.STORY_CHARACTER_TABLE);

            return completed;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
}
