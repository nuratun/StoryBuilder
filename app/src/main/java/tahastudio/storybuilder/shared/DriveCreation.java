package tahastudio.storybuilder.shared;

import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.api.client.http.FileContent;

/**
 * Check if folder exists. If not, create a folder, then add the SQL backup to it.
 */
public class DriveCreation extends DriveBackup {

    // Create a folder for the backup file
    public void createBackup() {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("StoryBuilder")
                .build();
        Drive.DriveApi.getRootFolder(getApiClient()).createFolder(getApiClient(),
                changeSet).setResultCallback(callback);

        // So long as folder was created, backup the SQLite db
        Uri dbBackup = Uri.fromFile(new java.io.File(Environment.getDataDirectory().getPath()
                + "/data/tahastudio.storybuilder/databases/sb.db"));

        java.io.File content = new java.io.File(dbBackup.getPath());
        FileContent media = new FileContent("db", content);

        com.google.api.services.drive.model.File body =
                new com.google.api.services.drive.model.File();
        body.setName(content.getName());
        body.setMimeType("db");

    }

    final ResultCallback<DriveFolder.DriveFolderResult> callback =
            new ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(@NonNull DriveFolder.DriveFolderResult driveFolderResult) {
                    if ( !driveFolderResult.getStatus().isSuccess() ) {
                        Log.d("GoogleApiClient", "Error with creating folder");
                        return;
                    }
                    Log.d("GoogleApiClient", "folder created " + driveFolderResult.getDriveFolder()
                            .getDriveId());
                }
            };
}
