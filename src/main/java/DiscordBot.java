import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Map;

public class DiscordBot {
    private static final Map<String, ExecuteInterface> commands;

    static {
        commands = Commands.getCommands();
    }

    public static void main(String[] args) {

        GatewayDiscordClient client = DiscordClientBuilder.create("ODIwMzY1MDYyMzAwNTAwMDU5.YE0GgA.mCpuBe4HHBxipUVFWbSlOfxzE7M")
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    Events.onReady(event, client);
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .subscribe(event -> {
                    Events.onMessageCreated(event, commands);
                });

        client.onDisconnect().block();
    }
}
