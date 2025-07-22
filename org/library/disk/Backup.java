package org.library.disk;

import org.library.users.library.Library;
import org.library.components.Print;

public class Backup implements Runnable {

    private final Library library;
    private final String filePath;
    private final long intervalMillis;
    private volatile boolean running;
    private final Thread thread;

    public Backup(Library library, String filePath, int intervalSecs) {
        this.library = library;
        this.filePath = filePath;
        this.intervalMillis = intervalSecs * 1000L;
        this.running = true;
        this.thread = new Thread(this);
        this.thread.setDaemon(true); // Set as daemon thread
    }

    public void startBackup() {
        thread.start();
    }

    public void stopBackup() {
        running = false;
        thread.interrupt();
    }

    @Override
    public void run() {
        Print.info("Backup thread started. Saving every " + (intervalMillis / 1000) + " seconds.");

        while (running) {
            try {
                Thread.sleep(intervalMillis);
                Sync.safeSaveToDisk(library, filePath);
            } catch (InterruptedException e) {
                Print.warning("Backup thread interrupted.");
                break;
            } catch (Exception e) {
                Print.error("Backup failed: " + e.getMessage());
            }
        }

        Print.info("Backup thread stopped.");
    }
}
