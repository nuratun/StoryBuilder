package tahastudio.storybuilder.shared;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import tahastudio.storybuilder.activities.StoryBuilderMain;

/**
 * Backup to Google Drive
 */
public abstract class DriveBackup extends StoryBuilderMain implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.onConnectionFailedListener {

    private GoogleApiClient apiClient;

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


    public GoogleApiClient getApiClient() {
        return apiClient;
    }



}
