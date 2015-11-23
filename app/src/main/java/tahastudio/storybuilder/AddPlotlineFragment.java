package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPlotlineFragment extends Fragment {

    // Prepare the variables to hold the strings and button
    EditText mainplot;
    EditText firstsubplot;
    Button plotsave;
    List<String> addplot = new ArrayList<String>();

    public AddPlotlineFragment() {
    }

    public void addPlotline() {
        // Grab the user input strings and convert to String format
        String get_mainplot = (String) mainplot.getText().toString();
        String get_firstsubplot = (String) firstsubplot.getText().toString();

        // Add to arraylist
        addplot.add(get_mainplot);
        addplot.add(get_firstsubplot);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // Find the user inputted strings in the layout
            mainplot = (EditText) getActivity().findViewById(R.id.main_plotline);
            firstsubplot = (EditText) getActivity().findViewById(R.id.first_subplot);
            plotsave = (Button) getActivity().findViewById(R.id.plot_save);

            plotsave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addPlotline();
                }
            });

        return inflater.inflate(R.layout.fragment_add_plotline, container, false);
    }
}
