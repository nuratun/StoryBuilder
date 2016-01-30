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

        // For the FAB implemention in ShowStory
        TextView fab_author = (TextView) findViewById(R.id.fab_author);
        fab_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        fab_author.setText(Html.fromHtml("<a href='https://github.com/futuresimple/android-floating-action-button'>Jerzy Chalupski</a>"));

        // For some of the genre icons
        TextView icon_author = (TextView) findViewById(R.id.icons);
        icon_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        icon_author.setText(Html.fromHtml("<a href='http://www.freepik.com'>Freepik</a>"));

        // For some the assets generation
        TextView asset_author = (TextView) findViewById(R.id.assets_author);
        asset_author.setMovementMethod(LinkMovementMethod.getInstance());
        // Set the TextView to a link
        asset_author.setText(Html.fromHtml("<a href='https://romannurik.github.io/AndroidAssetStudio/index.html'>Android Asset Studio</a>"));

        // For the promo banner on the PlayStore
        TextView banner_credit = (TextView) findViewById(R.id.banner);
        banner_credit.setMovementMethod(LinkMovementMethod.getInstance());
        banner_credit.setText(Html.fromHtml("<a href='www.freepik.com/free-vector/typewriting-illustration-vector-free_756502.htm'>Freepik</a>"));
    }
}
