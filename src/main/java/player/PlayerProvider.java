package player;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public final class PlayerProvider extends AudioProvider {

    private final AudioPlayer PLAYER;
    private final MutableAudioFrame FRAME = new MutableAudioFrame();

    public PlayerProvider(final AudioPlayer PLAYER) {
        // Allocate a ByteBuffer for Discord4J's AudioProvider to hold audio data
        // for Discord
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
        // Set LavaPlayer's MutableAudioFrame to use the same buffer as the one we
        // just allocated
        FRAME.setBuffer(getBuffer());
        this.PLAYER = PLAYER;
    }

    @Override
    public boolean provide() {
        // AudioPlayer writes audio data to its AudioFrame
        final boolean didProvide = PLAYER.provide(FRAME);
        // If audio was provided, flip from write-mode to read-mode
        if (didProvide) {
            getBuffer().flip();
        }
        return didProvide;
    }
}