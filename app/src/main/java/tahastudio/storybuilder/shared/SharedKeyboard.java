package tahastudio.storybuilder.shared;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Since multiple classes use the same code to close the virtual keyboard,
 * it will be easier to use one class that every other class will call to.
 */
public class SharedKeyboard extends AppCompatActivity {

    public SharedKeyboard() { }

    public static void closeKeyboard(final Activity activity, final View view) {

        if ( view != null ) {
            // Close the virtual keyboard
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        activity.getApplicationContext().getSystemService
                                (Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow
                        (activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
