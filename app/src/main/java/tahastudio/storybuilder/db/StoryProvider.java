package tahastudio.storybuilder.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * ContentProvider for story elements. All other activities/fragments will now call
 * this content provider, instead of the SQLDatabase directly.
 */
public class StoryProvider extends ContentProvider {
    // Get an instance of the database
    private SQLDatabase db = null;
    private ContentResolver contentResolver;


    @Override
    public String getType(Uri uri) {
        switch (Constants.uriMatcher.match(uri)) {
            case (Constants.CHARACTER_LIST):
                return Constants.CONTENT_URI + "/" + Constants.STORY_CHARACTER_TABLE;
            case (Constants.CHARACTER_ID):
                return Constants.CONTENT_URI + "/" + Constants.STORY_CHARACTER_TABLE
                        + "/" + Constants.DB_ID;
            case (Constants.LOCATION_LIST):
                return Constants.CONTENT_URI + "/" + Constants.STORY_LOCATION_TABLE;
            case (Constants.LOCATION_ID):
                return Constants.CONTENT_URI + "/" + Constants.STORY_LOCATION_TABLE
                        + "/" + Constants.DB_ID;
            case (Constants.EVENT_LIST):
                return Constants.CONTENT_URI + "/" + Constants.STORY_EVENT_TABLE;
            case (Constants.EVENT_ID):
                return Constants.CONTENT_URI + "/" + Constants.STORY_EVENT_ID
                        + "/" + Constants.DB_ID;
        }
        return "tahastudio.storybuilder.db.SQLDatabase";
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        db = new SQLDatabase(context);
        contentResolver = context.getContentResolver(); // This to notify of db changes

        return true;
    }

    // Called from: AddCharacters, AddEvents, AddLocations
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        String id = null; // This will set the table in the db

        Log.d("the_uri", String.valueOf(uri));

        int uriType = Constants.uriMatcher.match(uri);

        switch (uriType) {
            case Constants.CHARACTER_LIST:
                id = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.CHARACTER_ID:
                id = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.LOCATION_LIST:
                id = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.LOCATION_ID:
                id = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.EVENT_LIST:
                id = Constants.STORY_EVENT_TABLE;
                break;
            case Constants.EVENT_ID:
                id = Constants.STORY_EVENT_TABLE;
                break;
        }

        Cursor cursor = db.getSearchResults(id, projection, selection, selectionArgs, sortOrder);

        // Call the ContentResolver on the cursor
        cursor.setNotificationUri(contentResolver, uri);

        return cursor;
    }

    // Uri will be the hardcoded table from AddCharacters, AddEvents, and AddLocations
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = db.addEntry(uri, values);

            // Notify ContentResolver of db change
            contentResolver.notifyChange(uri, null);

            return ContentUris.withAppendedId(Constants.CONTENT_URI, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = null; // The table to delete from

        int uriType = Constants.uriMatcher.match(uri); // Find the table by the uri
        Log.d("story", String.valueOf(uri));

        switch (uriType) {
            case Constants.CHARACTER_ID:
                table = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.CHARACTER_LIST:
                table = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.LOCATION_ID:
                table = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.LOCATION_LIST:
                table = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.EVENT_ID:
                table = Constants.STORY_EVENT_TABLE;
                break;
            case Constants.EVENT_LIST:
                table = Constants.STORY_EVENT_TABLE;
                break;
            case Constants.STORY_ID:
                table = Constants.STORY_TABLE;
                break;
            case Constants.STORY_LIST:
                table = Constants.STORY_TABLE;
                break;
        }

        // Notify ContentResolver of db change
        contentResolver.notifyChange(uri, null);

        return db.deleteEntry(table, selection, selectionArgs);
    }

    // Called from: ShowCharacter, ShowEvent, ShowLocation
    // String selection is database row id
    // selectionArgs is the id grabbed from the onClick method
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;

        int uriType = Constants.uriMatcher.match(uri);

        switch (uriType) {
            case Constants.CHARACTER_LIST:
                id = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.CHARACTER_ID:
                id = Constants.STORY_CHARACTER_TABLE;
                break;
            case Constants.LOCATION_LIST:
                id = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.LOCATION_ID:
                id = Constants.STORY_LOCATION_TABLE;
                break;
            case Constants.EVENT_LIST:
                id = Constants.STORY_EVENT_TABLE;
                break;
            case Constants.EVENT_ID:
                id = Constants.STORY_EVENT_TABLE;
                break;
        }

        // Notify ContentResolver of db change
        contentResolver.notifyChange(uri, null);

        return db.updateEntry(id, values, selection, selectionArgs);
    }
}
