package commands.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import settings.Config;

import java.util.List;

public class InviteCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        JDA jda = ctx.getJDA();

        if(args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        if(args.get(0).equals("server")) {
            channel.sendMessage("Server Invite Link: " + channel.createInvite().complete().getUrl()).queue();
        }
        else if(args.get(0).equals("bot")) {
            channel.sendMessage("Bot Invite Link: " + jda.getInviteUrl(Permission.ADMINISTRATOR)).queue();
        }
    }

    @Override
    public String getCommand() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "invite [server/bot]";
    }
}
