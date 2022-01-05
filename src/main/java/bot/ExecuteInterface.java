package bot;

import discord4j.core.event.domain.message.MessageCreateEvent;

@FunctionalInterface
public interface ExecuteInterface {
    void execute(MessageCreateEvent event);
}
