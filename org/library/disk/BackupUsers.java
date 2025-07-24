package org.library.disk;

import org.library.users.User;
import org.library.components.Print;

import java.util.Map;

public class BackupUsers implements Runnable {

    private final Map<String, User> users;
    private final long intervalMillis;
    private volatile boolean running;
    private final Thread thread;
    private final String filePath;

    public BackupUsers(Map<String, User> users,String file, int intervalSecs) {
        this.users = users;
        this.filePath=file;
        this.intervalMillis = intervalSecs * 1000L;
        this.running = true;
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
    }

    public void startBackup() {
        thread.start();
    }

    public void stopBackup() {
        thread.interrupt();
    }

    @Override
    public void run() {
        Print.info("User backup thread started. Saving every " + (intervalMillis / 1000) + " seconds.");
        while (running) {
            try {
                Thread.sleep(intervalMillis);
                SyncData.saveUsers(users,filePath);

            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                Print.error("User backup failed: " + e.getMessage());
            }
        }
        Print.info("User backup thread stopped.");
    }
}
