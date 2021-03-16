import discord4j.rest.util.Color;

import java.util.HashMap;
import java.util.Map;

public class Commands {

    public static Map<String, Command> getCommands() {

        final Map<String, Command> commands = new HashMap<>();

        commands.put("tip", event -> {
            if (event.getMessage().getContent().length() > 4) {
                event.getMessage()
                        .getChannel().block()
                        .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                                .setTitle(event.getMessage().getContent().substring(5).toUpperCase())
                                .setDescription(TooltipReader.getTooltip(event.getMessage().getContent().substring(5)))).subscribe();
            } else {
                event.getMessage()
                        .getChannel().block()
                        .createMessage("name required").block();
            }
        });
        commands.put("d", event -> {
            if (event.getMessage().getContent().length() > 2) {
                event.getMessage()
                        .getChannel().block()
                        .createMessage(DiceRoller.rolld20(event.getMessage().getContent().substring(2))).block();
            } else {
                event.getMessage()
                        .getChannel().block()
                        .createMessage("value required").block();
            }
        });
        commands.put("wildsurge", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setDescription(WildMagicSurge.rollOnTable())).subscribe();
        });
        commands.put("bony", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createMessage(BonusContent.bony()).block();
        });
        commands.put("esteban", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createMessage(BonusContent.esteban()).block();
        });
        commands.put("help", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createEmbed(embed -> embed.setColor(Color.DARK_GOLDENROD)
                            .setTitle("Commands:")
                            .addField(Help.rollCode(), Help.rollHelp(), false)
                            .addField(Help.tipCode(), Help.tipHelp(), false)
                            .addField(Help.surgeCode(), Help.surgeHelp(), false)).subscribe();

        });
        commands.put("hes kinda dumb", event -> {
            event.getMessage()
                    .getChannel().block()
                    .createMessage(BonusContent.dumb()).block();
        });
        return commands;
    }
}
