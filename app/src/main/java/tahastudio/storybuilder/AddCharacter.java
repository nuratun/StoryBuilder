package tahastudio.storybuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AddCharacter extends AppCompatActivity {

    EditText charactername;
    EditText characterbirthplace;
    EditText characterpersonality;
    Button charactersave;
    List<String> addcharacter = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_character);

        // Find character info
        charactername = (EditText) findViewById(R.id.character_name);
        characterbirthplace = (EditText) findViewById(R.id.character_birthplace);
        characterpersonality = (EditText) findViewById(R.id.character_personality);
        charactersave = (Button) findViewById(R.id.character_save);

        // On button click, save info to array
        charactersave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacter();
            }
        });
    }

    private void addCharacter() {
        // Convert each EditText to the equivalent string value
        String get_charactername = charactername.getText().toString();
        String get_characterbirthplace = characterbirthplace.getText().toString();
        String get_characterpersonality = characterpersonality.getText().toString();

        // Add each string value to the ArrayList to save
        addcharacter.add(get_charactername);
        addcharacter.add(get_characterbirthplace);
        addcharacter.add(get_characterpersonality);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_character, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
