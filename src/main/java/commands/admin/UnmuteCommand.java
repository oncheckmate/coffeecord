package commands.admin;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import settings.Config;

import java.util.List;

public class UnmuteCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        JDA jda = ctx.getJDA();
        Message message = ctx.getMessage();
        Guild guild = ctx.getGuild();
        Member author = ctx.getAuthorMember();
        Member member;

        //Check Permission
        if(!author.hasPermission(Permission.getPermissions(4194304L))) {
            channel.sendMessage("Insufficient Permission").queue();
            return;
        }

        //Send Help
        if(args.size() < 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        //Check if user is in guild
        if(message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("User is not in this guild").queue();
            return;
        }

        //Get member to unmute
        member = message.getMentionedMembers().get(0);
        Role muteRole = getMuteRole(guild);

        //Check if user is muted
        if(!isMuted(member, muteRole)) {
            channel.sendMessage(member.getUser().getName() + " is not muted").queue();
            return;
        }

        //Check if self-command
        if(author.getId().equals(member.getId())) {
            channel.sendMessage("Cannot unmute yourself").queue();
            return;
        }

        try{
            guild.removeRoleFromMember(member, muteRole).queue();
            channel.sendMessage(member.getUser().getAsTag() + " has been unmuted").queue();
        }
        catch (InsufficientPermissionException e) {
            channel.sendMessage("Insufficient Bot Permission").queue();
        }
        catch (HierarchyException e) {
            channel.sendMessage("Can't unmute a member with higher or equal highest role than yourself").queue();
        }
    }

    @Override
    public String getCommand() {
        return "unmute";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "unmute [@User]";
    }

    private Role getMuteRole(Guild guild) {
        if(guild.getRolesByName("coffeemute", true).isEmpty()){
            return null;
        }
        return guild.getRolesByName("coffeemute", true).get(0);
    }

    private boolean isMuted(Member member, Role muteRole) {
        for(Role r: member.getRoles() ){
            if(r == muteRole) {
                return true;
            }
        }
        return false;
    }
}
