package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fragment to replace BottomView
 */
public class StoryBuilderMainFragment extends Fragment {

    public StoryBuilderMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout = inflater.inflate(
                R.layout.fragment_story_builder_main,
                container,
                false);


        // Grab the user input from the XML
        final EditText the_story_title = (EditText) layout.findViewById(R.id.sb_title);
        final EditText the_story_genre = (EditText) layout.findViewById(R.id.sb_genre);
        final EditText the_story_notes = (EditText) layout.findViewById(R.id.sb_notes);

        // Get the button in the inflated layout. Must find by View first, which is -> layout
        final Button add_the_story = (Button) layout.findViewById(R.id.add_the_story);

        // Create a new intent to launch the CreateStory activity
        add_the_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO -> Add a cancel button

                // Get the string conversions of the input
                String sb_story_title = the_story_title.getText().toString();
                String sb_story_genre = the_story_genre.getText().toString();
                String sb_story_notes = the_story_notes.getText().toString();

                // Make sure both title and genre are non-empty strings
                if (sb_story_title.length() < 1 || sb_story_genre.length() < 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Both the title and " +
                            "genre are required fields", Toast.LENGTH_LONG).show();
                } else {
                    // Send to do db work in the background. Have to call from parent activity
                    StoryBuilderMain sb = new StoryBuilderMain();

                    Integer get_id = sb.callAddStoryTask(
                            sb_story_title,
                            sb_story_genre,
                            sb_story_notes);

                    // Add an intent for CreateStory
                    Intent callCreateStory = new Intent(getActivity(), CreateStory.class);

                    // Send the story title to the new activity, also
                    callCreateStory.putExtra("title", sb_story_title);
                    callCreateStory.putExtra("id", get_id);

                    // Call the new activity
                    startActivity(callCreateStory);
                }
            }
        });

        return layout;
    }
}
