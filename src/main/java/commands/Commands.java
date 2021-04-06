package commands;

import core.IExecute;
import discord4j.core.object.entity.channel.VoiceChannel;
import player.Player;

import java.util.HashMap;
import java.util.Map;

public class Commands implements ICommands {

    static Map<VoiceChannel, Player> channelPlayerMap = new HashMap<>();

    public static Map<String, IExecute> getCommands() {

        final Map<String, IExecute> commands = new HashMap<>();
        String playEmoji = "\u25B6";
        String stopEmoji = "\u23F8";
        String plusEmoji = "\u2B06";
        String minusEmoji = "\u2B07";
        String equalEmoji = "\u2195";

        commands.put("volume", event -> {
            ICommands.changeVolume(plusEmoji, minusEmoji, equalEmoji, event);
        });
        commands.put("join", event -> {
            ICommands.join(event);
        });
        commands.put("leave", event -> {
            ICommands.quit(event);
        });
        commands.put("quit", event -> {
            ICommands.quit(event);
        });
        commands.put("play", event -> {
            ICommands.play(playEmoji, event);
        });
        commands.put("stop", event -> {
            ICommands.pause(stopEmoji, event);
        });
        commands.put("pause", event -> {
            ICommands.pause(stopEmoji, event);
        });
        commands.put("tip", event -> {
            ICommands.tip(event);
        });
        commands.put("d", event -> {
            ICommands.roll(event);
        });
        commands.put("surge", event -> {
            ICommands.surge(event);
        });
        commands.put("bony", event -> {
            ICommands.printBony(event);
        });
        commands.put("help", event -> {
            ICommands.help(event);
        });
        commands.put("info", event -> {
            ICommands.printInfo(event);
        });
        return commands;
    }
}
