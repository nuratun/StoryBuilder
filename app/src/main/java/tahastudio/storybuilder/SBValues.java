package tahastudio.storybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.widget.EditText;

/**
 * To process ContentValues
 */
public class SBValues {
    private ContentValues values;

    // Get the context, generic ContentValues instance, the db field to input data to, and
    // the entry text to insert
    public void processValues(Context context, String field, EditText convert, String database) {
        // Initialize the database using the context given
        SQLDatabase db = new SQLDatabase(context);

        // Convert the EditText field to a string
        String entry = convert.getText().toString();

        values = new ContentValues();
        values.put(field, entry);

        // Insert the rows
        db.insertRow(values, database);
    }

}
