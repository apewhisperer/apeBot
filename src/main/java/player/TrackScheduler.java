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
    Map<Integer, AudioTrack> list = new HashMap<>();
    int position;

    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
    }

    private void setPosition(AudioTrack track) {

        for (int i = 0; i < list.size(); i++) {
            if (track == list.get(i)) {
                position = i;
                System.out.println("postion set to : " + i);
            }
        }
    }

    @Override
    public void trackLoaded(final AudioTrack track) {

        list.put(list.size(), track);
        setPosition(track);
        player.addListener(this);
        player.playTrack(list.get(position));
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {

        for (int i = 0; i < playlist.getTracks().size(); i++) {
            list.put(position + i, playlist.getTracks().get(i));
        }
        setPosition(playlist.getSelectedTrack());
        player.addListener(this);
        player.playTrack(list.get(position));
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

        if (position < list.size() + 1 && endReason.mayStartNext) {
            System.out.println("track end" + " pos: " + position);
            position++;
            player.playTrack(list.get(position));
        }
    }
}