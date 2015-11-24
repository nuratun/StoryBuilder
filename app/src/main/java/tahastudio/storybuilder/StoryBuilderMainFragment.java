package tahastudio.storybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment to start the CreateStory activity from the FAB click
 */
public class StoryBuilderMainFragment extends Fragment {
    FloatingActionButton the_fab;

    public StoryBuilderMainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        the_fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        the_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateStory.class));
            }
        });

        return inflater.inflate(R.layout.fragment_story_builder_main, container, false);
    }
}
