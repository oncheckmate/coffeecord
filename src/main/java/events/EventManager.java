package events;

import events.guild.member.GuildMemberEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventManager implements IEventManager {

    private List<Object> listeners = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(EventManager.class);

    public EventManager() {
        register(new GuildMemberEvent());
    }

    @Override
    public void register(@NotNull Object listener) {
        if(!(listener instanceof ListenerAdapter)) {
            throw new IllegalArgumentException("listener must implement ListenerAdapter");
        }

        listeners.add(listener);
    }

    @Override
    public void unregister(@NotNull Object listener) {
        listeners.remove(listener);
    }

    @Override
    public void handle(@NotNull GenericEvent event) {
        if(!(event.getJDA().getStatus() == JDA.Status.CONNECTED)) {
            return;
        }

        List<Object> listenerCopy = new LinkedList<>(listeners);
        for (Object listener : listenerCopy) {
            try {
                ((EventListener) listener).onEvent(event);
            } catch (PermissionException throwable) {
                logger.error("unchecked permission error!");
                logger.error(throwable.getMessage());
            } catch (Throwable throwable) {
                logger.error(throwable.getMessage());
            }
        }
    }

    @NotNull
    @Override
    public List<Object> getRegisteredListeners() {
        return this.listeners;
    }
}