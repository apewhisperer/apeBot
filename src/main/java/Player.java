import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.voice.AudioProvider;

public class Player {

    // Creates AudioPlayer instances and translates URLs to AudioTrack instances
    private final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    // Create an AudioPlayer so Discord4J can receive audio data
    private final AudioPlayer player = playerManager.createPlayer();
    // We will be creating LavaPlayerAudioProvider in the next step
    private AudioProvider provider = new PlayerProvider(player);

    public Player() {
        init();
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioProvider getProvider() {
        return provider;
    }

    // This is an optimization strategy that Discord4J can utilize.
// It is not important to understand
    private void init() {
        playerManager.getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

// Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager);
    }
}
