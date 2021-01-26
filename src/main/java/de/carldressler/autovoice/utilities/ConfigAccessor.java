package de.carldressler.autovoice.utilities;

import de.carldressler.autovoice.Bot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigAccessor {
    private static final Properties properties = new Properties();

    public ConfigAccessor() {
        String filename = "config.properties";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken(boolean isProdMode) {
        if (isProdMode)
            return properties.getProperty("prodToken");
        else
            return properties.getProperty("devToken");
    }

    public String[] getDBAuthInfo() {
        String[] info = new String[4];

        if (Bot.runsInProdMode) {
            info[0] = get("prodDBjdbcUrl");
            info[1] = get("prodDBUser");
            info[2] = get("prodDBPass");
            info[3] = get("prodDBSchema");
        } else {
            info[0] = get("devDBjdbcUrl");
            info[1] = get("devDBUser");
            info[2] = get("devDBPass");
            info[3] = get("devDBSchema");
        }
        return info;
    }

    public String get(String property) {
        return properties.getProperty(property);
    }
}
