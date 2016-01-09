package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * About page for the menu
 */
public class About extends AppCompatActivity {
    private String fab_author_link = "<a href='https://github.com/futuresimple/" +
            "android-floating-action-button'>Jerzy Chalupski</a>";
    private String icon_author_link = "<a href='http://www.freepik.com'>Freepik</a>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView fab_author = (TextView) findViewById(R.id.fab_author);
        fab_author.setMovementMethod(LinkMovementMethod.getInstance());
        fab_author.setText(Html.fromHtml(fab_author_link)); // Set the TextView to a link

        TextView icon_author = (TextView) findViewById(R.id.icons);
        icon_author.setMovementMethod(LinkMovementMethod.getInstance());
        icon_author.setText(Html.fromHtml(icon_author_link));
    }
}
