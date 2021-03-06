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
    private int fadeVolume;
    private boolean isStopped;
    private boolean isLoaded;
    private boolean isFailed;
    private boolean isPositioned;
    private boolean isFading;
    private boolean isLooped;

    public TrackScheduler(final AudioPlayer PLAYER) {
        this.PLAYER = PLAYER;
        LIST = new HashMap<>();
        position = 0;
        fadeVolume = 15;
        isStopped = false;
        isLoaded = false;
        isFailed = false;
        isPositioned = false;
        isFading = false;
        isLooped = false;
    }

    public boolean isLooped() {
        return isLooped;
    }

    public void setLooped(boolean looped) {
        isLooped = looped;
    }

    public boolean isFading() {
        return isFading;
    }

    public void setFading(boolean fading) {
        isFading = fading;
    }

    public int getFadeVolume() {
        return fadeVolume;
    }

    public void setFadeVolume(int fadeVolume) {
        this.fadeVolume = fadeVolume;
    }

    public int getPosition(AudioTrack track) {
        for (int i = 0; i < LIST.size(); i++) {
            if (track == LIST.get(i)) {
                return i;
            }
        }
        return 0;
    }

    public boolean isPositioned() {
        return isPositioned;
    }

    public void setPositioned(boolean positioned) {
        isPositioned = positioned;
    }

    public void clearList() {
        LIST.clear();
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

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
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
        if (PLAYLIST.getSelectedTrack() != null) {
            for (int i = (int) PLAYLIST.getSelectedTrack().getPosition(); i < PLAYLIST.getTracks().size(); i++) {
                LIST.put((int) (getList().size() - PLAYLIST.getSelectedTrack().getPosition()), PLAYLIST.getTracks().get((int) (i + PLAYLIST.getSelectedTrack().getPosition())));
            }
        } else {
            for (int i = getList().size(); i < PLAYLIST.getTracks().size(); i++) {
                LIST.put(getList().size(), PLAYLIST.getTracks().get(i));
            }
        }
        if (isPositioned()) {
            setPosition(getPosition(PLAYLIST.getSelectedTrack()));
        }
        PLAYER.addListener(this);
        setFailed(false);
        setLoaded(true);
    }

    @Override
    public void noMatches() {
        System.out.println("NO MATCHES FOUND");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        setLoaded(true);
        setFailed(true);
        System.out.println("TRACK LOAD FAILED");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (position < LIST.size() + 1) {
            if (endReason.mayStartNext) {
                if (!isLooped()) {
                    position++;
                    player.playTrack(LIST.get(position));
                } else {
                    player.playTrack(LIST.get(position).makeClone());
                }
            } else if (endReason == AudioTrackEndReason.STOPPED) {
                setStopped(true);
            }
        }
    }
}