package tahastudio.storybuilder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

/**
 * Cursor adapter to populate the AddCharacters listview
 */
public class AddCharacterCursorAdapter extends CursorAdapter {

    // Define the constructor of the superclass
    public AddCharacterCursorAdapter(Context context, Cursor cursor, int flag) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_add_characters, parent, false);
    }

    // Start binding the views to the textviews in the layout. The number of textviews should
    // equal the number of columsn we'll be returning from the database
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find the listview to populate
        ListView character_list = (ListView) view.findViewById(R.id.add_characters_list);

        // Attach the db columns
        String character_name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.STORY_CHARACTER));
    }
}
