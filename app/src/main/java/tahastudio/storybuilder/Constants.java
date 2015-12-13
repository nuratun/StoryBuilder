package tahastudio.storybuilder;

/**
 * To store constants for SQL
 */
public class Constants {

    // Version number must change if schema changes
    public static final double DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "sql.db";

    // Set up database schema for the story
    public static final String DB_ID = "id";
    public static final String DB_NAME = "sb_db";
    public static final String DB_COMMA = ", ";

    // Set up table for story
    public static final String STORY_TABLE = "sb_story";
    public static final String STORY_NAME = "title";
    public static final String STORY_GENRE = "genre";
    public static final String STORY_NOTES = "notes";

    // Set up table for characters
    public static final String STORY_CHARACTER_TABLE = "sb_characters";
    public static final String STORY_CHARACTER = "name";
    public static final String STORY_MAIN = "main";
    public static final String STORY_GENDER = "gender";
    public static final String STORY_AGE = "age";
    public static final String STORY_BIRTHPLACE = "birthplace";
    public static final String STORY_PERSONALITY = "personality";
    public static final String STORY_CHARACTER_NOTES = "notes";

    // Set up table for plotline
    public static final String STORY_PLOTLINE_TABLE = "sb_plotline";
    public static final String STORY_MAIN_PLOTLINE = "main_plotline";
    public static final String STORY_SECONDARY_PLOTLINE = "sec_plotline";
    public static final String STORY_THIRD_PLOTLINE = "third_plotline";

    // Set up table for places
    public static final String STORY_PLACES_TABLE = "sb_places";
    public static final String STORY_MAIN_PLACE = "main_place";
    public static final String STORY_MAIN_PLACE_DESC = "main_place_description";
    public static final String STORY_SEC_PLACE = "sec_place";
    public static final String STORY_SEC_PLACE_DESC = "sec_place_desc";
    public static final String STORY_PLACE_NOTES = "place_notes";

    // Create the database
    public static final String SQL_CREATE_DB = "CREATE DATABASE " + DB_NAME;

    // Create the story table
    public static final String SQL_CREATE_STORY_TABLE = "CREATE TABLE "
            + STORY_TABLE + "("
            + DB_ID + " int(8) not null, "
            + STORY_NAME + " text not null, "
            + STORY_GENRE + " text not null, "
            + STORY_NOTES + " text, "
            + "PRIMARY KEY(" + DB_ID + ")"
            + " )";

    // Create the character table, with foreign keys from the story table
    public static final String SQL_CREATE_CHARACTERS = "CREATE TABLE "
            + STORY_CHARACTER_TABLE + "("
            + STORY_CHARACTER + " text not null, "
            + STORY_MAIN + " tinyint(1) not null, "
            + STORY_GENDER + " text not null, "
            + STORY_AGE + " int(8), "
            + STORY_BIRTHPLACE + " text not null, "
            + STORY_PERSONALITY + " text, "
            + STORY_CHARACTER_NOTES + " text, "
            + "FOREIGN KEY("
            + STORY_TABLE + "_" + DB_ID + DB_COMMA + STORY_TABLE + "_" + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create the plotline table
    public static final String SQL_CREATE_PLOTLINE = "CREATE TABLE "
            + STORY_PLOTLINE_TABLE + "("
            + STORY_MAIN_PLOTLINE + " text, "
            + STORY_SECONDARY_PLOTLINE + " text, "
            + STORY_THIRD_PLOTLINE + " text, "
            + "FOREIGN KEY("
            + STORY_TABLE + "_" + DB_ID + DB_COMMA + STORY_TABLE + "_" + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";

    // Create teh places table
    public static final String SQL_CREATE_PLACES = "CREATE TABLE "
            + STORY_PLACES_TABLE + "("
            + STORY_MAIN_PLACE + " text, "
            + STORY_MAIN_PLACE_DESC + " text, "
            + STORY_SEC_PLACE + " text, "
            + STORY_SEC_PLACE_DESC + " text, "
            + STORY_PLACE_NOTES + " text, "
            + "FOREIGN KEY("
            + STORY_TABLE + "_" + DB_ID + DB_COMMA + STORY_TABLE + "_" + STORY_NAME + ") "
            + "REFERENCES " + STORY_TABLE + "(" + DB_ID + DB_COMMA + STORY_NAME + ")"
            + " )";
}
