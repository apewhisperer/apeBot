package bot;

import discord4j.rest.request.RouteMatcher;
import discord4j.rest.route.Routes;
import functions.Commands;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class DiscordBot {

    private static final Map<String, ExecuteInterface> COMMANDS;

    static {
        COMMANDS = Commands.getCommands();
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        GatewayDiscordClient client = DiscordClientBuilder.create(Objects.requireNonNull(dotenv.get("TOKEN")))
                .onClientResponse(request -> {
                    if (RouteMatcher.route(Routes.MESSAGE_CREATE).matches(request)) {
                        return mono -> mono.timeout(Duration.ofSeconds(15));
                    }
                    return mono -> mono;
                })
                .build()
                .login()
                .block();
        if (client != null) {
            run(client);
        }
    }

    private static void run(GatewayDiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> Events.onReady(event, client));
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(event -> event.getMessage().getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(event -> Events.onMessageCreated(event, COMMANDS));
        client.onDisconnect().block();
    }
}
