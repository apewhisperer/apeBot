import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Commands {

    public static Map<String, IExecute> getCommands() {

        final Map<String, IExecute> commands = new HashMap<>();

        commands.put("tip", message -> {
            if (message.getContent().length() > 4) {
                Objects.requireNonNull(message.getChannel().block())
                        .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                                .setTitle(message.getContent().substring(5).toUpperCase())
                                .setDescription(TooltipReader.getTooltip(message.getContent().substring(5)))).subscribe();
            } else {
                Objects.requireNonNull(message.getChannel().block())
                        .createMessage("name required").block();
            }
        });
        commands.put("d", message -> {
            Objects.requireNonNull(message.getChannel().block())
                    .createMessage(DiceRoller.rollDice(message.getContent())).block();
        });
        commands.put("wildsurge", message -> {
            Objects.requireNonNull(message.getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setDescription(WildMagicSurge.rollOnTable())).subscribe();
        });
        commands.put("bony", message -> {
            Objects.requireNonNull(message.getChannel().block())
                    .createMessage(BonusContent.bony()).block();
        });
        commands.put("help", message -> {
            Objects.requireNonNull(message.getChannel().block())
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle("Commands:")
                            .addField(Help.rollCode(), Help.rollHelp(), false)
                            .addField(Help.tipCode(), Help.tipHelp(), false)
                            .addField(Help.surgeCode(), Help.surgeHelp(), false)).subscribe();

        });
        commands.put("info", message -> {
            Objects.requireNonNull(message.getChannel().block())
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
