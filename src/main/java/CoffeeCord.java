import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import settings.Config;

public class CoffeeCord {
    public static void main(String[] args) throws Exception  {

        String token = Config.get("token");

        JDABuilder.createLight(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new CommandListener())
                .addEventListeners(new AutomodListener())
                .addEventListeners(new EventListener())
                .setActivity(Activity.listening("Murder, Inc."))
                .build();
    }
}
