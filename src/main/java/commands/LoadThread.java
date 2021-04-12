package commands;

import player.PlayerController;
import player.TrackScheduler;

public class LoadThread extends Thread {

    PlayerController playerController;
    String link;

    public LoadThread(String link, PlayerController playerController) {
        this.link = link;
        this.playerController = playerController;
    }

    @Override
    public void run() {

        TrackScheduler scheduler = playerController.getScheduler();
        scheduler.clearList();
        scheduler.setPosition(0);
        playerController.getPlayerManager().loadItem(link, playerController.getScheduler());
        while (!playerController.getScheduler().isLoaded()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
