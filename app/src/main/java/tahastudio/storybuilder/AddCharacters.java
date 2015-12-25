package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    private ListView add_characters_listview;
    private Context context;

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View add_character_layout = inflater.inflate(
                R.layout.fragment_add_characters,
                container,
                false);
        add_characters_listview = (ListView) add_character_layout
                .findViewById(R.id.add_characters_list);

        // Get an instance of the application context
        context = getActivity().getApplicationContext();

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
                // To programmatically add in the AddCharacterElements fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                // Need to get APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction
                        .add(R.id.add_characters_tab, new AddCharacterElements())
                        .addToBackStack("add_the_character")
                        .commit();
            }
        });

        // Return the layout
        return add_character_layout;
    }
}
