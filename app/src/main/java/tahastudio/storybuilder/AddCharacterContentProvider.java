package tahastudio.storybuilder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.net.URI;

/**
 * Cursor adapter to populate the AddCharacters listview
 */
public class AddCharacterContentProvider extends ContentProvider {
    private SQLDatabase dbhelper;

    @Override
    public boolean onCreate() {
        dbhelper = new SQLDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query() {

    }

    @Override
    public String getType(Uri uri) {

    }

    @Override
    public URI insert() {

    }

    @Override
    public int delete() {

    }

    @Override
    public int update(URI uri, ContentValues values, String selection, String[] string_array) {
        return 0;
    }

}
