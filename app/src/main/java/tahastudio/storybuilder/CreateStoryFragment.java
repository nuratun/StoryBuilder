package tahastudio.storybuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.FileOutputStream;

/**
 * The FAB button on StoryBuilderMain leads here.
 */
public class CreateStoryFragment extends Fragment {
    FloatingActionButton the_second_fab;
    EditText story_title;

    public CreateStoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the layout for this fragment
        View create_story_layout = inflater.inflate(R.layout.fragment_create_story, container, false);

        CreateTheStory(create_story_layout);

        return create_story_layout;
    }

    public void CreateTheStory(View story_layout) {
        // Get the FAB for the story creation. Clicking on this FAB should bring a
        // small pop-up list with the ability to add a new character, plotline, or place
        the_second_fab = (FloatingActionButton) story_layout.findViewById(R.id.second_fab);

        // Link to the editText id in fragment_create_story XML
        story_title = (EditText) story_layout.findViewById(R.id.edit_title);

        // Filename to save to internal storage
        // TODO -> Implement the SQL database
        String FILENAME = "Story_Title";
        String title = story_title.getText().toString();

        // Try to save the story contents or fail gracefully
        try {
            FileOutputStream outstream = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
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

