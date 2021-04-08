package player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.HashMap;
import java.util.Map;

public final class TrackScheduler extends AudioEventAdapter implements AudioLoadResultHandler {

    private final AudioPlayer player;
    private final Map<Integer, AudioTrack> list;
    private boolean isStopped;
    private boolean isLoaded;
    private int position;

    public TrackScheduler(final AudioPlayer player) {
        list = new HashMap<>();
        this.player = player;
        isStopped = false;
        isLoaded = false;
        position = 0;
    }

    public void clearList() {
        System.out.println("clearlist");
        list.clear();
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void getPosition(AudioTrack track) {
        for (int i = 0; i < list.size(); i++) {
            if (track == list.get(i)) {
                position = i;
                System.out.println("postion: " + i);
            }
        }
    }

    public Map<Integer, AudioTrack> getList() {
        return list;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
        for (int i = (int) playlist.getSelectedTrack().getPosition(); i < playlist.getTracks().size() - playlist.getSelectedTrack().getPosition(); i++) {
            list.put((int) (position + i - playlist.getSelectedTrack().getPosition()), playlist.getTracks().get((int) (i + playlist.getSelectedTrack().getPosition())));
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
            } else if (endReason == AudioTrackEndReason.REPLACED) {
                System.out.println("replaced");
            }
        }
    }
}