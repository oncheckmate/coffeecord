package modules.automod;

import modules.automod.mods.BannedWords;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class AutomodManager {

    private static ArrayList<Automod> mods = new ArrayList<>();

    public AutomodManager() {
        addMod(new BannedWords());
    }

    public boolean addMod(Automod mod) {
        if(findMod(mod)) {
            System.out.println("Failed to Add AutoMode!\nAutoMode Already Exists");
            return false;
        }
        mods.add(mod);
        return true;
    }

    private boolean findMod(Automod mod) {
        for(Automod m: mods) {
            if(m.getName().equals(mod.getName()))
                return true;
        }
        return false;
    }

    public static Automod getMod(String modName) {
        for(Automod mod: mods) {
            if(mod.getName().equals(modName))
                return mod;
        }
        return null;
    }

    public void handle(GuildMessageReceivedEvent event) {
        for(Automod mod: mods) {
            if(mod.getStatus()) {
                mod.handle(event);
            }
        }
    }
}
