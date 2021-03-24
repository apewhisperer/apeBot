import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

import java.util.Map;

public class Events {

    public static void onMessageCreated(MessageCreateEvent event, Map<String, ExecuteInterface> commands) {

        final String content = event.getMessage().getContent();

        if (event.getMessage().getContent().contains("820365062300500059")) {
            event.getMessage()
                    .getChannel().block()
                    .createMessage("!help").block();
        }
        for (final Map.Entry<String, ExecuteInterface> entry : commands.entrySet()) {
            if (content.startsWith('!' + entry.getKey())) {
                entry.getValue().execute(event);
                break;
            }
        }
    }

    public static void onReady(ReadyEvent event, GatewayDiscordClient client) {
        client.updatePresence(Presence.online(Activity.listening("!help"))).subscribe();
        final User self = event.getSelf();
        System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
    }
}
