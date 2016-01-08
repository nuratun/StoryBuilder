package tahastudio.storybuilder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.StoryBuilderMain;

/**
 * Splash screen
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
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
