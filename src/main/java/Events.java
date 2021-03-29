import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

import java.util.Map;
import java.util.Objects;

public class Events {

    public static void onMessageCreated(Message message, Map<String, IExecute> commands) {

        final String content = message.getContent();

        if (content.matches("^!\\d+d\\d+[+-]\\S+") || content.matches("^!d\\d+[+-]\\S+") || content.matches("^!\\d+d\\S+") || content.matches("^!d\\d+")) {
            commands.get("d").execute(message);
            return;
        }
        for (final Map.Entry<String, IExecute> entry : commands.entrySet()) {
            if (content.startsWith('!' + entry.getKey())) {
                entry.getValue().execute(message);
                break;
            }
        }
        if (content.contains("820365062300500059")) {
            Objects.requireNonNull(message.getChannel().block()).
                    createMessage(message.getAuthor().get().getMention() + " type this:`!help`").block();
        }
    }

    public static void onReady(ReadyEvent event, GatewayDiscordClient client) {

        final User user = event.getSelf();

        client.updatePresence(Presence.online(Activity.listening("!help"))).subscribe();
        System.out.printf("Logged in as %s#%s%n", user.getUsername(), user.getDiscriminator());
    }
}
