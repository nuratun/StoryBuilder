package tahastudio.storybuilder;

import android.content.Intent;
import android.os.AsyncTask;
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

public class CreateStory extends AppCompatActivity {
    // To programmatically add in the element fragments
    FragmentManager fragmentManager = getSupportFragmentManager();
    // Need to get APIs from FragmentTransaction to add, replace or remove fragments
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    // To be used for the checkboxes
    private String character_type;
    private String character_gender;
    private boolean plot;

    // TODO -> Add create to https://github.com/futuresimple/android-floating-action-button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

        // Find the toolbar and set the action bar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("StoryBuilder");

        // Find the title so we can add in the user's story title
        TextView story_title = (TextView) findViewById(R.id.story_title);

        Intent get_title = getIntent(); // Get the Intent sent from StoryBuilderMain
        String title = get_title.getStringExtra("title"); // Get the title from the intent
        story_title.setText(title); // Set the TextView for this activity

        // Find the FAB menu and add the actions
        com.getbase.floatingactionbutton.FloatingActionsMenu fab =
                (com.getbase.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.fab);

        // Find the inner menu for adding a character
        com.getbase.floatingactionbutton.FloatingActionButton characters_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.characters);

        // Find the inner menu for adding a place
        com.getbase.floatingactionbutton.FloatingActionButton place_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.places);

        // Find the inner menu for adding a plot
        com.getbase.floatingactionbutton.FloatingActionButton plot_fab =
                (com.getbase.floatingactionbutton.FloatingActionButton)
                        findViewById(R.id.plots);

        // Now add the buttons to the menu
        // TODO -> below is causing following error: The specified child already has a parent.
        // You must call removeView() on the child's parent first.
        fab.addButton(characters_fab);
        fab.addButton(place_fab);
        fab.addButton(plot_fab);

        // Now create the menu clicks for each button menu
        characters_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction
                        .add(R.id.add_characters_tab, new AddCharacterElements())
                        .addToBackStack("add_the_character")
                        .commit();
            }
        });

        place_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction.add(R.id.add_places_tab, new AddPlaceElements())
                        .addToBackStack("add_the_place")
                        .commit();
            }
        });

        plot_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the fragment, add it to the backstack, and commit it
                fragmentTransaction.add(R.id.add_plotline_tab, new AddPlotlineElements())
                        .addToBackStack("add_the_plot")
                        .commit();
            }
        });

         // Find the TabLayout in activity_create_story.xml
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
        TabViewer tab_viewer = new TabViewer(getSupportFragmentManager(), tab_layout.getTabCount());

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
    }

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

    // Create the tables on a background thread
    private class createStoryTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate() {
            super.onProgressUpdate();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            // Set boolean value initially to false
            boolean completed = false;
            SQLDatabase db = new SQLDatabase(CreateStory.this);

            try {
                db.insertTheID(params[0]);
                completed = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return completed;
        }

        @Override
        protected void onPostExecute(Boolean completed) {
            super.onPostExecute(completed);
        }
    }
}