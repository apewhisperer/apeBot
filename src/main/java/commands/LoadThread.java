package commands;

import player.PlayerController;

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
        if (isPositioned) {
            playerController.getScheduler().clearList();
            playerController.getScheduler().setPositioned(true);
        }
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
