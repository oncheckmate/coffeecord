package modules.automod.mods;

import modules.automod.Automod;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BannedWords extends Automod {

    @Override
    public void handle(GuildMessageReceivedEvent event) {
        if(!getStatus()) {
            return;
        }

        if(event.getMessage().getContentRaw().contains("ghetto")) {
            event.getMessage().delete().queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "bannedwords";
    }
}
