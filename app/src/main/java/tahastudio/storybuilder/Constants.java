package tahastudio.storybuilder;

/**
 * To store constants for SQL
 */
public class Constants {

    // Version number must change if database changes
    public static final int DATABASE_VERSION = 12;
    public static final String DATABASE_NAME = "sql.db";

    // Set up table schema for the story
    public static final String DB_ID = "_id";
    public static final String DB_COMMA = ", ";
    public static final String STORY_TABLE = "sb_story";
    public static final String STORY_NAME = "title";
    public static final String STORY_GENRE = "genre";
    public static final String STORY_NOTES = "notes";

    // Set up table schema for characters
    public static final String STORY_CHARACTER_TABLE = "sb_characters";
    public static final String STORY_CHARACTER = "name";
    public static final String STORY_MAIN = "main";
    public static final String STORY_GENDER = "gender";
    public static final String STORY_AGE = "age";
    public static final String STORY_BIRTHPLACE = "birthplace";
    public static final String STORY_PERSONALITY = "personality";
    public static final String STORY_CHARACTER_NOTES = "notes";

    // Set up table schema for plotline
    public static final String STORY_PLOTLINE_TABLE = "sb_plotline";
    public static final String STORY_MAIN_PLOTLINE = "main_plotline";
    public static final String STORY_PLOTLINE = "sb_plotline_desc";
    public static final String STORY_PLOTLINE_NOTES = "plotline_notes";

    // Set up table schema for places
    public static final String STORY_PLACES_TABLE = "sb_places";
    public static final String STORY_PLACE_NAME = "place_name";
    public static final String STORY_PLACE_LOCATION = "place_location";
    public static final String STORY_PLACE_DESC = "place_description";
    public static final String STORY_PLACE_NOTES = "place_notes";

    // Get latest story entry id from database
    public static final String GET_STORY_ID = "SELECT MAX(_id) FROM " + STORY_TABLE;

    // Get a story entry id from database, going by the title
    public static final String FIND_STORY_ID = "SELECT "
            + DB_ID + " FROM "
            + STORY_TABLE + " WHERE "
            + STORY_NAME + " = ";

    // Create the story table. All other tables will reference the id and story name
    public static final String SQL_CREATE_STORY_TABLE = "CREATE TABLE "
            + STORY_TABLE + "("
            + DB_ID + " integer primary key autoincrement, "
            + STORY_NAME + " text, "
            + STORY_GENRE + " text, "
            + STORY_NOTES + " text "
            + " )";

    // Create the character table, with foreign keys from the story table
    public static final String SQL_CREATE_CHARACTERS = "CREATE TABLE "
            + STORY_CHARACTER_TABLE + "("
            + STORY_CHARACTER + " text, "
            + STORY_MAIN + " tinyint(1), "
            + STORY_GENDER + " text, "
            + STORY_AGE + " integer, "
            + STORY_BIRTHPLACE + " text, "
            + STORY_PERSONALITY + " text, "
            + STORY_CHARACTER_NOTES + " text, "
            + DB_ID + " integer, "
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the plotline table, with foreign keys from the story table
    public static final String SQL_CREATE_PLOTLINE = "CREATE TABLE "
            + STORY_PLOTLINE_TABLE + "("
            + STORY_MAIN_PLOTLINE + " text, "
            + STORY_PLOTLINE + " text, "
            + STORY_PLOTLINE_NOTES + " text, "
            + DB_ID + " integer,"
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the places table, with foreign keys from the story table
    public static final String SQL_CREATE_PLACES = "CREATE TABLE "
            + STORY_PLACES_TABLE + "("
            + STORY_PLACE_NAME + " text, "
            + STORY_PLACE_LOCATION + " text, "
            + STORY_PLACE_DESC + " text, "
            + STORY_PLACE_NOTES + " text, "
            + DB_ID + " integer, "
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Used in StoryBuilderMain to populate the ListView
    public static final String GRAB_STORY_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_NAME + ", "
            + STORY_GENRE + " FROM "
            + STORY_TABLE + ";";

    // Used in AddCharacters class to populate the ListView
    public static final String GRAB_CHARACTER_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_CHARACTER + ", "
            + STORY_AGE + " FROM "
            + STORY_CHARACTER_TABLE + " WHERE "
            + DB_ID + " = "
            + CreateStory.SB_ID + ";";

    // Used in the AddPlaces class to populate the ListView
    public static final String GRAB_PLACES_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_PLACE_NAME + ", "
            + STORY_PLACE_LOCATION + " FROM "
            + STORY_PLACES_TABLE + " WHERE "
            + DB_ID + " = "
            + CreateStory.SB_ID + ";";

    // Used in the AddPlotline class to populate the ListView
    public static final String GRAB_PLOTLINE_DETALIS = "SELECT "
            + DB_ID + ", "
            + STORY_MAIN_PLOTLINE + ", "
            + STORY_PLOTLINE + " FROM "
            + STORY_PLOTLINE_TABLE + " WHERE "
            + DB_ID + " = "
            + CreateStory.SB_ID + ";";
}