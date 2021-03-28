import discord4j.core.object.entity.Message;

@FunctionalInterface
public interface IExecute {
    void execute(Message message);
}
