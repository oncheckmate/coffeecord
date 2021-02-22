package commands.admin;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import settings.Config;

import java.util.List;
import java.util.function.Consumer;

public class UnbanCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Guild guild = ctx.getGuild();
        Member author = ctx.getAuthorMember();
        String userId;

        //Check Permission
        if(!author.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage("Insufficient Permission").queue();
            return;
        }

        //Send help
        if(args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        try{
            //Get Requested UserId
            userId = args.get(0);

            guild.unban(userId).queue(new Consumer<Void>() {
                @Override
                public void accept(Void unused) {
                    channel.sendMessage("User " + userId + " has been unbanned").queue();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    channel.sendMessage("User " + userId + " is not banned").queue();
                }
            });
        }
        catch (InsufficientPermissionException e) {
            channel.sendMessage("Insufficient Bot Permission").queue();
        }
        catch (HierarchyException e) {
            channel.sendMessage("Can't unban a member with higher or equal highest role than yourself").queue();
        }
        catch (IllegalArgumentException e) {
            channel.sendMessage("UserId can only contain numbers").queue();
        }

    }

    @Override
    public String getCommand() {
        return "unban";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "unban [userId]";
    }
}
