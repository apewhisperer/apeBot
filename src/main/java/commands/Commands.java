package commands;

import core.IExecute;
import discord4j.core.object.entity.channel.VoiceChannel;
import player.PlayerController;

import java.util.HashMap;
import java.util.Map;

public class Commands implements ICommands {

    static Map<VoiceChannel, PlayerController> channelPlayerMap = new HashMap<>();

    public static Map<String, IExecute> getCommands() {
        final Map<String, IExecute> COMMANDS = new HashMap<>();
        String playEmoji = "\u25B6";
        String pauseEmoji = "\u23F8";
        String plusEmoji = "\u2B06";
        String minusEmoji = "\u2B07";
        String equalEmoji = "\u2195";
        String stopEmoji = "\u23F9";
        COMMANDS.put("banana", event -> {
            ICommands.banana(event);
        });
        COMMANDS.put("volume", event -> {
            ICommands.changeVolume(plusEmoji, minusEmoji, equalEmoji, event);
        });
        COMMANDS.put("join", event -> {
            ICommands.join(event);
        });
        COMMANDS.put("leave", event -> {
            ICommands.quit(event);
        });
        COMMANDS.put("quit", event -> {
            ICommands.quit(event);
        });
        COMMANDS.put("play", event -> {
            ICommands.play(playEmoji, event);
        });
        COMMANDS.put("stop", event -> {
            ICommands.stop(stopEmoji, event);
        });
        COMMANDS.put("pause", event -> {
            ICommands.pause(pauseEmoji, event);
        });
        COMMANDS.put("tip", event -> {
            ICommands.tip(event);
        });
        COMMANDS.put("d", event -> {
            ICommands.roll(event);
        });
        COMMANDS.put("surge", event -> {
            ICommands.surge(event);
        });
        COMMANDS.put("bony", event -> {
            ICommands.printBony(event);
        });
        COMMANDS.put("help", event -> {
            ICommands.help(event);
        });
        COMMANDS.put("info", event -> {
            ICommands.printInfo(event);
        });
        return COMMANDS;
    }
}
