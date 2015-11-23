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
public class AddPlacesFragment extends Fragment {

    EditText planetname;
    EditText countryname;
    EditText cityname;
    Button placesave;
    List<String> addplace = new ArrayList<String>();

    public AddPlacesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_places, container, false);

        // Find place info
        planetname = (EditText) view.findViewById(R.id.planet_name);
        countryname = (EditText) view.findViewById(R.id.country_name);
        cityname = (EditText) view.findViewById(R.id.city_name);
        placesave = (Button) view.findViewById(R.id.place_save);

        placesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });


        return view;
    }

    private void addPlace() {
        // Convert each EditText to it's string value
        String get_planetname = planetname.getText().toString();
        String get_countryname = countryname.getText().toString();
        String get_cityname = cityname.getText().toString();

        // Add each string to the addplace ArrayList
        addplace.add(get_planetname);
        addplace.add(get_countryname);
        addplace.add(get_cityname);
    }
}
