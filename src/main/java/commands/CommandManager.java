package commands;

import commands.admin.*;
import commands.superuser.util.ShutdownCommand;
import commands.text.hashing.Md5Command;
import commands.util.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import settings.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private static List<ICommand> commands = new ArrayList<>();

    public CommandManager() {

        //Superuser Commands
        //Utility
        addCommand(new ShutdownCommand());
//        addCommand(new ServerCommand());

        //Admin Commands
        addCommand(new KickCommand());
        addCommand(new BanCommand());
        addCommand(new UnbanCommand());
        addCommand(new MuteCommand());
        addCommand(new UnmuteCommand());
        addCommand(new AutomodCommand());

        //Utility Commands
        addCommand(new PingCommand());
        addCommand(new HelpCommand());
        addCommand(new InviteCommand());
        addCommand(new InfoCommand());
        addCommand(new HasteCommand());

        //Text Manipulation
        //Hashing
        addCommand(new Md5Command());
    }

    public boolean addCommand(ICommand command) {
        if(findCommand(command)) {
            System.out.println("Failed to Add Command!\nCommand Already Exists");
            return false;
        }
        commands.add(command);
        return true;
    }

    public static ICommand getCommand(String command) {
        for(ICommand cmd: commands) {
            if(cmd.getCommand().equals(command)) {
                return cmd;
            }
        }
        return null;
    }

    private boolean findCommand(ICommand command) {
        for(ICommand cmd: commands) {
            if(command.getCommand().equals(cmd.getCommand()))
                return true;
        }
        return false;
    }

    public void handle(GuildMessageReceivedEvent event) {
        String[] raw = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "").split("\\s+");
        String command = raw[0];
        ICommand cmd = getCommand(command);

        if(cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(raw).subList(1, raw.length);
            cmd.execute(new CommandContext(event, args));
        }
    }

}
