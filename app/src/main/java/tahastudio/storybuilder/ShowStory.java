package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import tahastudio.storybuilder.fragments.AddCharacterElements;
import tahastudio.storybuilder.fragments.AddPlaceElements;
import tahastudio.storybuilder.fragments.AddPlotlineElements;
import tahastudio.storybuilder.ui.TabViewer;

public class ShowStory extends AppCompatActivity {
    // To programmatically add in the element fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    // Create public static reference, so other classes can access it
    public static int SB_ID;

    // TODO -> To be used for the checkboxes
    private String character_type;
    private String character_gender;
    private boolean plot;

    // TODO -> Add credit to https://github.com/futuresimple/android-floating-action-button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Find the toolbar, set it, and set the title
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("StoryBuilder");

        // TextView to be used for story title
        TextView story_title = (TextView) findViewById(R.id.story_title);

        Intent get_title = getIntent(); // Get the Intent from CreateStoryTask
        String title = get_title.getStringExtra("title"); // Get the title from the intent
        story_title.setText(title); // Set the above TextView as the title

        // Set the public static variable as the story _id from the db
        SB_ID = getIntent().getExtras().getInt("id");

        // Find the FAB menu and add the actions
        final com.getbase.floatingactionbutton.FloatingActionsMenu fab =
                (com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.fab);

        // TODO -> Factor out into one method - begin
        // Find the inner menu for adding a character
        com.getbase.floatingactionbutton.FloatingActionButton characters_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.characters);

        characters_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, commit it
                fragmentTransaction
                        .add(R.id.story_creation_layout, new AddCharacterElements())
                        .addToBackStack("add_the_character")
                        .commit();
            }
        });

        // Find the inner menu for adding a place
        com.getbase.floatingactionbutton.FloatingActionButton place_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.places);

        place_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, commit it
                fragmentTransaction
                        .add(R.id.story_creation_layout, new AddPlaceElements())
                        .addToBackStack("add_the_place")
                        .commit();
            }
        });

        // Find the inner menu for adding a plot
        com.getbase.floatingactionbutton.FloatingActionButton plot_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.plots);

        plot_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // APIs from FragmentTransaction to add, replace or remove fragments
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Add the fragment, add it to the backstack, commit it
                fragmentTransaction
                        .add(R.id.story_creation_layout, new AddPlotlineElements())
                        .addToBackStack("add_the_plot")
                        .commit();
            }
        });
        // TODO -> Factor out into one method - end

         // Find the TabLayout in the XML
        TabLayout tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        // Add the number tabs, set the title for each one. Content
        // will be provided by the TabViewer method
        tab_layout.addTab(tab_layout.newTab().setText("Characters"));
        tab_layout.addTab(tab_layout.newTab().setText("Places"));
        tab_layout.addTab(tab_layout.newTab().setText("Plotlines"));
        tab_layout.setTabGravity(tab_layout.GRAVITY_FILL);

        // ViewPager allows flipping left and right through pages (tabs) of data
        final ViewPager view_pager = (ViewPager) findViewById(R.id.pager);

        // TabViewer class to get the list of fragments for the ViewPager
        TabViewer tab_viewer = new TabViewer(getSupportFragmentManager(), tab_layout.getTabCount());

        // Set TabViewer as adapter of the ViewPager. Since TabViewer created the tabs (fragments),
        // ViewPager can now flip through them
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
    }

    // Recreate main activity when back button is pressed. Seems to improve performance.
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, StoryBuilderMain.class));
        this.finish();
    }

    // TODO -> All the methods below
    // May be a variation -> main character & the antagonist, just the main character
    // or just the antagonist.
    public void mCharacterCheckbox(View view) {
        // Is a checkbox checked?
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.main_character_checkbox:
                if ( checked ) {
                    character_type = "Protagonist";
                    break;
                }
            case R.id.antagonist_character_checkbox:
                if ( checked ) {
                    character_type += "Antagonist";
                    break;
                }
        }
    }

    public void characterGender(View view) {
        // Is a radio button selected?
        boolean checked = ((RadioButton) view).isChecked();

        // Return which radio button was selected
        switch (view.getId()) {
            case R.id.male_gender:
                if (checked)
                    character_gender = "male";
                break;
            case R.id.female_gender:
                if (checked)
                    character_gender = "female";
                break;
            case R.id.other_gender:
                if (checked)
                    character_gender = "other";
                break;
        }
    }

    public void mPlotline(View view) {
        // Is radio button selected?
        boolean checked = ((RadioButton) view).isChecked();

        // If one of them is selected...
        if ( checked ) {
            switch (view.getId()) {
                case R.id.main_plot_checkbox:
                    plot = true;
                default:
                    plot = false;
            }
        }
    }
}