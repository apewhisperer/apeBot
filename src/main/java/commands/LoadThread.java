package commands;

import player.PlayerController;

public class LoadThread extends Thread {

    PlayerController playerController;
    String link;

    public LoadThread(String link, PlayerController playerController) {
        this.link = link;
        this.playerController = playerController;
    }

    @Override
    public void run() {
        playerController.getPLAYER_MANAGER().loadItem(link, playerController.getSCHEDULER());
        while (!playerController.getSCHEDULER().isLoaded()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
