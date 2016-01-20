package tahastudio.storybuilder;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.db.SQLDatabase;
import tahastudio.storybuilder.fragments.AddCharacterElements;
import tahastudio.storybuilder.fragments.AddCharacters;
import tahastudio.storybuilder.fragments.AddEventElements;
import tahastudio.storybuilder.fragments.AddEvents;
import tahastudio.storybuilder.fragments.AddLocationElements;
import tahastudio.storybuilder.fragments.AddLocations;
import tahastudio.storybuilder.fragments.ShowCharacter;
import tahastudio.storybuilder.fragments.ShowEvent;
import tahastudio.storybuilder.fragments.ShowLocation;
import tahastudio.storybuilder.ui.TabViewer;

/**
 * This class will show a list of saved characters, locations, and events,
 * as well as the FAB menu to add elements to the story.
 * Implements: characterListener, locationListener, eventListener
 */
public class ShowStory extends AppCompatActivity implements
        AddCharacters.characterListener,
        AddLocations.locationListener,
        AddEvents.eventListener {

    // To programmatically add in the fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    // Create public static references for the story, so other classes can access them
    public static int SB_ID; // This value will not change unless a user selects a different story
    public static String CHARACTER_GENDER; // These values
    public static String CHARACTER_TYPE; // may change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Set the public static variable as the story _id from the db
        SB_ID = getIntent().getExtras().getInt("id");

        Intent get_title = getIntent(); // Get the Intent from CreateStoryTask
        // Grab the string and pass to the AsyncTask, setStoryTitle
        setStoryTitle setStoryTitle = new setStoryTitle(get_title.getStringExtra("title"));
        setStoryTitle.execute();

        // Find the FAB menu and add the actions
        final com.getbase.floatingactionbutton.FloatingActionsMenu fab =
                (com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.fab);

        // Find the inner menu for adding a character
        com.getbase.floatingactionbutton.FloatingActionButton characters_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.characters);
        characters_fab.setTitle("Add A Character");

        characters_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddCharacterElements(), "add_the_character");
            }
        });

        // Find the inner menu for adding a location
        com.getbase.floatingactionbutton.FloatingActionButton location_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.locations);
        location_fab.setTitle("Add A Location");

        location_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddLocationElements(), "add_the_location");
            }
        });

        // Find the inner menu for adding an event
        com.getbase.floatingactionbutton.FloatingActionButton events_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.events);
        events_fab.setTitle("Add An Event");

        events_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collapse the menu fab on click
                fab.collapse();

                // Call re-factored method for fragment transaction
                onFragmentSelected(new AddEventElements(), "add_the_event");
            }
        });

         // Find the TabLayout in the XML
        TabLayout tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        // Add the number tabs, set the title for each one. Content
        // will be provided by the TabViewer method
        tab_layout.addTab(tab_layout.newTab().setText("Characters"));
        tab_layout.addTab(tab_layout.newTab().setText("Locations"));
        tab_layout.addTab(tab_layout.newTab().setText("Events"));
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
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Implement the AddCharacters interface method for a ListView click
    public void onCharacterSelected(int id, String name) {
        // Bundle info and send user to the new fragment, ShowCharacter
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("id", id);

        // Set up the fragment with the bundle
        ShowCharacter showCharacter = new ShowCharacter();
        showCharacter.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showCharacter, "show_character");
    }

    // Implement the AddLocations interface method for a ListView click
    public void onLocationSelected(int id, String name) {
        // Bundle info and send user to the new fragment, ShowLocation
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("id", id);

        // Set up the fragment with the bundle
        ShowLocation showLocation = new ShowLocation();
        showLocation.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showLocation, "show_location");
    }

    // Implement the AddEvents interface method for a ListView click
    public void onEventSelected(int id, String name) {
        // Bundle info and send user to the new fragment, ShowEvent
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putInt("id", id);

        // Set up the fragment with the bundle
        ShowEvent showEvent = new ShowEvent();
        showEvent.setArguments(bundle);

        // Call re-factored method for fragment transaction
        onFragmentSelected(showEvent, "show_event");
    }

    // Re-factored method for all fragment transactions
    public void onFragmentSelected(Fragment fragment, String tag) {
        // APIs from FragmentTransaction to add, replace or remove fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Replace the fragment, add it to backstack, commit it
        fragmentTransaction
                .add(R.id.story_creation_layout, fragment)
                .addToBackStack(tag)
                .commit();
    }

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
                CHARACTER_TYPE = null;
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
                CHARACTER_GENDER = null;
        }
    }

    // This AsyncTask will set the TextView to show the title and the genre pic
    public class setStoryTitle extends AsyncTask<String, Void, String> {
        private TextView story_title = (TextView) findViewById(R.id.story_title);
        private String title;

        public setStoryTitle(String title) {
            this.title = title;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            story_title.setText(title); // Set the story title on the top TextView
        }

        @Override
        protected String doInBackground(String... params) {
            SQLDatabase db = SQLDatabase.getInstance(getBaseContext());
            Cursor cursor = db.getStoryGenre(); // Grab the genre string from the db

            if ( cursor.moveToFirst() ) { // If not null...
                return cursor.getString(cursor.getColumnIndex
                        (Constants.STORY_GENRE)); // ... return the string
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Get the pic from drawables for the genre the user selected
            int pic = getBaseContext().getResources().getIdentifier // Find the drawable
                    ("genre_" + result.replaceAll(" ", "_").toLowerCase(), // replace any spaces
                            "drawable", getPackageName());

            // Set the drawable next to the story title
            story_title.setCompoundDrawablesWithIntrinsicBounds(pic, 0, 0, 0);
        }
    }
}