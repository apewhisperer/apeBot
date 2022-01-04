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
        scheduler.setFading(true);
        if (!scheduler.isFading()) {
            issueFade(player, scheduler);
        }
    }

    private void issueFade(AudioPlayer player, TrackScheduler scheduler) {
        if (player.getVolume() > 0) {
            scheduler.setFadeVolume(player.getVolume());
            while (player.getVolume() > 0 || scheduler.isFading()) {
                player.setVolume(player.getVolume() - 1);
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            scheduler.setFading(false);
        } else if (player.getVolume() == 0) {
            while (player.getVolume() < scheduler.getFadeVolume() || scheduler.isFading()) {
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
