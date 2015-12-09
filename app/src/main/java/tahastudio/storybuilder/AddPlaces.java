package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Second tab for SB
 */
public class AddPlaces extends Fragment {

    public AddPlaces() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        return inflater.inflate(R.layout.fragment_add_places, container, false);
    }
}
