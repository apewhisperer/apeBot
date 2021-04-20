package actions;

import core.IExecute;
import discord4j.core.object.entity.channel.VoiceChannel;
import player.PlayerController;

import java.util.HashMap;
import java.util.Map;

public class Commands implements ICommands {

    static Map<VoiceChannel, PlayerController> channelPlayerMap = new HashMap<>();

    public static Map<String, IExecute> getCommands() {
        final Map<String, IExecute> COMMANDS = new HashMap<>();
        COMMANDS.put("banana", event -> {
            ICommands.useTts(event);
        });
        COMMANDS.put("bony", event -> {
            ICommands.printBony(event);
        });
        COMMANDS.put("loop", event -> {
            ICommands.loop(event);
        });
        COMMANDS.put("fade", event -> {
            ICommands.fade(event);
        });
        COMMANDS.put("list", event -> {
            ICommands.printList(event);
        });
        COMMANDS.put("volume", event -> {
            ICommands.changeVolume(event);
        });
        COMMANDS.put("vol", event -> {
            ICommands.changeVolume(event);
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
            ICommands.play(event);
        });
        COMMANDS.put("stop", event -> {
            ICommands.stop(event);
        });
        COMMANDS.put("pause", event -> {
            ICommands.pause(event);
        });
        COMMANDS.put("tip", event -> {
            ICommands.tip(event);
        });
        COMMANDS.put("diceroll", event -> {
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
            ICommands.queue(event);
        });
        COMMANDS.put("next", event -> {
            ICommands.playNext(event);
        });
        COMMANDS.put("last", event -> {
            ICommands.playLast(event);
        });
        return COMMANDS;
    }
}
