package functions;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import player.PlayerController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static functions.Help.*;

public interface CommandsInterface extends IssueInterface {

    static void useTts(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(new Consumer<MessageCreateSpec>() {
                    @Override
                    public void accept(MessageCreateSpec messageCreateSpec) {
                        messageCreateSpec.setTts(true);
                        messageCreateSpec.setContent("Nom nom nom");
                    }
                }).block();
    }

    static void loop(MessageCreateEvent event) {
        String repeatEmoji = "\uD83D\uDD01";
        String crossEmoji = "❎";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issueLoop(event, repeatEmoji, crossEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }


    static void fade(MessageCreateEvent event) {
        String checkEmoji = "✅";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issueFade(event, checkEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }


    static void printList(MessageCreateEvent event) {
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issuePrintList(event, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }


    static String getList(Map<Integer, AudioTrack> list, PlayerController playerController) {
        Map<Integer, AudioTrack> embedList = list;
        String stringList = "";
        int position = 1;
        for (Map.Entry<Integer, AudioTrack> entry : embedList.entrySet()) {
            stringList = stringList.concat(String.valueOf(position)).concat(". ");
            if ((position - 1) == playerController.getScheduler().getPosition()) {
                stringList = stringList.concat("\u25B6 ");
            }
            stringList = stringList.concat(entry.getValue().getInfo().title).concat("\n");
            position++;
        }
        return stringList;
    }

    static void changeVolume(MessageCreateEvent event) {
        String plusEmoji = "\uD83D\uDD3C";
        String minusEmoji = "\uD83D\uDD3D";
        String equalEmoji = "↕";
        final String CONTENT = event.getMessage().getContent();
        final List<String> COMMAND = Arrays.asList(CONTENT.split(" "));
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issueChangeVolume(event, plusEmoji, minusEmoji, equalEmoji, COMMAND, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }


    static void printInfo(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle(Info.gitHub())
                        .setUrl("https://github.com/apewhisperer/apeBot")
                        .setFooter("© " + Info.author() + " " + "email: " + Info.email(), "https://discord.com/assets/6debd47ed13483642cf09e832ed0bc1b.png")
                        .setAuthor("4pe wh1sperer", "https://discordapp.com/users/253270489173196800", "https://cdn.discordapp.com/avatars/253270489173196800/7e9d8a6f49d7b4f90c514cbf07c92cbd.webp?size=128")
                        .setDescription(Info.description())).subscribe();
    }

    static void help(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setTitle("Commands:")
                        .addField(rollCode(), rollInfo(), false)
                        .addField(tipCode(), tipInfo(), false)
                        .addField(playCode(), playInfo(), false)
                        .addField(pauseCode(), pauseInfo(), false)
                        .addField(volumeCode(), volumeInfo(), false)
                        .addField(surgeCode(), surgeInfo(), false)).subscribe();
    }

    static void surge(MessageCreateEvent event) {
        Log.registerEvent(event.getMessage().getContent());
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                        .setDescription(ExtendedFunctions.wildMagicSurge())).subscribe();
    }

    static void roll(MessageCreateEvent event) {
        String command = DiceRoller.roll(event.getMessage().getContent());
        Log.registerEvent(event.getMessage().getContent());
        if (event.getMessage().getContent().length() > 2) {
            IssueInterface.issueRoll(event, command);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("value required").block();
        }
    }

    static void tip(MessageCreateEvent event) {
        String command = TooltipReader.getTooltip(event.getMessage().getContent().substring(5));
        Log.registerEvent(event.getMessage().getContent());
        if (event.getMessage().getContent().length() > 5) {
            IssueInterface.issueTip(event, command);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("name required").block();
        }
    }

    static void pause(MessageCreateEvent event) {
        String pauseEmoji = "⏸";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issuePause(event, pauseEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static void play(MessageCreateEvent event) {
        String playEmoji = "▶";
        final String CONTENT = event.getMessage().getContent();
        final List<String> COMMAND = Arrays.asList(CONTENT.split(" "));
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        join(event, true);
        if (CHANNEL != null) {
            IssueInterface.issuePlay(event, playEmoji, COMMAND, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static void join(MessageCreateEvent event, boolean isPlay) {
        PlayerController playerController = new PlayerController();
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        if (!isPlay) {
            Log.registerEvent(event.getMessage().getContent());
        }
        if (CHANNEL != null) {
            IssueInterface.issueJoin(event, playerController, CHANNEL);
        }
    }

    static void quit(MessageCreateEvent event) {
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issueQuit(CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static VoiceChannel getChannel(Member member) {
        if (member != null) {
            final VoiceState VOICE_STATE = member.getVoiceState().block();
            if (VOICE_STATE != null) {
                return VOICE_STATE.getChannel().block();
            }
        }
        return null;
    }

    static void stop(MessageCreateEvent event) {
        String stopEmoji = "⏹";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        if (CHANNEL != null) {
            IssueInterface.issueStop(event, stopEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static void queue(MessageCreateEvent event) {
        String checkEmoji = "✅";
        final String CONTENT = event.getMessage().getContent();
        final List<String> COMMAND = Arrays.asList(CONTENT.split(" "));
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        join(event, true);
        if (CHANNEL != null) {
            IssueInterface.issueQueue(event, checkEmoji, COMMAND, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static void playNext(MessageCreateEvent event) {
        String nextEmoji = "⏩";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        join(event, true);
        if (CHANNEL != null) {
            IssueInterface.issuePlayNext(event, nextEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }

    static void playLast(MessageCreateEvent event) {
        String lastEmoji = "⏪";
        final Member MEMBER = event.getMember().orElse(null);
        final VoiceChannel CHANNEL = getChannel(MEMBER);
        Log.registerEvent(event.getMessage().getContent());
        join(event, true);
        if (CHANNEL != null) {
            IssueInterface.issuePlayLast(event, lastEmoji, CHANNEL);
        } else {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage("join voice channel first").subscribe();
        }
    }
}
