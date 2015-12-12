package tahastudio.storybuilder;

/**
 * To store constants for SQL
 */
public class Constants {

    // Version number must change if schema changes
    public static final double DATABASE_VERSION = 1.0;
    public static final String DATABASE_NAME = "sql.db";

    // Set up database schema for the story
    public static final String DB_ID = "_id";
    public static final String DB_NAME = "sb_story";

    // Set up table for story
    public static final String STORY_NAME = "title";
    public static final String STORY_GENRE = "genre";
    public static final String STORY_NOTES = "notes";

    // Set up table for characters
    public static final String STORY_CHARACTER = "name";
    public static final String STORY_MAIN = "main";
    public static final String STORY_GENDER = "gender";
    public static final String STORY_AGE = "age";
    public static final String STORY_BIRTHPLACE = "birthplace";
    public static final String STORY_PERSONALITY = "personality";
    public static final String STORY_CHARACTER_NOTES = "notes";

    // Set up table for plotline
    public static final String STORY_MAIN_PLOTLINE = "main_plotline";
    public static final String STORY_SECONDARY_PLOTLINE = "sec_plotline";
    public static final String STORY_THIRD_PLOTLINE = "third_plotline";

    // Set up table for places
    public static final String STORY_MAIN_PLACE = "main_place";
    public static final String STORY_MAIN_PLACE_DESC = "main_place_description";
    public static final String STORY_SEC_PLACE = "sec_place";
    public static final String STORY_SEC_PLACE_DESC = "sec_place_desc";
    public static final String STORY_PLACE_NOTES = "place_notes";

    // Time entries
    public static final String MODIFIED_TIME = "MODIFIED_TIME";
    public static final String CRTEATED_TIME = "CREATED_TIME";

    // Create the actual database entries
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + DB_NAME
            + "("
            + DB_ID
            + "Integer Primary Key, "
            + STORY_NAME
            + "text not null, "
            + STORY_GENRE
            + "text not null, "
            + STORY_NOTES
            + "text)";

}
