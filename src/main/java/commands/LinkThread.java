package commands;

import player.PlayerController;

public class PlayThread extends Thread {

    PlayerController playerController;
    String command;

    public PlayThread(String command, PlayerController playerController) {
        this.command = command;
        this.playerController = playerController;
    }

    @Override
    public void run() {

        LoadThread loadThread = new LoadThread(command, playerController);
        loadThread.start();
        try {
            loadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        playerController.getPlayer().playTrack(playerController.getScheduler().getList().get(playerController.getScheduler().getPosition()));
        while (!playerController.getScheduler().isPositionSet()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
