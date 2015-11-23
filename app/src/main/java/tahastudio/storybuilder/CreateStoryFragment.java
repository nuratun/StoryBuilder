package tahastudio.storybuilder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateStoryFragment extends Fragment {

    public CreateStoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.add_characters).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddCharacters.class));
            }
        });

        getActivity().findViewById(R.id.add_plotline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddPlotline.class));
            }
        });

        getActivity().findViewById(R.id.add_places).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddPlaces.class));
            }
        });

        return inflater.inflate(R.layout.fragment_create_story, container, false);
    }
}
