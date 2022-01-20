package functions;

import bot.ExecuteInterface;
import discord4j.core.object.entity.channel.VoiceChannel;
import player.PlayerController;

import java.util.HashMap;
import java.util.Map;

public class Commands implements CommandsInterface {

    public static Map<String, ExecuteInterface> getCommands() {
        final Map<String, ExecuteInterface> COMMANDS = new HashMap<>();
        COMMANDS.put("banana", event -> {
            CommandsInterface.useTts(event);
        });
        COMMANDS.put("loop", event -> {
            CommandsInterface.loop(event);
        });
        COMMANDS.put("fade", event -> {
            CommandsInterface.fade(event);
        });
        COMMANDS.put("list", event -> {
            CommandsInterface.printList(event);
        });
        COMMANDS.put("volume", event -> {
            CommandsInterface.changeVolume(event);
        });
        COMMANDS.put("vol", event -> {
            CommandsInterface.changeVolume(event);
        });
        COMMANDS.put("join", event -> {
            CommandsInterface.join(event, false);
        });
        COMMANDS.put("leave", event -> {
            CommandsInterface.quit(event);
        });
        COMMANDS.put("quit", event -> {
            CommandsInterface.quit(event);
        });
        COMMANDS.put("play", event -> {
            CommandsInterface.play(event);
        });
        COMMANDS.put("stop", event -> {
            CommandsInterface.stop(event);
        });
        COMMANDS.put("pause", event -> {
            CommandsInterface.pause(event);
        });
        COMMANDS.put("tip", event -> {
            CommandsInterface.tip(event);
        });
        COMMANDS.put("diceroll", event -> {
            CommandsInterface.roll(event);
        });
        COMMANDS.put("surge", event -> {
            CommandsInterface.surge(event);
        });
        COMMANDS.put("help", event -> {
            CommandsInterface.help(event);
        });
        COMMANDS.put("info", event -> {
            CommandsInterface.printInfo(event);
        });
        COMMANDS.put("queue", event -> {
            CommandsInterface.queue(event);
        });
        COMMANDS.put("next", event -> {
            CommandsInterface.playNext(event);
        });
        COMMANDS.put("last", event -> {
            CommandsInterface.playLast(event);
        });
        return COMMANDS;
    }
}
