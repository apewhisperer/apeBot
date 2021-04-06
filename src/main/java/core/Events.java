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

        final String content = event.getMessage().getContent();

        if (content.matches("^!(\\d+)?d\\d+([+-][d0-9]+)*(;(\\d+)?d\\d+([+-][d0-9]+)*)*;?")) {
            commands.get("d").execute(event);
            return;
        }
        for (final Map.Entry<String, IExecute> entry : commands.entrySet()) {
            if (content.startsWith('!' + entry.getKey())) {
                entry.getValue().execute(event);
                break;
            }
        }
        if (content.contains("820365062300500059")) {
            Objects.requireNonNull(event.getMessage().getChannel().block()).
                    createMessage(event.getMessage().getAuthor().get().getMention() + " type this:`!help`").block();
        }
    }

    public static void onReady(ReadyEvent event, GatewayDiscordClient client) {

        final User user = event.getSelf();

        client.updatePresence(Presence.online(Activity.listening("!help"))).subscribe();
        System.out.printf("Logged in as %s#%s%n", user.getUsername(), user.getDiscriminator());
    }
}
