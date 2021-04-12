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
        String checkEmoji = "\u2705";
        String nextEmoji = "\u23ED";
        String lastEmoji = "\u23EE";
        COMMANDS.put("banana", event -> {
            ICommands.useTts(event);
        });
        COMMANDS.put("bony", event -> {
            ICommands.printBony(event);
        });
        COMMANDS.put("list", event -> {
            ICommands.printList(event);
        });
        COMMANDS.put("volume", event -> {
            ICommands.changeVolume(plusEmoji, minusEmoji, equalEmoji, event);
        });
        COMMANDS.put("vol", event -> {
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
        COMMANDS.put("p", event -> {
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
        COMMANDS.put("help", event -> {
            ICommands.help(event);
        });
        COMMANDS.put("info", event -> {
            ICommands.printInfo(event);
        });
        COMMANDS.put("queue", event -> {
            ICommands.queue(checkEmoji, event);
        });
        COMMANDS.put("q", event -> {
            ICommands.queue(checkEmoji, event);
        });
        COMMANDS.put("next", event -> {
            ICommands.playNext(nextEmoji, event);
        });
        COMMANDS.put("n", event -> {
            ICommands.playNext(nextEmoji, event);
        });
        COMMANDS.put("last", event -> {
            ICommands.playLast(lastEmoji, event);
        });
        COMMANDS.put("l", event -> {
            ICommands.playLast(lastEmoji, event);
        });
        return COMMANDS;
    }
}
