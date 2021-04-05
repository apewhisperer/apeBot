import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;

import java.util.*;

public class Commands {

    static Map<VoiceChannel, Player> channelPlayerMap = new HashMap<>();

    public static Map<String, IExecute> getCommands() {

        final Map<String, IExecute> commands = new HashMap<>();
        String playEmoji = "\u25B6";
        String stopEmoji = "\u23F8";
        String plusEmoji = "\u2B06";
        String minusEmoji = "\u2B07";
        String equalEmoji = "\u2195";

        commands.put("volume", event -> {
            changeVolume(plusEmoji, minusEmoji, equalEmoji, event);
        });
        commands.put("join", event -> {
            join(event);
        });
        commands.put("leave", event -> {
            quit(event);
        });
        commands.put("quit", event -> {
            quit(event);
        });
        commands.put("play", event -> {
            play(playEmoji, event);
        });
        commands.put("stop", event -> {
            pause(stopEmoji, event);
        });
        commands.put("pause", event -> {
            pause(stopEmoji, event);
        });
        commands.put("tip", event -> {
            tip(event);
        });
        commands.put("d", event -> {
            diceRoll(event);
        });
        commands.put("surge", event -> {
            surge(event);
        });
        commands.put("bony", event -> {
            printBony(event);
        });
        commands.put("help", event -> {
            help(event);
        });
        commands.put("info", event -> {
            printInfo(event);
        });
        return commands;
    }

    private static void changeVolume(String plusEmoji, String minusEmoji, String equalEmoji, MessageCreateEvent event) {
        final String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));
        String emoji;

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    if (command.size() > 1) {
                        int levelAfter = Integer.parseInt(command.get(1));
                        if (levelAfter >= 0 && levelAfter <= 100) {
                            int current = entry.getValue().getPlayer().getVolume();
                            if (levelAfter == current) {
                                entry.getValue().getPlayer().setVolume(levelAfter);
                                emoji = equalEmoji;
                            } else if (levelAfter > current) {
                                entry.getValue().getPlayer().setVolume(levelAfter);
                                emoji = plusEmoji;
                            } else {
                                entry.getValue().getPlayer().setVolume(levelAfter);
                                emoji = minusEmoji;
                            }
                            Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(emoji))).block();
                        } else {
                            Objects.requireNonNull(event.getMessage().getChannel().block())
                                    .createMessage("volume must be within 0-100 range").block();
                        }
                    }
                }
            }
        }
    }

    private static void printInfo(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle(Info.gitHub())
                        .setUrl("https://github.com/apewhisperer/apeBot")
                        .setFooter("© " + Info.author() + " " + "email: " + Info.email(), "https://discord.com/assets/6debd47ed13483642cf09e832ed0bc1b.png")
                        .setAuthor("4pe wh1sperer", "https://discordapp.com/users/253270489173196800", "https://cdn.discordapp.com/avatars/253270489173196800/7e9d8a6f49d7b4f90c514cbf07c92cbd.webp?size=128")
                        .setDescription(Info.description())).subscribe();
    }

    private static void help(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle("Commands:")
                        .addField(Help.rollCode(), Help.rollHelp(), false)
                        .addField(Help.tipCode(), Help.tipHelp(), false)
                        .addField(Help.surgeCode(), Help.surgeHelp(), false)).subscribe();
    }

    private static void printBony(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(BonusContent.bony()).block();
    }

    private static void surge(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setDescription(WildMagicSurge.rollOnTable())).subscribe();
    }

    private static void diceRoll(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(DiceRoller.rollDice(event.getMessage().getContent())).block();
    }

    private static void tip(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        if (event.getMessage().getContent().length() > 4) {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle(event.getMessage().getContent().substring(5).toUpperCase())
                            .setDescription(TooltipReader.getTooltip(event.getMessage().getContent().substring(5)))).subscribe();
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("name required").block();
        }
    }

    private static void pause(String stopEmoji, MessageCreateEvent event) {

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    if (!entry.getValue().getPlayer().isPaused()) {
                        entry.getValue().getPlayer().setPaused(true);
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(stopEmoji))).block();
                    }
                }
            }
        }
    }

    private static void play(String playEmoji, MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());

        join(event);
        final String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    if (command.size() == 1 && entry.getValue().getPlayer().isPaused()) {
                        entry.getValue().getPlayer().setPaused(false);
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                    } else if (command.size() == 2) {
                        if (entry.getValue().getPlayer().isPaused()) {
                            entry.getValue().getPlayer().setPaused(false);
                        }
                        entry.getValue().getPlayerManager().loadItem(command.get(1), entry.getValue().getScheduler());
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                    }
                }
            }
        }
    }

    private static void join(MessageCreateEvent event) {
        Player player = new Player();
        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            if (!channelPlayerMap.containsKey(channel)) {
                channelPlayerMap.put(channel, player);
            }
            channel.join(spec -> spec.setProvider(player.getProvider())).block();
        }
    }

    private static VoiceChannel getChannel(Member member) {
        if (member != null) {
            final VoiceState voiceState = member.getVoiceState().block();
            if (voiceState != null) {
                return voiceState.getChannel().block();
            }
        }
        return null;
    }

    private static void quit(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    entry.getValue().getPlayer().destroy();
                    channel.sendDisconnectVoiceState().block();
                }
            }
        }
    }
}
