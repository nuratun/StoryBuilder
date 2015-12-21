package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class CreateStory extends AppCompatActivity {
    // To be used for the checkboxes
    private String character_type;
    private String character_gender;
    private boolean plot;

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

        // Grab the title from StoryBuilderMain
        Intent get_title = getIntent();
        String title = get_title.getStringExtra("title");
        story_title.setText(title);

        // Grab the DB_ID from StoryBuilderMain
        //Intent get_int = getIntent();
        //int the_id = get_int.getIntExtra("id", 0);

        // Insert into database using the custom SQL method
        //SQLDatabase db = new SQLDatabase(CreateStory.this);
        //db.insertTheID(the_id);

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
                case R.id.sec_plot_checkbox:
                    plot = false;
                default:
                    plot = false;
            }
        }
    }
}