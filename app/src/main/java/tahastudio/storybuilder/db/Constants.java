package tahastudio.storybuilder.db;

/**
 * To store constants for SQL queries.
 * These constants will not change throughout the lifecycle
 */
public class Constants {

    // Version number must change if database changes
    public static final int DATABASE_VERSION = 14;
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
    public static final String STORY_CHARACTER_NAME = "name";
    public static final String STORY_CHARACTER_POSITION = "position";
    public static final String STORY_CHARACTER_GENDER = "gender";
    public static final String STORY_CHARACTER_AGE = "age";
    public static final String STORY_CHARACTER_BIRTHPLACE = "birthplace";
    public static final String STORY_CHARACTER_PERSONALITY = "personality";
    public static final String STORY_CHARACTER_NOTES = "notes";

    // Set up table schema for plots
    public static final String STORY_PLOT_TABLE = "sb_plot";
    public static final String STORY_MAIN_PLOT = "main_plot";
    public static final String STORY_PLOT_TITLE = "plot_title";
    public static final String STORY_PLOT_DESC = "sb_plot_desc";
    public static final String STORY_PLOT_NOTES = "plot_notes";

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
            + STORY_CHARACTER_NAME + " text, "
            + STORY_CHARACTER_POSITION + " tinyint(1), "
            + STORY_CHARACTER_GENDER + " text, "
            + STORY_CHARACTER_AGE + " integer, "
            + STORY_CHARACTER_BIRTHPLACE + " text, "
            + STORY_CHARACTER_PERSONALITY + " text, "
            + STORY_CHARACTER_NOTES + " text, "
            + DB_ID + " integer, "
            + STORY_NAME + " text, "
            + "FOREIGN KEY(" + DB_ID + DB_COMMA + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the plot table, with foreign keys from the story table
    public static final String SQL_CREATE_PLOT = "CREATE TABLE "
            + STORY_PLOT_TABLE + "("
            + STORY_MAIN_PLOT + " integer, "
            + STORY_PLOT_TITLE + " text, "
            + STORY_PLOT_DESC + " text, "
            + STORY_PLOT_NOTES + " text, "
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
            + STORY_CHARACTER_NAME + ", "
            + STORY_CHARACTER_AGE + " FROM "
            + STORY_CHARACTER_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in the AddPlaces class to populate the ListView
    public static final String GRAB_PLACES_DETAILS = "SELECT "
            + DB_ID + ", "
            + STORY_PLACE_NAME + ", "
            + STORY_PLACE_LOCATION + " FROM "
            + STORY_PLACES_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in the AddPlots class to populate the ListView
    public static final String GRAB_PLOT_DETALIS = "SELECT "
            + DB_ID + ", "
            + STORY_MAIN_PLOT + ", "
            + STORY_PLOT_TITLE + " FROM "
            + STORY_PLOT_TABLE + " WHERE "
            + DB_ID + " = ";

    // Used in UpdateElementTask to grab the row selected in AddCharacters
    public static final String GRAB_CHARACTER_ROW_DETAILS = "SELECT * FROM "
            + STORY_CHARACTER_TABLE + " WHERE "
            + STORY_CHARACTER_NAME + " = ";

    // Used in UpdateElementTask to grab the row selected in AddPlaces
    public static final String GRAB_PLACE_ROW_DETAILS = "SELECT * FROM "
            + STORY_PLACES_TABLE + " WHERE "
            + STORY_PLACE_NAME + " = ";

    // Used in UpdateElementTask to grab the row selected in AddPlots
    public static final String GRAB_PLOT_ROW_DETAILS = "SELECT * FROM "
            + STORY_PLOT_TABLE + " WHERE "
            + STORY_PLOT_TITLE + " = ";


}