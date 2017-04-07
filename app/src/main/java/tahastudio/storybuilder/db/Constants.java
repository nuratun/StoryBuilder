package tahastudio.storybuilder.db;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * To store constants for SQL queries.
 * Most of these constants will not change throughout the lifecycle
 */
public class Constants {

    // For Google Drive backup
    public static final String EXISTING_FOLDER_ID = null;
    public static final String EXISTING_FILE_ID = null;
    public static final int REQUEST_CODE_RESOLUTION = 1;
    public static final int NEXT_AVAILABLE_REQUEST_CODE = 2;

    // For the SimpleItemTouchHelperCallback helper
    public static final float ALPHA_FULL = 1.0f;

    // The Loader instance for StoryBuilderMain
    public static final int LOADER = 0;

    // Create public static references for the story and table, so other classes can access them
    public static int SB_ID; // This value will not change unless a user selects a different story
    public static String SB_TABLE;

    // Version number must change if database changes
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "sb.db";

    // Columns used by all tables
    public static final String ID = "id"; // Migrate all id's (except story) to this. Version 4 -> 5
    public static final String ARCHIVE = "archive";
    public static final int ARCHIVED = 1;

    // Set up table schema for the story
    public static final String DB_ID = "_id";
    public static final String STORY_TABLE = "sb_story";
    public static final String STORY_NAME = "title";
    public static final String STORY_GENRE = "genre";
    public static final String STORY_DESC = "desc";

    // Set up table schema for characters
    public static final String STORY_CHARACTER_ID = "sb_characters_id"; // TODO -> Will delete after migration
    public static final String STORY_CHARACTER_TABLE = "sb_characters";
    public static final String STORY_CHARACTER_NAME = "character_name";
    public static final String STORY_CHARACTER_TYPE = "character_type";
    public static final String STORY_CHARACTER_GENDER = "character_gender";
    public static final String STORY_CHARACTER_AGE = "character_age";
    public static final String STORY_CHARACTER_BIRTHPLACE = "character_birthplace";
    public static final String STORY_CHARACTER_HISTORY = "character_history";
    public static final String STORY_CHARACTER_GOALS = "character_goals";
    public static final String STORY_CHARACTER_CONFLICTS = "character_conflicts";
    public static final String STORY_CHARACTER_EPIPHANY = "character_epiphany";
    public static final String STORY_CHARACTER_PERSONALITY = "character_personality";
    public static final String STORY_CHARACTER_STORYLINE = "character_storyline";
    public static final String STORY_CHARACTER_NOTES = "character_notes";

    // Set up table schema for events
    public static final String STORY_EVENT_ID = "sb_events_id"; // TODO -> Will delete after migration
    public static final String STORY_EVENT_TABLE = "sb_events";
    public static final String STORY_EVENT_LINER = "event_liner";
    public static final String STORY_EVENT_DESC = "event_desc";
    public static final String STORY_EVENT_CHARACTERS = "event_characters";
    public static final String STORY_EVENT_SUMMARY = "event_summary";
    public static final String STORY_EVENT_NOTES = "event_notes";

    // Set up table schema for locations
    public static final String STORY_LOCATION_ID = "sb_locations_id"; // TODO -> Will delete after migration
    public static final String STORY_LOCATION_TABLE = "sb_locations";
    public static final String STORY_LOCATION_NAME = "location_name";
    public static final String STORY_LOCATION_LOCATION = "location_location";
    public static final String STORY_LOCATION_DESC = "location_description";
    public static final String STORY_LOCATION_IMPORTANCE = "location_importance";
    public static final String STORY_LOCATION_EVENTS = "location_events";
    public static final String STORY_LOCATION_NOTES = "location_notes";

    // Set up table schema for plots
    public static final String STORY_PLOT_ID = "sb_plots_id"; // TODO -> Will delete after migration
    public static final String STORY_PLOT_TABLE = "sb_plots";
    public static final String STORY_PLOT_TITLE = "plot_name";
    public static final String STORY_PLOT_MAIN = "plot_main_sub";
    public static final String STORY_PLOT_DESC = "plot_description";
    public static final String STORY_PLOT_MISC = "plot_misc";
    public static final String STORY_PLOT_NOTES = "plot_notes";

