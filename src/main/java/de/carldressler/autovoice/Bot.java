package de.carldressler.autovoice;

import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.listeners.CommandListener;
import de.carldressler.autovoice.utilities.ConfigAccessor;
import de.carldressler.autovoice.utilities.ExecutionModeDeterminator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA instance;
    public static ConfigAccessor configAccessor = new ConfigAccessor();
    public static boolean runsInProdMode;

    public static void main(String[] args) throws LoginException, InterruptedException {
        setProdMode(args);

        DB.testConnectivity();

        String token = getToken();
        instance = JDABuilder
                .createDefault(token)
                .addEventListeners(new CommandListener())
                .build()
                .awaitReady();
    }

    static void setProdMode(String[] args) {
        runsInProdMode = new ExecutionModeDeterminator().isProd(args);
        System.out.println(runsInProdMode);
    }

    public static String getToken() {
        return configAccessor.getToken(runsInProdMode);
    }
}
