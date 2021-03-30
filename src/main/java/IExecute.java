import discord4j.core.event.domain.message.MessageCreateEvent;

@FunctionalInterface
public interface IExecute {
    void execute(MessageCreateEvent event);
}
