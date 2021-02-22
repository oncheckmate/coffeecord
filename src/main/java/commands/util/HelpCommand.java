package commands.util;

import commands.CommandContext;
import commands.CommandManager;
import commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import settings.Config;

import java.awt.*;
import java.util.List;

public class HelpCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if(args.size() != 1) {
            channel.sendMessage(getHelpEmbed().build()).queue();
            return;
        }

        ICommand cmd = CommandManager.getCommand(args.get(0));
        if(cmd != null) {
            channel.sendMessage(cmd.getHelp()).queue();
        }
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "help\n" + Config.get("prefix") + "help [COMMAND]";
    }

    private EmbedBuilder getHelpEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Help Commands");
        embed.setColor(Color.CYAN);
        embed.setDescription("Prefix: " + Config.get("prefix"));

        embed.addField("❯ Superuser", "`shutdown` " +
                "`server [ip]`", true);

        embed.addBlankField(true);
        embed.addBlankField(true);

        embed.addField("❯ Util", "`help` " +
                "`info [bot/checkmate]` " +
                "`invite [server/bot]` ", true);

        embed.addBlankField(true);
        embed.addBlankField(true);

        embed.addField("❯ Admin", "`ban [@user] [optional reason]` " +
                "`unban [userId]` " +
                "`kick [@user] [optional reason]` " +
                "`mute [@User] [Optional reason]` " +
                "`unmute [@User]` ", true);

        embed.setFooter("❯ ©2021 Checkmate");
        return embed;
    }
}
