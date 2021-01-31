package de.carldressler.autovoice;

import de.carldressler.autovoice.utilities.database.DB;
import de.carldressler.autovoice.listeners.ChannelEventListener;
import de.carldressler.autovoice.listeners.CommandHandler;
import de.carldressler.autovoice.listeners.VoiceEventListener;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.startup.ConfigAccessor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA jda;
    public static ConfigAccessor configAccessor;
    public static boolean runsInProdMode;

    public static void main(String[] args) throws LoginException, InterruptedException {
        setProdMode(args);
        configAccessor = new ConfigAccessor();

        DB.testConnectivity();

        String token = getToken();
        jda = JDABuilder
            .createDefault(token)
            .setActivity(Activity.watching(Constants.PREFIX + "setup \uD83C\uDF89 v 1.1"))
            .addEventListeners(new CommandHandler(),
                new VoiceEventListener(),
                new ChannelEventListener())
                .build()
                .awaitReady();
    }

    static void setProdMode(String[] args) {
        runsInProdMode = args.length > 1 || args[0].equals("prod");
    }

    public static String getToken() {
        return configAccessor.getToken();
    }
}
