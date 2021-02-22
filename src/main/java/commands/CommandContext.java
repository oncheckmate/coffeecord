package commands;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.List;

public class CommandContext {

    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    public JDA getJDA() {
        return this.event.getJDA();
    }

    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    public Guild getGuild() {
        return this.event.getGuild();
    }

    public User getAuthor() {
        return this.event.getAuthor();
    }

    public Member getAuthorMember() {
        return this.event.getMember();
    }

    public TextChannel getChannel() {
        return this.event.getChannel();
    }

    public Message getMessage() {
        return this.event.getMessage();
    }

    public ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    public User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    public Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }

    public List<String> getArgs() {
        return this.args;
    }
}
