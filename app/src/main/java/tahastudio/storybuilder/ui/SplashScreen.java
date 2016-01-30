package tahastudio.storybuilder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.StoryBuilderMain;

/**
 * Splash screen showing the TahaStudio logo
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000); // Show for 3000 milliseconds
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Start the main activity
                    Intent intent = new Intent(SplashScreen.this, StoryBuilderMain.class);
                    startActivity(intent);
                }
            }
        }; timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
