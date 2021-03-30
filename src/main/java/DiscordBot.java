import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.voice.AudioProvider;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Map;
import java.util.Objects;

public class DiscordBot {
    // Creates AudioPlayer instances and translates URLs to AudioTrack instances
    final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    // Create an AudioPlayer so Discord4J can receive audio data
    final static AudioPlayer player = playerManager.createPlayer();
    private static final Map<String, IExecute> commands;
    // We will be creating LavaPlayerAudioProvider in the next step
    static AudioProvider provider = new LavaPlayer(player);


    static {
        commands = Commands.getCommands();
    }

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
// This is an optimization strategy that Discord4J can utilize.
// It is not important to understand
        playerManager.getConfiguration()
                .setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);

// Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager);

        GatewayDiscordClient client = DiscordClientBuilder.create(Objects.requireNonNull(dotenv.get("TOKEN")))
                .build()
                .login()
                .block();

        assert client != null;
        run(client);
    }

    private static void run(GatewayDiscordClient client) {

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> Events.onReady(event, client));

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> Events.onMessageCreated(event, commands));

        client.onDisconnect().block();
    }
}
