package tahastudio.storybuilder;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // Get an instance of the SQLDatabase
    private SQLDatabase db;
    private ListView add_characters_listview;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        View add_character_layout = inflater.inflate(R.layout.fragment_add_characters, container,
                false);
        add_characters_listview = (ListView) add_character_layout.findViewById(R.id
                .add_characters_list);

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
                addStoryElements(v);
            }
        });

        return add_character_layout;
    }

    public void addStoryElements(View view) {

    }
}
