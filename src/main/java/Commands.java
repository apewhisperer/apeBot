import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.rest.util.Color;

import java.util.*;

public class Commands {

    public static Map<String, IExecute> getCommands() {

        final Map<String, IExecute> commands = new HashMap<>();

        final TrackScheduler scheduler = new TrackScheduler(DiscordBot.player);
        commands.put("join", event -> {
            final Member member = event.getMember().orElse(null);

            if (member != null) {
                final VoiceState voiceState = member.getVoiceState().block();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel().block();
                    if (channel != null) {
                        // join returns a VoiceConnection which would be required if we were
                        // adding disconnection features, but for now we are just ignoring it.
                        channel.join(spec -> spec.setProvider(DiscordBot.provider)).block();
                    }
                }
            }
        });
        commands.put("play", event -> {
            Log.registerEvent("!play ", event.getMessage().getContent());
            final String content = event.getMessage().getContent();
            final List<String> command = Arrays.asList(content.split(" "));
            DiscordBot.playerManager.loadItem(command.get(1), scheduler);
        });
        commands.put("tip", event -> {
            Log.registerEvent("!tip ", event.getMessage().getContent());
            if (event.getMessage().getContent().length() > 4) {
                Objects.requireNonNull(event.getMessage().getChannel().block())
                        .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                                .setTitle(event.getMessage().getContent().substring(5).toUpperCase())
                                .setDescription(TooltipReader.getTooltip(event.getMessage().getContent().substring(5)))).subscribe();
            } else {
                Objects.requireNonNull(event.getMessage().getChannel().block())
                        .createMessage("name required").block();
            }
        });
        commands.put("d", event -> {
            Log.registerEvent("!d", event.getMessage().getContent());
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage(DiceRoller.rollDice(event.getMessage().getContent())).block();
        });
        commands.put("wildsurge", event -> {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setDescription(WildMagicSurge.rollOnTable())).subscribe();
        });
        commands.put("bony", event -> {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createMessage(BonusContent.bony()).block();
        });
        commands.put("help", event -> {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle("Commands:")
                            .addField(Help.rollCode(), Help.rollHelp(), false)
                            .addField(Help.tipCode(), Help.tipHelp(), false)
                            .addField(Help.surgeCode(), Help.surgeHelp(), false)).subscribe();

        });
        commands.put("info", event -> {
            Objects.requireNonNull(event.getMessage().getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle(Info.gitHub())
                            .setUrl("https://github.com/apewhisperer/apeBot")
                            .setFooter("© " + Info.author() + " " + "email: " + Info.email(), "https://discord.com/assets/6debd47ed13483642cf09e832ed0bc1b.png")
                            .setAuthor("4pe wh1sperer", "https://discordapp.com/users/253270489173196800", "https://cdn.discordapp.com/avatars/253270489173196800/7e9d8a6f49d7b4f90c514cbf07c92cbd.webp?size=128")
                            .setDescription(Info.description())).subscribe();

        });
        return commands;
    }
}
