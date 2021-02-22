package modules.automod;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public abstract class Automod {

    private static final Logger logger = LoggerFactory.getLogger(Automod.class);

    public abstract void handle(GuildMessageReceivedEvent event);
    public abstract String getName();

    public boolean setStatus(boolean s) {
        try {
            Ini ini = new Ini(new File("config.ini"));
            ini.put("automod", getName(), s);
            ini.store();
            return true;

        } catch (InvalidFileFormatException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    public boolean getStatus() {
        try {
            Ini ini = new Ini(new File("config.ini"));
            return ini.get("automod", getName()).equals("true");

        } catch (InvalidFileFormatException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}
