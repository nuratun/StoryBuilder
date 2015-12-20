package tahastudio.storybuilder;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // Get an instance of the SQLDatabase and the listview to populate
    private SQLDatabase db;
    private ListView add_characters_listview;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_character_layout = inflater.inflate(R.layout.fragment_add_characters, container,
                false);
        add_characters_listview = (ListView) add_character_layout.findViewById(R.id
                .add_characters_list);

        // TODO -> Create async task for background threads

        // Instantiate the db and get the context
        db = new SQLDatabase(getActivity().getApplicationContext());

        // Create a Cursor object to hold the rows
        final Cursor cursor = db.getRows(Constants.GRAB_CHARACTER_DETAILS);

        // Get the column names
        String[] columns = new String[] {
                Constants.DB_ID,
                Constants.STORY_CHARACTER,
                Constants.STORY_AGE
        };

        // Get the listview widget
        int[] widgets = new int[] {
                R.id.add_characters_list
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
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
    public void addCharacterElements(View view) {
        // Set activity of pop-up box
        final PopupWindow popup = new PopupWindow(getActivity().getApplicationContext());

        // Inflate the layout to use in this pop-up window
        final View layout = getActivity().getLayoutInflater().inflate(R.layout
                .activity_add_character,
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

        // Now find the elements
        final EditText name = (EditText) layout.findViewById(R.id.sb_character_name);
        final EditText age = (EditText) layout.findViewById(R.id.sb_character_age);
        final EditText birthplace = (EditText) layout.findViewById(R.id.sb_character_birthplace);
        final EditText personality = (EditText) layout.findViewById(R.id.sb_character_personality);
        final EditText character_notes = (EditText) layout.findViewById(R.id.sb_character_notes);
        Button add_the_character = (Button) layout.findViewById(R.id.add_the_character);

        // Get character's position in story: protagonist, antagonist, or neither
        final String pos = mCharacterCheckbox(layout);

        // Get character's gender or null
        final String gender = characterGender(layout);

        add_the_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert element entries to text
                String sb_name = name.getText().toString();
                String sb_age = age.getText().toString();
                String sb_birthplace = birthplace.getText().toString();
                String sb_personality = personality.getText().toString();
                String sb_character_notes = character_notes.getText().toString();

                // TODO -> Add a cancel button
                // Make sure the name field is not empty
                if ( sb_name.length() < 1 ) {
                    Toast.makeText(getActivity().getApplicationContext(), "The character's name "
                            + "is a required field", Toast.LENGTH_LONG).show();
                }

                else {
                    // Format values in their db fields
                    ContentValues values = new ContentValues();
                    values.put(Constants.STORY_CHARACTER, sb_name);
                    values.put(Constants.STORY_AGE, sb_age);
                    values.put(Constants.STORY_BIRTHPLACE, sb_birthplace);
                    values.put(Constants.STORY_PERSONALITY, sb_personality);
                    values.put(Constants.STORY_MAIN, pos);
                    values.put(Constants.STORY_GENDER, gender);
                    values.put(Constants.STORY_CHARACTER_NOTES, sb_character_notes);

                    // Initialize the db
                    db = new SQLDatabase(getActivity().getApplicationContext());

                    // Insert the rows
                    db.insertRow(values, Constants.STORY_CHARACTER_TABLE);

                    // Dismiss the pop-up window
                    popup.dismiss();
                }
            }
        });
    }

    // May be a variation -> main character & the antagonist, just the main character
    // or just the antagonist.
    public String mCharacterCheckbox(View view) {
        // Is a checkbox checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which one(s) were checked
        if ( checked ) {
            // If checked, initialize a string to hold the value
            String type = null;
            switch (view.getId()) {
                case R.id.main_character_checkbox:
                    type = "Protagonist";
                    break;
                case R.id.antagonist_character_checkbox:
                    type += "Antagonist";
                    break;
            }
            return type;
        }
        return null;
    }

    public String characterGender(View view) {
        // Is a radio button selected?
        boolean checked = ((RadioButton) view).isChecked();

        // Return which radio button was selected
        switch (view.getId()) {
            case R.id.male_gender:
                if (checked)
                    return "male";
                break;
            case R.id.female_gender:
                if (checked)
                    return "female";
                break;
            case R.id.other_gender:
                if (checked)
                    return "other";
                break;
        }
        return null;
    }
}
