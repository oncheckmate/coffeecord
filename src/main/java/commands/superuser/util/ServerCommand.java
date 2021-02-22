package commands.superuser.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import settings.Config;

import java.net.InetAddress;
import java.util.List;

public class ServerCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        User user = ctx.getAuthor();

        if(!user.getId().equals(Config.get("OWNER_ID"))) {
            channel.sendMessage("Error: Superuser Command!").queue();
            return;
        }

        if(args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        if(args.get(0).equals("ip")) {
            channel.sendMessage(getIp()).complete();
        }
    }

    @Override
    public String getCommand() {
        return "server";
    }

    @Override
    public String getHelp() {
        return "NEED TO BE A SUPERUSER!\n" +
                Config.get("prefix") + "server [ip]";
    }

    private String getIp() {
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
