package tahastudio.storybuilder.shared;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

/**
 * Check if folder exists. If not, create a folder, then add the SQL backup file to it.
 */
public class DriveCreation extends DriveBackup {

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onConnected(connectionHint);
    }


    // Create a folder for the backup file
    public void createFolder() {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("StoryBuilder")
                .build();
        Drive.DriveApi.getRootFolder(getApiClient()).createFolder(getApiClient(),
                changeSet).setResultCallback(callback);
    }

    final ResultCallback<DriveFolder.DriveFolderResult> callback =
            new ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {

                }
            }
}
