package tahastudio.storybuilder;

/**
 * To store constants for SQL
 */
public class Constants {

    // Version number must change if schema changes
    public static final double DATABASE_VERSION = 1.0;
    public static final String DATABASE_NAME = "sql.db";

    // Set up database schema
    public static final String DB_ID = "_id";
    public static final String DB_NAME = "tahastudio_SB";
    public static final String COLUMN_NAME = "NAME";
    public static final String TEXT_TYPE = "TEXT";
    public static final String COMMA_TYPE = ",";

    // Time entries
    public static final String MODIFIED_TIME = "MODIFIED_TIME";
    public static final String CRTEATED_TIME = "CREATED_TIME";

    // Create the actual database entries
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE +";

}
