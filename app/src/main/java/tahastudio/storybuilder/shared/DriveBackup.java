package tahastudio.storybuilder.shared;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import tahastudio.storybuilder.activities.StoryBuilderMain;
import tahastudio.storybuilder.db.Constants;

/**
 * Backup to Google Drive
 */
public class DriveBackup extends StoryBuilderMain implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient apiClient;

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("GoogleAPI", "GoogleApiClient connected");
    }


    @Override
    public void onConnectionSuspended(int num) {
        Log.d("GoogleAPI", "GoogleApiClient suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("GoogleAPI", "GoogleApiClient failed " + result.toString());
        if ( !result.hasResolution() ) {
            // Show the error dialog
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0)
                    .show();
            return;
        }
        try {
            result.startResolutionForResult(this, Constants.REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.d("GoogleAPI", "exception " + e);
        }
    }

    // Called when activity is visible to connect to Drive
    @Override
    protected void onResume() {
        super.onResume();

        if ( apiClient == null ) {
            apiClient = new GoogleApiClient().Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        apiClient.connect();
    }

    @Override
    protected void onPause() {
        if ( apiClient != null ) {
            apiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);

        if ( request == Constants.REQUEST_CODE_RESOLUTION && result == RESULT_OK ) {
            apiClient.connect();
        }
    }

    public GoogleApiClient getApiClient() {
        return apiClient;
    }

}
