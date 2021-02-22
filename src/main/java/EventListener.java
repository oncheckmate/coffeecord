import events.EventManager;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);
    private final EventManager eventManager = new EventManager();

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        eventManager.handle(event);
    }
}
