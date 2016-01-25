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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView fab_author = (TextView) findViewById(R.id.fab_author);
        fab_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        fab_author.setText(Html.fromHtml("<a href='https://github.com/futuresimple/android-floating-action-button'>Jerzy Chalupski</a>"));

        TextView icon_author = (TextView) findViewById(R.id.icons);
        icon_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        icon_author.setText(Html.fromHtml("<a href='http://www.freepik.com'>Freepik</a>"));

        TextView asset_author = (TextView) findViewById(R.id.assets_author);
        asset_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        asset_author.setText(Html.fromHtml("<a href='https://romannurik.github.io/AndroidAssetStudio/index.html'>Android Asset Studio</a>"));
    }
}
