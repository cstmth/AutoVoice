package de.carldressler.autovoice;

import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.listeners.ChannelListener;
import de.carldressler.autovoice.listeners.CommandListener;
import de.carldressler.autovoice.listeners.VoiceListener;
import de.carldressler.autovoice.utilities.ConfigAccessor;
import de.carldressler.autovoice.utilities.ExecutionModeDeterminator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA instance;
    public static ConfigAccessor configAccessor;
    public static boolean runsInProdMode;

    public static void main(String[] args) throws LoginException, InterruptedException {
        setProdMode(args);
        configAccessor = new ConfigAccessor();

        DB.testConnectivity();

        String token = getToken();
        instance = JDABuilder
                .createDefault(token)
                .setActivity(Activity.watching("\uD83C\uDF89 v 1.0 \uD83C\uDF8A"))
                .addEventListeners(new CommandListener(), new VoiceListener(), new ChannelListener())
                .build()
                .awaitReady();
    }

    static void setProdMode(String[] args) {
        runsInProdMode = new ExecutionModeDeterminator().isProd(args);
        System.out.println(runsInProdMode);
    }

    public static String getToken() {
        return configAccessor.getToken();
    }
}
