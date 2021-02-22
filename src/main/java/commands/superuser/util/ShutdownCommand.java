package commands.superuser.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import settings.Config;

import java.util.List;

public class ShutdownCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        User user = ctx.getAuthor();

        if(!user.getId().equals(Config.get("OWNER_ID"))) {
            channel.sendMessage("Error: Superuser Command!").queue();
            return;
        }

        channel.sendMessage("Shutting down...").complete();
        System.exit(0);
    }

    @Override
    public String getCommand() {
        return "shutdown";
    }

    @Override
    public String getHelp() {
        return "NEED TO BE A SUPERUSER!\n" +
                Config.get("prefix") + "shutdown";
    }
}
