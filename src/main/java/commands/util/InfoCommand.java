package commands.util;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import settings.Config;

import java.awt.*;
import java.util.List;

public class InfoCommand implements ICommand {

    @Override
    public void execute(CommandContext ctx) {

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        JDA jda = ctx.getJDA();

        if(args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        if(args.get(0).equals("checkmate")) {
            channel.sendMessage(getCheckmateInfoEmbed().build()).queue();
        }
        else if(args.get(0).equals("bot")) {
            channel.sendMessage(getBotInfoEmbed(jda).build()).queue();
        }
    }

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "info [bot/checkmate]";
    }

    private EmbedBuilder getCheckmateInfoEmbed() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Bot Info");
        embed.setColor(Color.CYAN);

        embed.addField("❯ Checkmate Discord Server", "[Checkmate](https://discord.gg/pqJfwrPEHt)", true);
        embed.addField("❯ Checkmate Github", "[Checkmate](https://github.com/oncheckmate)", true);

        embed.setFooter("©2021 Checkmate");
        return embed;
    }

    private EmbedBuilder getBotInfoEmbed(JDA jda) {
        String numberOfGuilds = getNumberOfGuilds(jda);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Bot Info");
        embed.setColor(Color.CYAN);

        embed.addField("❯ Home Server", "[CoffeeCord](https://discord.gg/pqJfwrPEHt)", true);
        embed.addField("❯ Invite Bot", "[CoffeeCord](" + jda.getInviteUrl(Permission.ADMINISTRATOR) + ")", true);
        embed.addField("❯ Servers", numberOfGuilds, true);
        embed.addField("❯ Source Code", "[Github](https://github.com/oncheckmate/coffeecord)", true);
        embed.addField("❯ Version", "0.0.1", true);
        embed.addField("❯ Author", "Checkmate", true);

        embed.setFooter("©2021 Checkmate");
        return embed;
    }

    private String getNumberOfGuilds(JDA jda) {
        int numberOfGuilds = 0;
        for(Guild guild: jda.getGuilds()) {
            numberOfGuilds++;
        }
        return Integer.toString(numberOfGuilds);
    }
}
