package commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import player.Player;
import player.TrackScheduler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public interface ICommands {

    static void banana(MessageCreateEvent event) {

        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(new Consumer<MessageCreateSpec>() {
                    @Override
                    public void accept(MessageCreateSpec messageCreateSpec) {
                        messageCreateSpec.setTts(true);
                        messageCreateSpec.setContent("go eat a banana");
                    }
                }).block();
    }

    static void changeVolume(String plusEmoji, String minusEmoji, String equalEmoji, MessageCreateEvent event) {
        final String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));
        String emoji;

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : Commands.channelPlayerMap.entrySet()) {
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

    static void printInfo(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle(Info.gitHub())
                        .setUrl("https://github.com/apewhisperer/apeBot")
                        .setFooter("© " + Info.author() + " " + "email: " + Info.email(), "https://discord.com/assets/6debd47ed13483642cf09e832ed0bc1b.png")
                        .setAuthor("4pe wh1sperer", "https://discordapp.com/users/253270489173196800", "https://cdn.discordapp.com/avatars/253270489173196800/7e9d8a6f49d7b4f90c514cbf07c92cbd.webp?size=128")
                        .setDescription(Info.description())).subscribe();
    }

    static void help(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle("Commands:")
                        .addField(Help.rollCode(), Help.rollInfo(), false)
                        .addField(Help.tipCode(), Help.tipInfo(), false)
                        .addField(Help.playCode(), Help.playInfo(), false)
                        .addField(Help.pauseCode(), Help.pauseInfo(), false)
                        .addField(Help.volumeCode(), Help.volumeInfo(), false)
                        .addField(Help.surgeCode(), Help.surgeInfo(), false)).subscribe();
    }

    static void printBony(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(BonusContent.bony()).block();
    }

    static void surge(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setDescription(BonusContent.wildMagicSurge())).subscribe();
    }

    static void roll(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        String command = DiceRoller.roll(event.getMessage().getContent().substring(1));

        if (event.getMessage().getContent().length() > 2) {
            if (command.length() > Message.MAX_CONTENT_LENGTH) {
                convertToFile(event, command);
            } else {
                Objects.requireNonNull(event.getMessage().getChannel().block())
                        .createMessage(command).block();
            }
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("value required").block();
        }
    }

    static void tip(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        String command = TooltipReader.getTooltip(event.getMessage().getContent().substring(5));

        if (event.getMessage().getContent().length() > 5) {
            if (command.length() > Message.MAX_CONTENT_LENGTH) {
                convertToFile(event, command);
            } else {
                Objects.requireNonNull(event.getMessage().getChannel().block())
                        .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                                .setTitle(event.getMessage().getContent().substring(5).toUpperCase())
                                .setDescription(command)).subscribe();
            }
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("name required").block();
        }
    }

    static void pause(String stopEmoji, MessageCreateEvent event) {

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : Commands.channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    if (!entry.getValue().getPlayer().isPaused()) {
                        entry.getValue().getPlayer().setPaused(true);
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(stopEmoji))).block();
                    }
                }
            }
        }
    }

    static void play(String playEmoji, MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());

        join(event);
        final String content = event.getMessage().getContent();
        final List<String> command = Arrays.asList(content.split(" "));

        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : Commands.channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    if (command.size() == 1) {
                        TrackScheduler scheduler = entry.getValue().getScheduler();
                        if (entry.getValue().getPlayer().isPaused()) {
                            entry.getValue().getPlayer().setPaused(false);
                        }
                        if (scheduler.isStopped) {
                            System.out.println("unstop");
                            scheduler.setStopped(false);
                            entry.getValue().getPlayer().playTrack(scheduler.getList().get(scheduler.getPosition()).makeClone());
                        }
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

    static void join(MessageCreateEvent event) {
        Player player = new Player();
        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            if (!Commands.channelPlayerMap.containsKey(channel)) {
                Commands.channelPlayerMap.put(channel, player);
            }
            channel.join(spec -> spec.setProvider(player.getProvider())).block();
        }
    }

    static void quit(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : Commands.channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    entry.getValue().getPlayer().destroy();
                    Commands.channelPlayerMap.remove(entry.getKey(), entry.getValue());
                    System.out.println("player destroyed");
                    channel.sendDisconnectVoiceState().block();
                }
            }
        }
    }

    static VoiceChannel getChannel(Member member) {
        if (member != null) {
            final VoiceState voiceState = member.getVoiceState().block();
            if (voiceState != null) {
                return voiceState.getChannel().block();
            }
        }
        return null;
    }

    static void convertToFile(MessageCreateEvent event, String command) {
        InputStream targetStream = new ByteArrayInputStream(command.getBytes());

        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(new Consumer<MessageCreateSpec>() {
                    @Override
                    public void accept(MessageCreateSpec messageCreateSpec) {
                        messageCreateSpec.addFile("message.txt", targetStream);
                    }
                }).block();
    }

    static void stop(String stopEmoji, MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        VoiceChannel channel = getChannel(member);

        if (channel != null) {
            for (Map.Entry<VoiceChannel, Player> entry : Commands.channelPlayerMap.entrySet()) {
                if (entry.getKey().equals(channel)) {
                    entry.getValue().getPlayer().stopTrack();
                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(stopEmoji))).block();
                }
            }
        }
    }
}
