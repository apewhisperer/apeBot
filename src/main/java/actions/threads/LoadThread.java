package actions.threads;

import player.PlayerController;
import player.TrackScheduler;

public class LoadThread extends Thread {

    String link;
    PlayerController playerController;
    boolean isPositioned;

    public LoadThread(String link, PlayerController playerController, boolean isPositioned) {
        this.link = link;
        this.playerController = playerController;
        this.isPositioned = isPositioned;
    }

    @Override
    public void run() {
        TrackScheduler scheduler = playerController.getScheduler();
        if (isPositioned) {
            scheduler.clearList();
            scheduler.setPosition(0);
            scheduler.setPositioned(true);
        }
        playerController.getPlayerManager().loadItem(link, scheduler);
        while (!scheduler.isLoaded()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
