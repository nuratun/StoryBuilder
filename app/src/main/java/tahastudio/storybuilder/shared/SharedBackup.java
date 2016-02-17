package tahastudio.storybuilder.shared;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupManager;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

import tahastudio.storybuilder.db.Constants;

/**
 * This will allow automatic backups in the background, without user intervention.
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

        } catch (Exception e) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int version, ParcelFileDescriptor newState) {

        try {
            synchronized (BackupManager.class) {
                super.onRestore(data, version, newState);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
