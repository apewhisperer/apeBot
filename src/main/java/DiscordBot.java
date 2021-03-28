import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Map;

public class DiscordBot {
    private static final Map<String, IExecute> commands;

    static {
        commands = Commands.getCommands();
    }

    public static void main(String[] args) {

        GatewayDiscordClient client = DiscordClientBuilder.create("ODIwMzY1MDYyMzAwNTAwMDU5.YE0GgA.PRGZHhTF6YEfuyraDPYQz3n492k")
                .build()
                .login()
                .block();

        assert client != null;
        run(client);
    }

    private static void run(GatewayDiscordClient client) {

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> Events.onReady(event, client));

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(message -> Events.onMessageCreated(message, commands));

        client.onDisconnect().block();
    }
}
