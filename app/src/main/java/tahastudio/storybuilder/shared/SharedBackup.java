package tahastudio.storybuilder.shared;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

import tahastudio.storybuilder.db.Constants;

/**
 * This will allow backup to a cloud service. Just Google Drive at the start, but
 * eventually more: Dropbox, Box, OneDrive, etc.
 */
public class SharedBackup extends BackupAgentHelper {

    public SharedBackup() { }

    // Only required to override onCreate
    @Override
    public void onCreate() {

        try {
            // Backup the SQLite db file in the app directory
            FileBackupHelper helper = new FileBackupHelper(this,
                    this.getExternalFilesDir(Constants.DATABASE_NAME).getAbsolutePath());
            addHelper(Constants.DATABASE_NAME, helper);

            Log.d("backup_created", "Backup file is created");
        } catch (Exception e) {
            Log.d("backup_created", "error here");
            e.printStackTrace();
        }
    }

    // Will override the backup method to enable manual backup
    @Override
    public void onBackup(ParcelFileDescriptor oldState,
                         BackupDataOutput data,
                         ParcelFileDescriptor newState) {
        try {
            synchronized (BackupManager.class) {
                super.onBackup(oldState, data, newState);
                Log.d("onBackup", "backup changed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("onBackup", "error onbackup");
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int version, ParcelFileDescriptor newState) {

        try {
            synchronized (BackupManager.class) {
                super.onRestore(data, version, newState);
                Log.d("onRestore", "on restore called successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("onRestore", "there was an error");
        }
    }
}
