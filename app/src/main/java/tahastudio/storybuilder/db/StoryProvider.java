package tahastudio.storybuilder.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * ContentProvider for story elements. All other activities/fragments will now call
 * this content provider, instead of the SQLDatabase directly.
 */
public class StoryProvider extends ContentProvider {
    // Get an instance of the database
    private SQLDatabase db = null;

    @Override
    public String getType(Uri uri) {
        switch (Constants.uriMatcher.match(uri)) {
            case Constants.ROW_ID:
                return "tahastudio.storybuilder.db.SQLDatabase";
            case Constants.ROW_DETAILS:
                return "tahastudio.storybuilder.db.SQLDatabase";
        }
        return "";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        db = new SQLDatabase(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        String id = null;

        if ( Constants.uriMatcher.match(uri) == Constants.ROW_ID ) {
            id = uri.getPathSegments().get(1);
        }
        return db.getSearchResults(id, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = db.addEntry(values);
            return ContentUris.withAppendedId(Constants.CONTENT_URI, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if ( Constants.uriMatcher.match(uri) == Constants.ROW_ID ) {
            // Deletion is for a row. Get that row.
            id = uri.getPathSegments().get(1);
        }
        return db.deleteEntry(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if ( Constants.uriMatcher.match(uri) == Constants.ROW_ID ) {
            // Update is for a row. Get that row.
            id = uri.getPathSegments().get(1);
        }
        return db.updateEntry(id, values);
    }
}
