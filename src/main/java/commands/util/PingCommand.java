package commands.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import settings.Config;

public class PingCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {
        JDA jda = ctx.getJDA();
        TextChannel channel = ctx.getChannel();
        channel.sendMessage("PONG! `" + jda.getGatewayPing() + "ms`").queue();
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "ping";
    }
}
