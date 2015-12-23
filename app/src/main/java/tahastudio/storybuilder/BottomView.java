package tahastudio.storybuilder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The bottom view of the main activity. Will have a list of saved
 * user stories.
 */
public class BottomView extends Fragment {

    public BottomView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundleSavedInstance) {
        return inflater.inflate(R.layout.bottom_view,
                container,
                false);
    }

}
