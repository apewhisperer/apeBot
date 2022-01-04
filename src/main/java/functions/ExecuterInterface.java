package functions;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import functions.threads.FadeThread;
import functions.threads.LoadThread;
import player.PlayerController;
import player.TrackScheduler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public interface ExecuterInterface {
    static void executeLoop(MessageCreateEvent event, String repeatEmoji, String crossEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                TrackScheduler scheduler = entry.getValue().getScheduler();
                if (scheduler.isLooped()) {
                    scheduler.setLooped(false);
                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(crossEmoji))).block();
                    return;
                } else {
                    scheduler.setLooped(true);
                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(repeatEmoji))).block();
                    return;
                }
            }
        }
    }

    static void executeFade(MessageCreateEvent event, String checkEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                if (!entry.getValue().getScheduler().isFading()) {
                    FadeThread fadeThread = new FadeThread(entry.getValue());
                    fadeThread.start();
                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(checkEmoji))).block();
                    return;
                } else {
                    Objects.requireNonNull(event.getMessage().getChannel().block())
                            .createMessage("fade is already issued!").subscribe();
                    return;
                }
            }
        }
    }

    static void executePrintList(MessageCreateEvent event, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                Map<Integer, AudioTrack> list = entry.getValue().getScheduler().getList();
                String stringList = CommandsInterface.getList(list, entry.getValue());
                if (stringList.length() > Message.MAX_CONTENT_LENGTH) {
                    convertToFile(event, stringList);
                    return;
                } else {
                    Objects.requireNonNull(event.getMessage().getChannel().block())
                            .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                                    .setDescription(CommandsInterface.getList(list, entry.getValue()))).subscribe();
                    return;
                }
            }
        }
    }

    static void executeChangeVolume(MessageCreateEvent event, String plusEmoji, String minusEmoji, String equalEmoji, List<String> COMMAND, VoiceChannel CHANNEL) {
        String emoji;
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                if (COMMAND.size() > 1) {
                    int levelAfter = Integer.parseInt(COMMAND.get(1));
                    if (levelAfter >= 0 && levelAfter <= 100) {
                        AudioPlayer player = entry.getValue().getPlayer();
                        int current = player.getVolume();
                        if (levelAfter == current) {
                            player.setVolume(levelAfter);
                            emoji = equalEmoji;
                        } else if (levelAfter > current) {
                            player.setVolume(levelAfter);
                            emoji = plusEmoji;
                        } else {
                            player.setVolume(levelAfter);
                            emoji = minusEmoji;
                        }
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(emoji))).block();
                        return;
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("volume must be within 0-100 range").block();
                        return;
                    }
                }
            }
        }
    }

    static void executeRoll(MessageCreateEvent event, String command) {
        if (command.length() > Message.MAX_CONTENT_LENGTH) {
            convertToFile(event, command);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage(command).block();
        }
    }

    static void executeTip(MessageCreateEvent event, String command) {
        if (command.length() > Message.MAX_CONTENT_LENGTH) {
            convertToFile(event, command);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle(event.getMessage().getContent().substring(5).toUpperCase())
                            .setDescription(command)).subscribe();
        }
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

    static void executePause(MessageCreateEvent event, String pauseEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                if (!entry.getValue().getPlayer().isPaused()) {
                    entry.getValue().getPlayer().setPaused(true);
                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(pauseEmoji))).block();
                    return;
                }
            }
        }
    }

    static void executePlay(MessageCreateEvent event, String playEmoji, List<String> COMMAND, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                TrackScheduler scheduler = entry.getValue().getScheduler();
                AudioPlayer player = entry.getValue().getPlayer();
                if (COMMAND.size() == 1) {
                    if (scheduler.getList().size() > 0) {
                        if (player.isPaused()) {
                            player.setPaused(false);
                            if (scheduler.isStopped()) {
                                scheduler.setStopped(false);
                                if (scheduler.getList().size() > 0) {
                                    player.playTrack(scheduler.getList().get(scheduler.getPosition()).makeClone());
                                    Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                                    return;
                                }
                            }
                            Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                            return;
                        } else if (scheduler.isStopped()) {
                            scheduler.setStopped(false);
                            if (scheduler.getList().size() > 0) {
                                player.playTrack(scheduler.getList().get(scheduler.getPosition()).makeClone());
                                Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                                return;
                            }
                        } else if (player.getPlayingTrack() == null) {
                            player.playTrack(scheduler.getList().get(scheduler.getPosition()));
                            Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                            return;
                        }
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("queue is empty!").subscribe();
                        return;
                    }
                } else if (COMMAND.size() == 2) {
                    if (player.isPaused()) {
                        player.setPaused(false);
                    }
                    initLoadThread(COMMAND.get(1), entry.getValue(), true);
                    if (!scheduler.isFailed()) {
                        player.playTrack(scheduler.getList().get(scheduler.getPosition()));
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(playEmoji))).block();
                        return;
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("invalid link").subscribe();
                        return;
                    }
                }
            }
        }
    }

    static void initLoadThread(String link, PlayerController playerController, boolean isPositioned) {
        LoadThread loadThread = new LoadThread(link, playerController, isPositioned);
        loadThread.start();
        try {
            loadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        playerController.getScheduler().setPositioned(false);
        playerController.getScheduler().setLoaded(false);
    }

    static void executeJoin(MessageCreateEvent event, PlayerController playerController, VoiceChannel CHANNEL) {
        if (!Commands.channelPlayerMap.containsKey(CHANNEL)) {
            try {
                Commands.channelPlayerMap.put(CHANNEL, playerController);
                CHANNEL.join(spec -> spec.setProvider(playerController.getProvider())).block();
            } catch (Exception e) {
                Objects.requireNonNull(event.getMessage().getChannel().block())
                        .createMessage("unable to join").subscribe();
            }
        }
    }

    static void executeQuit(VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                entry.getValue().getPlayer().destroy();
                Commands.channelPlayerMap.remove(entry.getKey(), entry.getValue());
                CHANNEL.sendDisconnectVoiceState().block();
                return;
            }
        }
    }

    static void executeStop(MessageCreateEvent event, String stopEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                entry.getValue().getPlayer().stopTrack();
                Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(stopEmoji))).block();
                return;
            }
        }
    }

    static void executeQueue(MessageCreateEvent event, String checkEmoji, List<String> COMMAND, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                TrackScheduler scheduler = entry.getValue().getScheduler();
                if (COMMAND.size() == 2) {
                    initLoadThread(COMMAND.get(1), entry.getValue(), false);
                    if (!scheduler.isFailed()) {
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(checkEmoji))).block();
                        return;
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("invalid link").subscribe();
                        return;
                    }
                }
            }
        }
    }

    static void executePlayNext(MessageCreateEvent event, String nextEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                TrackScheduler scheduler = entry.getValue().getScheduler();
                AudioPlayer player = entry.getValue().getPlayer();
                if (scheduler.getList().size() > 0) {
                    if (scheduler.getList().size() > scheduler.getPosition() + 1) {
                        scheduler.setPosition(scheduler.getPosition() + 1);
                        player.playTrack(scheduler.getList().get(scheduler.getPosition()).makeClone());
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(nextEmoji))).block();
                        return;
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("queue end!").subscribe();
                        return;
                    }
                } else {
                    Objects.requireNonNull(event.getMessage().getChannel().block())
                            .createMessage("queue is empty!").subscribe();
                    return;
                }
            }
        }
    }

    static void executePlayLast(MessageCreateEvent event, String lastEmoji, VoiceChannel CHANNEL) {
        for (Map.Entry<VoiceChannel, PlayerController> entry : Commands.channelPlayerMap.entrySet()) {
            if (entry.getKey().equals(CHANNEL)) {
                TrackScheduler scheduler = entry.getValue().getScheduler();
                AudioPlayer player = entry.getValue().getPlayer();
                if (scheduler.getList().size() > 0) {
                    if (scheduler.getPosition() - 1 >= 0) {
                        scheduler.setPosition(scheduler.getPosition() - 1);
                        player.playTrack(scheduler.getList().get(scheduler.getPosition()).makeClone());
                        Objects.requireNonNull(event.getMessage().addReaction(ReactionEmoji.unicode(lastEmoji))).block();
                        return;
                    } else {
                        Objects.requireNonNull(event.getMessage().getChannel().block())
                                .createMessage("queue beginning!").subscribe();
                        return;
                    }
                } else {
                    Objects.requireNonNull(event.getMessage().getChannel().block())
                            .createMessage("queue is empty!").subscribe();
                    return;
                }
            }
        }
    }
}
