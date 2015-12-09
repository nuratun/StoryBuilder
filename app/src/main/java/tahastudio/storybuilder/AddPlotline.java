package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 3rd tab for SB
 */
public class AddPlotline extends Fragment {

    public AddPlotline() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return it
        return inflater.inflate(R.layout.fragment_add_plotline, container, false);
    }
}
