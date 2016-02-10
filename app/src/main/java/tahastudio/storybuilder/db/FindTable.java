package tahastudio.storybuilder.db;

/**
 * The below switch statement is used across classes. Better to refactor into one class and
 * call it, when needed.
 * Called by: SQLDatabase, StoryProvider
 */
public class FindTable {

    public String findTheTable(int uri) {
        String table = null;

        switch (uri) {
            case Constants.STORY_ID:
                table = Constants.STORY_TABLE;
                break;
            case Constants.STORY_LIST:
                table = Constants.STORY_TABLE;
                break;
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
            case Constants.PLOT_ID:
                table = Constants.STORY_PLOT_TABLE;
                break;
            case Constants.PLOT_LIST:
                table = Constants.STORY_PLOT_TABLE;
                break;
        }
        return table;
    }
}
