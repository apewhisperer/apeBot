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

        playerController.getPlayerManager().loadItem(link, playerController.getScheduler());
        while (!playerController.getScheduler().isLoaded) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PositionThread positionThread = new PositionThread(playerController.getScheduler());
        positionThread.start();
        try {
            positionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
