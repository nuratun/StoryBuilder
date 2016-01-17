package tahastudio.storybuilder.db;

/**
 * To store constants for SQL queries.
 * These constants will not change throughout the lifecycle
 */
public class Constants {

    // Version number must change if database changes
    public static final int DATABASE_VERSION = 22;
    public static final String DATABASE_NAME = "sql.db";

    // Set up table schema for the story
    public static final String DB_ID = "_id";
    public static final String DB_COMMA = ", ";
    public static final String STORY_TABLE = "sb_story";
    public static final String STORY_NAME = "title";
    public static final String STORY_GENRE = "genre";
    public static final String STORY_DESC = "desc";

    // Set up table schema for characters
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
    public static final String STORY_EVENT_TABLE = "sb_event";
    public static final String STORY_EVENT_LINER = "event_liner";
    public static final String STORY_EVENT_DESC = "event_desc";
    public static final String STORY_EVENT_CHARACTERS = "event_characters";
    public static final String STORY_EVENT_SUMMARY = "event_summary";
    public static final String STORY_EVENT_NOTES = "event_notes";

    // Set up table schema for locations
    public static final String STORY_LOCATION_TABLE = "sb_location";
    public static final String STORY_LOCATION_NAME = "location_name";
    public static final String STORY_LOCATION_LOCATION = "location_location";
    public static final String STORY_LOCATION_DESC = "location_description";
    public static final String STORY_LOCATION_IMPORTANCE = "location_importance";
    public static final String STORY_LOCATION_EVENTS = "location_events";
    public static final String STORY_LOCATION_NOTES = "location_notes";

    // Get latest story entry id from database
    public static final String GET_STORY_ID = "SELECT MAX(_id) FROM " + STORY_TABLE;

    // Get a story entry id from database, going by the title
    public static final String FIND_STORY_ID = "SELECT "
            + DB_ID + " FROM "
            + STORY_TABLE + " WHERE "
            + STORY_NAME + " = ?";

    // Create the story table. All other tables will reference the id and story name
    public static final String SQL_CREATE_STORY_TABLE = "CREATE TABLE "
            + STORY_TABLE + "("
            + DB_ID + " integer primary key autoincrement, "
            + STORY_NAME + " text, "
            + STORY_GENRE + " text, "
            + STORY_DESC + " text "
            + " )";

    // Create the character table, with foreign keys from the story table
    public static final String SQL_CREATE_CHARACTERS = "CREATE TABLE "
            + STORY_CHARACTER_TABLE + "("
            + STORY_CHARACTER_NAME + " text, "
            + STORY_CHARACTER_TYPE + " text, "
            + STORY_CHARACTER_GENDER + " text, "
            + STORY_CHARACTER_AGE + " integer, "
            + STORY_CHARACTER_BIRTHPLACE + " text, "
            + STORY_CHARACTER_HISTORY + " text, "
            + STORY_CHARACTER_GOALS + " text, "
            + STORY_CHARACTER_CONFLICTS + " text, "
            + STORY_CHARACTER_EPIPHANY + " text, "
            + STORY_CHARACTER_PERSONALITY + " text, "
            + STORY_CHARACTER_STORYLINE + " text, "
            + STORY_CHARACTER_NOTES + " text, "
            + DB_ID + " integer, "
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the event table, with foreign keys from the story table
    public static final String SQL_CREATE_EVENTS = "CREATE TABLE "
            + STORY_EVENT_TABLE + "("
            + STORY_EVENT_LINER + " text, "
            + STORY_EVENT_DESC + " text, "
            + STORY_EVENT_CHARACTERS + " text, "
            + STORY_EVENT_SUMMARY + " text, "
            + STORY_EVENT_NOTES + " text, "
            + DB_ID + " integer,"
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the locations table, with foreign keys from the story table
    public static final String SQL_CREATE_LOCATIONS = "CREATE TABLE "
            + STORY_LOCATION_TABLE + "("
            + STORY_LOCATION_NAME + " text, "
            + STORY_LOCATION_LOCATION + " text, "
            + STORY_LOCATION_DESC + " text, "
            + STORY_LOCATION_IMPORTANCE + " text,"
            + STORY_LOCATION_EVENTS + " text,"
            + STORY_LOCATION_NOTES + " text, "
            + DB_ID + " integer, "
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Used in StoryBuilderMain to populate the ListView
    public static final String GRAB_STORY_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_NAME + ", "
            + STORY_GENRE + ", "
            + STORY_DESC + " FROM "
            + STORY_TABLE + ";";

    // Used in AddCharacters class to populate the ListView
    public static final String GRAB_CHARACTER_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_CHARACTER_NAME + ", "
            + STORY_CHARACTER_AGE + " FROM "
            + STORY_CHARACTER_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in the AddLocations class to populate the ListView
    public static final String GRAB_LOCATION_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_LOCATION_NAME + ", "
            + STORY_LOCATION_LOCATION + " FROM "
            + STORY_LOCATION_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in the AddEvents class to populate the ListView
    public static final String GRAB_EVENT_DETALIS = "SELECT "
            + DB_ID + ", "
            + STORY_EVENT_LINER + ", "
            + STORY_EVENT_CHARACTERS + " FROM "
            + STORY_EVENT_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in UpdateElementTask to grab the row selected in AddCharacters
    public static final String GRAB_CHARACTER_ROW_DETAILS = "SELECT * FROM "
            + STORY_CHARACTER_TABLE + " WHERE "
            + STORY_CHARACTER_NAME + " = ";

    // Used in UpdateElementTask to grab the row selected in AddLocations
    public static final String GRAB_PLACE_ROW_DETAILS = "SELECT * FROM "
            + STORY_LOCATION_TABLE + " WHERE "
            + STORY_LOCATION_NAME + " = ";

    // Used in UpdateElementTask to grab the row selected in AddEvents
    public static final String GRAB_PLOT_ROW_DETAILS = "SELECT * FROM "
            + STORY_EVENT_TABLE + " WHERE "
            + STORY_EVENT_DESC + " = ";

    // Used in ShowCharacter, ShowLocation, ShowEvent, and ShowElementsTask
    public static final String CHARACTERS_TABLE = "sb_characters";
    public static final String LOCATIONS_TABLE = "sb_locations";
    public static final String EVENTS_TABLE = "sb_events";

    // Used in ShowStory
    public static final String CHARACTER_TYPE_PROTAGONIST = "protagonist";
    public static final String CHARACTER_TYPE_ANTAGONIST = "antagonist";
    public static final String CHARACTER_TYPE_OTHER = "other";

    public static final String CHARACTER_GENDER_MALE = "male";
    public static final String CHARACTER_GENDER_FEMALE = "female";
    public static final String CHARACTER_GENDER_OTHER = "other";

    public static final String GET_STORY_GENRE = "SELECT GENRE FROM "
            + STORY_TABLE + " WHERE "
            + DB_ID + " = ";

}