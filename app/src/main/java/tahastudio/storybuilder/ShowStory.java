package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.fragments.AddCharacterElements;
import tahastudio.storybuilder.fragments.AddCharacters;
import tahastudio.storybuilder.fragments.AddPlaceElements;
import tahastudio.storybuilder.fragments.AddPlaces;
import tahastudio.storybuilder.fragments.AddPlotElements;
import tahastudio.storybuilder.fragments.AddPlots;
import tahastudio.storybuilder.fragments.ShowCharacter;
import tahastudio.storybuilder.fragments.ShowPlace;
import tahastudio.storybuilder.fragments.ShowPlot;
import tahastudio.storybuilder.ui.TabViewer;

/**
 * This class will show a list of saved characters, places, and plots,
 * as well as the FAB menu to add elements to the story.
 * Implements: characterListener, placeListener, plotListener
 */
public class ShowStory extends AppCompatActivity implements
        AddCharacters.characterListener,
        AddPlaces.placeListener,
        AddPlots.plotListener {

    // To programmatically add in the fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    // Create public static reference, so other classes can access it
    public static int SB_ID;

    // TODO -> To be used for the checkboxes
    public static String CHARACTER_TYPE;
    public static String CHARACTER_GENDER;
    public static String PLOT_TYPE;

    // TODO -> Add credit to https://github.com/futuresimple/android-floating-action-button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Find the toolbar, set it, and set the title
        android.support.v7.widget.Toolbar toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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

        // Find the inner menu for adding a character
        com.getbase.floatingactionbutton.FloatingActionButton characters_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.characters);

        characters_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddCharacterElements(), "add_the_character");
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

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddPlaceElements(), "add_the_place");
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

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddPlotElements(), "add_the_plot");
            }
        });

         // Find the TabLayout in the XML
        TabLayout tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        // Add the number tabs, set the title for each one. Content
        // will be provided by the TabViewer method
        tab_layout.addTab(tab_layout.newTab().setText("Characters"));
        tab_layout.addTab(tab_layout.newTab().setText("Places"));
        tab_layout.addTab(tab_layout.newTab().setText("Plots"));
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

    // Implement the AddCharacters interface method for a ListView click
    public void onCharacterSelected(String name) {
        // Bundle info and send user to the new fragment, ShowCharacter
        Bundle bundle = new Bundle();
        bundle.putString("name", name);

        // Set up the fragment with the bundle
        ShowCharacter showCharacter = new ShowCharacter();
        showCharacter.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showCharacter, "show_character");
    }

    // Implement the AddPlaces interface method for a ListView click
    public void onPlaceSelected(String name) {
        // Bundle info and send user to the new fragment, ShowPlace
        Bundle bundle = new Bundle();
        bundle.putString("name", name);

        // Set up the fragment with the bundle
        ShowPlace showPlace = new ShowPlace();
        showPlace.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showPlace, "show_place");
    }

    // Implement the AddPlots interface method for a ListView click
    public void onPlotSelected(String name) {
        // Bundle info and send user to the new fragment, ShowPlot
        Bundle bundle = new Bundle();
        bundle.putString("name", name);

        // Set up the fragment with the bundle
        ShowPlot showPlot = new ShowPlot();
        showPlot.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showPlot, "show_plot");
    }

    // Re-factored method for all fragment transactions
    public void onFragmentSelected(Fragment fragment, String tag) {
        // APIs from FragmentTransaction to add, replace or remove fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Add the fragment, add it to the backstack, commit it
        fragmentTransaction
                .add(R.id.story_creation_layout, fragment)
                .addToBackStack(tag)
                .commit();
    }

    // TODO -> All the methods below
    // May be a variation -> main character & the antagonist, just the main character
    // or just the antagonist.
    public void mCharacterCheckbox(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.sb_character_main:
                if ( checked ) {
                    CHARACTER_TYPE = Constants.CHARACTER_TYPE_PROTAGONIST;
                    break;
                }
            case R.id.sb_character_antagonist:
                if ( checked ) {
                    CHARACTER_TYPE = Constants.CHARACTER_TYPE_ANTAGONIST;
                    break;
                }
            default:
                CHARACTER_TYPE = Constants.CHARACTER_TYPE_OTHER;
        }
    }

    public void characterGender(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Return which radio button was selected
        switch (view.getId()) {
            case R.id.male_gender:
                if (checked)
                    CHARACTER_GENDER = Constants.CHARACTER_GENDER_MALE;
                break;
            case R.id.female_gender:
                if (checked)
                    CHARACTER_GENDER = Constants.CHARACTER_GENDER_FEMALE;
                break;
            case R.id.other_gender:
                if (checked)
                    CHARACTER_GENDER = Constants.CHARACTER_GENDER_OTHER;
                break;
            default:
                CHARACTER_GENDER = Constants.CHARACTER_GENDER_OTHER;
        }
    }

    public void mPlotline(View view) {
        // Is radio button selected?
        boolean checked = ((RadioButton) view).isChecked();

        // If one of them is selected...
        if ( checked ) {
            switch (view.getId()) {
                case R.id.main_plot_checkbox:
                    PLOT_TYPE = Constants.PLOT_MAIN;
                default:
                    PLOT_TYPE = Constants.PLOT_SUPPORTING;
            }
        }
    }
}