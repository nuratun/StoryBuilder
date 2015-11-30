package tahastudio.storybuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.FileOutputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateStoryFragment extends Fragment {

    public CreateStoryFragment() {
        // Link storytitle to the editText id in fragment_create_story
        EditText storytitle = (EditText) getView().findViewById(R.id.edit_title);

        // Filename to save to internal storage
        String FILENAME = "Story_Title";
        String title = storytitle.getText().toString();

        // Try to save the story title or fail gracefully
        try {
            FileOutputStream outstream = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outstream.write(title.getBytes());
            outstream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_story, container, false);

    }
}
