import modules.automod.AutomodManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AutomodListener extends ListenerAdapter {

    private final AutomodManager automodManager = new AutomodManager();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        automodManager.handle(event);
    }
}
