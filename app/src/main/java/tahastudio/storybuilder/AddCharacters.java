package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * First tab for SB
 */
public class AddCharacters extends Fragment {
    // Get an instance of the SQLDatabase
    private SQLDatabase db = new SQLDatabase(getActivity());

    public AddCharacters() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        View add_character_layout = inflater.inflate(R.layout.fragment_add_characters, container, false);

        // Run the query to populate the listview
        db.getWritableDatabase();
        db.runQuery(Constants.GRAB_CHARACTER_NAME);

        return add_character_layout;
    }
}