    // The below if for the content provider class, StoryProvider
    // Since this content provider will not be accessible by outside apps,
    // the provider will be the app itself
    public static final String AUTHORITY = "tahastudio.storybuilder.db.SQLDatabase";
    public static final String PROVIDER_NAME = "tahastudio.storybuilder.db.StoryProvider";
    public static final int STORY_ID = 1;
    public static final int STORY_LIST = 2;
    public static final int CHARACTER_ID = 3;
    public static final int CHARACTER_LIST = 4;
    public static final int LOCATION_ID = 5;
    public static final int LOCATION_LIST = 6;
    public static final int EVENT_ID = 7;
    public static final int EVENT_LIST = 8;
    public static final int PLOT_ID = 9;
    public static final int PLOT_LIST = 10;
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME);
    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, STORY_TABLE + "/#", STORY_ID);
        uriMatcher.addURI(PROVIDER_NAME, STORY_TABLE, STORY_LIST);
        uriMatcher.addURI(PROVIDER_NAME, STORY_CHARACTER_TABLE + "/#", CHARACTER_ID);
        uriMatcher.addURI(PROVIDER_NAME, STORY_CHARACTER_TABLE, CHARACTER_LIST);
        uriMatcher.addURI(PROVIDER_NAME, STORY_LOCATION_TABLE + "/#", LOCATION_ID);
        uriMatcher.addURI(PROVIDER_NAME, STORY_LOCATION_TABLE, LOCATION_LIST);
        uriMatcher.addURI(PROVIDER_NAME, STORY_EVENT_TABLE + "/#", EVENT_ID);
        uriMatcher.addURI(PROVIDER_NAME, STORY_EVENT_TABLE, EVENT_LIST);
        uriMatcher.addURI(PROVIDER_NAME, STORY_PLOT_TABLE + "/#", PLOT_ID);
        uriMatcher.addURI(PROVIDER_NAME, STORY_PLOT_TABLE, PLOT_LIST);
    }

    // Get latest story entry id from database
    public static final String GET_STORY_ID = "SELECT MAX(_id) FROM " + STORY_TABLE;

    // Create the story table. All other tables will reference the id and story name
    public static final String SQL_CREATE_STORY_TABLE = "CREATE TABLE IF NOT EXISTS "
            + STORY_TABLE + "("
            + DB_ID + " integer primary key autoincrement, "
            + STORY_NAME + " text, "
            + STORY_GENRE + " text, "
            + STORY_DESC + " text, "
            + ARCHIVE + " integer)";

    // Create the character table, with foreign keys from the story table
    public static final String SQL_CREATE_CHARACTERS = "CREATE TABLE IF NOT EXISTS "
            + STORY_CHARACTER_TABLE + "("
            + ID + " integer primary key autoincrement, "
            + STORY_CHARACTER_NAME + " text, "
            + STORY_CHARACTER_TYPE + " integer, "
            + STORY_CHARACTER_GENDER + " integer, "
            + STORY_CHARACTER_AGE + " integer, "
            + STORY_CHARACTER_BIRTHPLACE + " text, "
            + STORY_CHARACTER_HISTORY + " text, "
            + STORY_CHARACTER_GOALS + " text, "
            + STORY_CHARACTER_CONFLICTS + " text, "
            + STORY_CHARACTER_EPIPHANY + " text, "
            + STORY_CHARACTER_PERSONALITY + " text, "
            + STORY_CHARACTER_STORYLINE + " text, "
            + STORY_CHARACTER_NOTES + " text, "
            + ARCHIVE + " integer, "
            + DB_ID + " integer, "
            + "FOREIGN KEY(" + DB_ID + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + ") ON DELETE CASCADE"
            + " );";

    // Create the event table, with foreign keys from the story table
    public static final String SQL_CREATE_EVENTS = "CREATE TABLE IF NOT EXISTS "
            + STORY_EVENT_TABLE + "("
            + ID + " integer primary key autoincrement, "
            + STORY_EVENT_LINER + " text, "
            + STORY_EVENT_DESC + " text, "
            + STORY_EVENT_CHARACTERS + " text, "
            + STORY_EVENT_SUMMARY + " text, "
            + STORY_EVENT_NOTES + " text, "
            + ARCHIVE + " integer, "
            + DB_ID + " integer,"
            + "FOREIGN KEY(" + DB_ID + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + ") ON DELETE CASCADE"
            + " );";

    // Create the locations table, with foreign keys from the story table
    public static final String SQL_CREATE_LOCATIONS = "CREATE TABLE IF NOT EXISTS "
            + STORY_LOCATION_TABLE + "("
            + ID + " integer primary key autoincrement, "
            + STORY_LOCATION_NAME + " text, "
            + STORY_LOCATION_LOCATION + " text, "
            + STORY_LOCATION_DESC + " text, "
            + STORY_LOCATION_IMPORTANCE + " text,"
            + STORY_LOCATION_EVENTS + " text,"
            + STORY_LOCATION_NOTES + " text, "
            + ARCHIVE + " integer, "
            + DB_ID + " integer, "
            + "FOREIGN KEY(" + DB_ID + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + ") ON DELETE CASCADE"
            + " );";

    // Create the plots table, with foreign keys from the story table
    public static final String SQL_CREATE_PLOTS = "CREATE TABLE IF NOT EXISTS "
            + STORY_PLOT_TABLE + "("
            + ID + " integer primary key autoincrement, "
            + STORY_PLOT_TITLE + " text, "
            + STORY_PLOT_MAIN + " integer, "
            + STORY_PLOT_MISC + " text, "
            + STORY_PLOT_DESC + " text, "
            + STORY_PLOT_NOTES + " text, "
            + ARCHIVE + " integer, "
            + DB_ID + " integer, "
            + "FOREIGN KEY(" + DB_ID + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + ") ON DELETE CASCADE"
            + " );";

    // The below is used in the SQLDatabase getElementRows method
    public static final String GRAB_CHARACTER_ROW_DETAILS = "SELECT * FROM "
            + STORY_CHARACTER_TABLE + " WHERE " + ID + " = ";

    // The below is used in the SQLDatabase getElementRows method
    public static final String GRAB_LOCATION_ROW_DETAILS = "SELECT * FROM "
            + STORY_LOCATION_TABLE + " WHERE " + ID + " = ";

    // The below is used in the SQLDatabase getElementRows method
    public static final String GRAB_EVENT_ROW_DETAILS = "SELECT * FROM "
            + STORY_EVENT_TABLE + " WHERE " + ID + " = ";

    // The below is used in the SQLDatabase getElementRows method
    public static final String GRAB_PLOT_ROW_DETAILS = "SELECT * FROM "
            + STORY_PLOT_TABLE + " WHERE " + ID + " = ";

    public static final String GET_STORY_GENRE = "SELECT " + STORY_GENRE + " FROM "
            + STORY_TABLE + " WHERE " + DB_ID + " = ";

    public static final String ARCHIVE_ELEMENT = "UPDATE " + SB_TABLE + " SET "
            + ARCHIVE + " = " + ARCHIVED + " WHERE " + ID + " = ";
}