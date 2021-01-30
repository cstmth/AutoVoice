package de.carldressler.autovoice;

import de.carldressler.autovoice.commands.tempchannel.lock.LockEventListener;
import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.listeners.ChannelEventListener;
import de.carldressler.autovoice.listeners.CommandHandler;
import de.carldressler.autovoice.listeners.VoiceEventListener;
import de.carldressler.autovoice.utilities.startup.ConfigAccessor;
import de.carldressler.autovoice.utilities.startup.ExecutionManager;
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
            .setActivity(Activity.watching("\uD83C\uDF89 v 1.0 \uD83C\uDF8A"))
            .addEventListeners(new CommandHandler(),
                new VoiceEventListener(),
                new ChannelEventListener(),
                new LockEventListener())
                .build()
                .awaitReady();
    }

    static void setProdMode(String[] args) {
        runsInProdMode = new ExecutionManager().isProd(args);
        System.out.println(runsInProdMode);
    }

    public static String getToken() {
        return configAccessor.getToken();
    }
}
