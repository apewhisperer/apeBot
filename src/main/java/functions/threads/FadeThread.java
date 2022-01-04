package functions.threads;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import player.PlayerController;
import player.TrackScheduler;

public class FadeThread extends Thread {

    PlayerController playerController;

    public FadeThread(PlayerController playerController) {
        this.playerController = playerController;
    }

    @Override
    public void run() {
        AudioPlayer player = playerController.getPlayer();
        TrackScheduler scheduler = playerController.getScheduler();
        if (!scheduler.isFading()) {
            if (player.getVolume() > 0) {
                scheduler.setFadeVolume(player.getVolume());
                scheduler.setFading(true);
                while (player.getVolume() > 0) {
                    player.setVolume(player.getVolume() - 1);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scheduler.setFading(false);
            } else if (player.getVolume() == 0) {
                scheduler.setFading(true);
                while (player.getVolume() < scheduler.getFadeVolume()) {
                    player.setVolume(player.getVolume() + 1);
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scheduler.setFading(false);
            }
        }
    }
}
