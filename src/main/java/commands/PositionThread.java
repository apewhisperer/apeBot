package commands;

import player.TrackScheduler;

public class PositionThread extends Thread {
    TrackScheduler scheduler;

    public PositionThread(TrackScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {

        scheduler.setPosition(scheduler.getList().get(scheduler.getPosition()));
        while (!scheduler.positionSet) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
