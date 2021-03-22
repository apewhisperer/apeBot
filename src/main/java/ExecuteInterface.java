import discord4j.core.event.domain.message.MessageCreateEvent;

public interface ExecuteInterface {
    void execute(MessageCreateEvent event);
}
