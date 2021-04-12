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

    private final AudioPlayer PLAYER;
    private final Map<Integer, AudioTrack> LIST;
    private int position;
    private boolean isStopped;
    private boolean isLoaded;
    private boolean isFailed;

    public TrackScheduler(final AudioPlayer PLAYER) {
        LIST = new HashMap<>();
        this.PLAYER = PLAYER;
        isStopped = false;
        isLoaded = false;
        isFailed = false;
        position = 0;
    }

    public void getPosition(AudioTrack track) {
        for (int i = 0; i < LIST.size(); i++) {
            if (track == LIST.get(i)) {
                position = i;
                System.out.println("postion: " + i);
            }
        }
    }

    public void clearList() {
        System.out.println("clearlist");
        LIST.clear();
    }

    public boolean isStopped() {
        return isStopped;
    }


    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public Map<Integer, AudioTrack> getList() {
        return LIST;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    @Override
    public void trackLoaded(final AudioTrack TRACK) {
        setLoaded(false);
        LIST.put(LIST.size(), TRACK);
        PLAYER.addListener(this);
        setFailed(false);
        setLoaded(true);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist PLAYLIST) {
        setLoaded(false);
        for (int i = (int) PLAYLIST.getSelectedTrack().getPosition(); i < PLAYLIST.getTracks().size() - PLAYLIST.getSelectedTrack().getPosition(); i++) {
            LIST.put((int) (position + i - PLAYLIST.getSelectedTrack().getPosition()), PLAYLIST.getTracks().get((int) (i + PLAYLIST.getSelectedTrack().getPosition())));
        }
        PLAYER.addListener(this);
        setFailed(false);
        setLoaded(true);
    }

    @Override
    public void noMatches() {
        System.out.println("no matches found");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        setLoaded(true);
        setFailed(true);
        System.out.println("track load failed");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (position < LIST.size() + 1) {
            if (endReason.mayStartNext) {
                System.out.println("track end" + " pos: " + position);
                position++;
                player.playTrack(LIST.get(position));
            } else if (endReason == AudioTrackEndReason.STOPPED) {
                System.out.println("track stopped");
                setStopped(true);
            } else if (endReason == AudioTrackEndReason.REPLACED) {
                System.out.println("replaced");
            }
        }
    }
}