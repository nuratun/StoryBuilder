package tahastudio.storybuilder.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
    private ContentResolver contentResolver;

    @Override
    public String getType(Uri uri) {
        switch (Constants.uriMatcher.match(uri)) {
            case (Constants.STORY_LIST):
                return Constants.CONTENT_URI + "/" + Constants.STORY_TABLE;
            case (Constants.STORY_ID):
                return Constants.CONTENT_URI + "/" + Constants.STORY_TABLE
                        + "/" + Constants.DB_ID;
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
            case (Constants.PLOT_LIST):
                return Constants.CONTENT_URI + "/" + Constants.STORY_PLOT_TABLE;
            case (Constants.PLOT_ID):
                return Constants.CONTENT_URI + "/" + Constants.STORY_PLOT_ID
                        + "/" + Constants.DB_ID;
        }
        return Constants.AUTHORITY; // Return the database
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        db = new SQLDatabase(context);
        contentResolver = context.getContentResolver(); // This to notify of db changes

        return true;
    }

    // Called from: StoryBuilderMain, AddCharacters, AddEvents, AddLocations
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        int uriType = Constants.uriMatcher.match(uri);

        // Call the FindTable class to use its switch statement
        FindTable getTable = new FindTable();
        String id = getTable.findTheTable(uriType); // Get the table by the uri code

        Cursor cursor = db.getSearchResults(id, projection, selection, selectionArgs, sortOrder);

        // Call the ContentResolver on the cursor
        cursor.setNotificationUri(contentResolver, uri);

        return cursor;
    }

    // Uri will be the hardcoded table from CreateStoryTask, AddCharacters, AddEvents,
    // and AddLocations
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

        int uriType = Constants.uriMatcher.match(uri); // Find the table by the uri

        // Call the FindTable class to use its switch statement
        FindTable getTable = new FindTable();
        String table = getTable.findTheTable(uriType); // Get the table by the uri code

        // Notify ContentResolver of db change
        contentResolver.notifyChange(uri, null);

        return db.deleteEntry(table, selection, selectionArgs);
    }

    // Called from: ShowCharacter, ShowEvent, ShowLocation
    // String selection is database row id
    // selectionArgs is the id grabbed from the onClick method
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = Constants.uriMatcher.match(uri);

        // Call the FindTable class to use its switch statement
        FindTable getTable = new FindTable();
        String id = getTable.findTheTable(uriType); // Get the table by the uri code

        // Notify ContentResolver of db change
        contentResolver.notifyChange(uri, null);

        return db.updateEntry(id, values, selection, selectionArgs);
    }
}