package player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import commands.PositionThread;

import java.util.HashMap;
import java.util.Map;

public final class TrackScheduler extends AudioEventAdapter implements AudioLoadResultHandler {

    public boolean isStopped = false;
    public boolean isLoaded = false;
    public boolean positionSet = false;
    private final AudioPlayer player;
    private final Map<Integer, AudioTrack> list = new HashMap<>();
    int position;

    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
    }

    public void setPosition(AudioTrack track) {

        positionSet = false;
        for (int i = 0; i < list.size(); i++) {
            if (track == list.get(i)) {
                position = i;
                positionSet = true;
                System.out.println("postion set to : " + i);
            }
        }
    }

    public Map<Integer, AudioTrack> getList() {
        return list;
    }

    public int getPosition() {
        return position;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {

        isLoaded = false;
        list.put(list.size(), track);
        player.addListener(this);
        isLoaded = true;
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {

        isLoaded = false;
        for (int i = 0; i < playlist.getTracks().size(); i++) {
            list.put(position + i, playlist.getTracks().get(i));
        }
        player.addListener(this);
        isLoaded = true;
    }

    @Override
    public void noMatches() {
        System.out.println("no matches found");
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        System.out.println("track load failed");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if (position < list.size() + 1) {
            if (endReason.mayStartNext) {
                System.out.println("track end" + " pos: " + position);
                position++;
                player.playTrack(list.get(position));
            } else if (endReason == AudioTrackEndReason.STOPPED) {
                System.out.println("track stopped");
                setStopped(true);
            }
        }
    }
}