package de.carldressler.autovoice;

import de.carldressler.autovoice.listeners.ChannelEventListener;
import de.carldressler.autovoice.listeners.CommandHandler;
import de.carldressler.autovoice.listeners.VoiceEventListener;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.database.DB;
import de.carldressler.autovoice.utilities.startup.ConfigAccessor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Set;

public class Bot {
    public static JDA jda;
    public static ConfigAccessor configAccessor;
    public static boolean runsInProdMode;
    private static final Logger logger = LoggerFactory.getLogger("Bot");

    public static void main(String[] args) throws LoginException, InterruptedException {
        setProdMode(args);
        configAccessor = new ConfigAccessor();

        DB.testConnectivity();

        String token = getToken();
        jda = JDABuilder
            .createDefault(token)
            .setActivity(Activity.watching(Constants.PREFIX + "help \uD83C\uDF89 v 1.1.3"))
            .addEventListeners(new CommandHandler(),
                new VoiceEventListener(),
                new ChannelEventListener())
                .build()
                .awaitReady();
    }

    static void setProdMode(String[] args) {
        if (args.length < 1 || !Set.of("prod", "dev").contains(args[0])) {
            logger.error("No or invalid first argument. Set first argument to either 'dev' or 'prod'");
            System.exit(1);
        }
        runsInProdMode = args[0].equals("prod");
    }

    public static String getToken() {
        return configAccessor.getToken();
    }
}
