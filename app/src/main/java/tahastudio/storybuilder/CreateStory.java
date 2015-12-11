package tahastudio.storybuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;

public class CreateStory extends AppCompatActivity {
    TextView story_title;
    // Need to instantiate the TabViewer class to set
    // the tabs for TabLayout
    private TabViewer tab_viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Find the toolbar and set the action bar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the FAB for the story creation. Clicking on this FAB should bring
        // a small pop-up list with the ability to add a new character, plotline, or place
        FloatingActionButton the_second_fab = (FloatingActionButton)
                findViewById(R.id.second_fab);

        // Find the tablayout in activity_create_story.xml
        TabLayout tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        // Add the number tabs, and set the title for each one. Still need the
        // content for each tab, which the TabViewer class will fill
        tab_layout.addTab(tab_layout.newTab().setText("Characters"));
        tab_layout.addTab(tab_layout.newTab().setText("Plotline"));
        tab_layout.addTab(tab_layout.newTab().setText("Places"));
        tab_layout.setTabGravity(tab_layout.GRAVITY_FILL);

        // ViewPager allows flipping left and right through pages of data
        final ViewPager view_pager = (ViewPager) findViewById(R.id.pager);
        // Use the TabViewer class to get the list of fragments for the ViewPager
        tab_viewer = new TabViewer(getSupportFragmentManager(), tab_layout.getTabCount());

        // Set TabViewer  as the adapter, so ViewPager can flip through the fragments
        view_pager.setAdapter(tab_viewer);
        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Link to the editText id in fragment_create_story XML
        story_title = (TextView) findViewById(R.id.story_title);

        // Filename to save to internal storage
        // TODO -> Implement the SQL database
        String FILENAME = "Story_Title";
        String title = story_title.getText().toString();

        // Try to save the story contents or fail gracefully
        try {
            FileOutputStream outstream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outstream.write(title.getBytes());
            outstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        the_second_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStoryElements(v);
            }
        });
    }

    public void addStoryElements(View view) {

    }
}