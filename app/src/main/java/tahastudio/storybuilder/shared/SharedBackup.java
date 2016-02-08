package tahastudio.storybuilder.shared;

import android.app.backup.BackupAgentHelper;

/**
 * This will allow backup to a cloud service. Just Google Drive at the start, but
 * eventually more: Dropbox, Box, OneDrive, etc.
 */
public class SharedBackup extends BackupAgentHelper {

    public SharedBackup() { }

    @Override
    void onCreate() {

    }
}
