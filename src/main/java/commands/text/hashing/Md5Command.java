package commands.text.hashing;

import commands.CommandContext;
import commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import settings.Config;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Command implements ICommand {

    private static final Logger logger = LoggerFactory.getLogger(Md5Command.class);

    @Override
    public void execute(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Message message = ctx.getMessage();

        //Send help
        if(ctx.getArgs().isEmpty()) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        String formatMsg = removeSubStr(message.getContentRaw(), "$md5", 1);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(formatMsg.getBytes("UTF-8"));
            channel.sendMessage(String.format("%032x", new BigInteger(1, md5.digest()))).queue();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String getCommand() {
        return "md5";
    }

    @Override
    public String getHelp() {
        return Config.get("prefix") + "md5 [text]";
    }

    private String removeSubStr(String str, String remove, int additional) {
        int start = str.indexOf(remove);
        int stop = start + remove.length() + additional;

        StringBuffer strb = new StringBuffer(str);
        strb.delete(start, stop);
        return strb.toString();
    }

}
