package commands.admin;

import commands.CommandContext;
import commands.ICommand;
import modules.automod.Automod;
import modules.automod.AutomodManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class AutomodCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        Member author = ctx.getAuthorMember();
        Automod mod;

        //Check Permission
        if(!author.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("Insufficient Permission").queue();
            return;
        }

        // Send help
        if(args.size() < 2) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        //Setup Settings
        mod = AutomodManager.getMod(args.get(0));
        if(mod == null) {
            channel.sendMessage("Mod doesn't exist").queue();
            return;
        }

        //Set Status (on/off)
        if(mod.setStatus(args.get(1).equals("on"))) {
            channel.sendMessage(mod.getName() + " set to " + args.get(1).equals("on")).queue();
        }
    }

    @Override
    public String getCommand() {
        return "automod";
    }

    @Override
    public String getHelp() {
        return "automod [mod] [option](on/off)";
    }
}
