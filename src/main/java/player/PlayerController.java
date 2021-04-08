package player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.voice.AudioProvider;

public class PlayerController {

    private final AudioPlayerManager PLAYER_MANAGER = new DefaultAudioPlayerManager();
    private final AudioPlayer PLAYER = PLAYER_MANAGER.createPlayer();
    private final AudioProvider PROVIDER = new PlayerProvider(PLAYER);
    private final TrackScheduler SCHEDULER = new TrackScheduler(PLAYER);

    public PlayerController() {
        PLAYER.setVolume(10);
        init();
    }

    public TrackScheduler getSCHEDULER() {
        return SCHEDULER;
    }

    public AudioPlayerManager getPLAYER_MANAGER() {
        return PLAYER_MANAGER;
    }

    public AudioPlayer getPLAYER() {
        return PLAYER;
    }

    public AudioProvider getPROVIDER() {
        return PROVIDER;
    }


    private void init() {
        // This is an optimization strategy that Discord4J can utilize.
        // It is not important to understand
        PLAYER_MANAGER.getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
    }
}
