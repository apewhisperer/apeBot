package core;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

import java.util.Map;
import java.util.Objects;

public class Events {

    public static void onMessageCreated(MessageCreateEvent event, Map<String, IExecute> commands) {
        final String CONTENT = event.getMessage().getContent();
        if (CONTENT.matches("^!(\\d+)?d\\d+([+-][d0-9]+)*(;(\\d+)?d\\d+([+-][d0-9]+)*)*;?")) {
            commands.get("diceroll").execute(event);
            return;
        }
        for (final Map.Entry<String, IExecute> ENTRY : commands.entrySet()) {
            if (CONTENT.startsWith('!' + ENTRY.getKey())) {
                ENTRY.getValue().execute(event);
                break;
            }
        }
        if (CONTENT.contains("820365062300500059") || CONTENT.contains("@apeBot")) {
            Objects.requireNonNull(event.getMessage().getChannel().block()).
                    createMessage(event.getMessage().getAuthor().get().getMention() + " type this:`!help`").block();
        }
    }

    public static void onReady(ReadyEvent event, GatewayDiscordClient client) {
        final User USER = event.getSelf();
        client.updatePresence(Presence.online(Activity.listening("!help"))).subscribe();
        System.out.printf("Logged in as %s#%s%n", USER.getUsername(), USER.getDiscriminator());
    }
}
