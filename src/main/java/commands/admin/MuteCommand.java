package commands.admin;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import settings.Config;

import java.awt.*;
import java.util.List;

public class MuteCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Message message = ctx.getMessage();
        Guild guild = ctx.getGuild();
        Member author = ctx.getAuthorMember();
        Member member;
        String reason = "Not Specified";

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

        //Get Reason
        if(args.size() == 2) {
            reason = args.get(1);
        }

        //Get member to mute
        member = message.getMentionedMembers().get(0);

        //Get Mute Role
        if(getMuteRole(guild) == null) {
            createMuteRole(guild);
        }
        Role muteRole = getMuteRole(guild);

        //Check if user is already muted
        for(Role role: member.getRoles() ){
            if(role == muteRole) {
                channel.sendMessage(member.getUser().getName() + " is already muted").queue();
                return;
            }
        }

        //Check if self-command
        if(author.getId().equals(member.getId())) {
            channel.sendMessage("Cannot mute yourself").queue();
            return;
        }

        try{
            guild.addRoleToMember(member, muteRole).queue();
            channel.sendMessage(member.getUser().getAsTag() + " has been Muted\nReason: " + reason).queue();
        }
        catch (InsufficientPermissionException e) {
            channel.sendMessage("Insufficient Bot Permission").queue();
        }
        catch (HierarchyException e) {
            channel.sendMessage("Can't mute a member with higher or equal highest role than yourself").queue();
        }

    }

    @Override
    public String getCommand() {
        return "mute";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "mute [@User] [Optional reason]";
    }

    private boolean createMuteRole(Guild guild) {
        if(guild.getRolesByName("coffeemute", true).isEmpty()) {
            RoleAction role = guild.createRole();
            role.setName("coffeemute");
            role.setColor(Color.GRAY);
            role.setPermissions(Permission.VIEW_CHANNEL);
            role.setMentionable(false);
            role.complete();
            return true;
        }
        return false;
    }

    private Role getMuteRole(Guild guild) {
        if(guild.getRolesByName("coffeemute", true).isEmpty()){
            return null;
        }
        return guild.getRolesByName("coffeemute", true).get(0);
    }
}
