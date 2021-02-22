package commands.admin;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import settings.Config;

import java.util.List;

public class KickCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Message message = ctx.getMessage();
        Member author = ctx.getAuthorMember();
        Member member;
        String reason = "Not Specified";

        //Check Permission
        if(!author.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("Insufficient Permission").queue();
            return;
        }

        // Send help
        if(args.size() < 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        //Check if user is in guild
        if(message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("User is not in this guild").queue();
            return;
        }

        //Get Reason
        if(args.size() == 2) {
            reason = args.get(1);
        }

        //Get member to ban
        member = message.getMentionedMembers().get(0);

        //Check if self-command
        if(author.getId().equals(member.getId())) {
            channel.sendMessage("Cannot kick yourself").queue();
            return;
        }

        try{
            member.kick().queue();
            channel.sendMessage(member.getUser().getAsTag() + " has been kicked\nReason: " + reason).queue();
        }
        catch (InsufficientPermissionException e) {
            channel.sendMessage("Insufficient Bot Permission").queue();
        }
        catch (HierarchyException e) {
            channel.sendMessage("Can't kick a member with higher or equal highest role than yourself").queue();
        }

    }

    @Override
    public String getCommand() {
        return "kick";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "kick [@user] [optional reason]";
    }
}
