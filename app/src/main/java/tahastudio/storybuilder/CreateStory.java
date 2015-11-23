package tahastudio.storybuilder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;

public class CreateStory extends AppCompatActivity {

    // Link storytitle to the editText id in fragment_create_story
    EditText storytitle = (EditText) findViewById(R.id.edit_title);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);

    }

    // Save the story title to internal storage
    public void SaveStoryTitle(View v) {
        // Filename to save to internal storage
        String FILENAME = "Story_Title";
        String title = storytitle.getText().toString();

        // Try to save the story title or fail gracefully
        try {
            FileOutputStream outstream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outstream.write(title.getBytes());
            outstream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_story, menu);
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
