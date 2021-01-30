package de.carldressler.autovoice.utilities.startup;

import de.carldressler.autovoice.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigAccessor {
    private final Properties jdaProperties = new Properties();
    private final Properties hikariProperties = new Properties();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ConfigAccessor() {
        String jdaFilename;
        InputStream jdaInputStream;

        String hikariFilename;
        InputStream hikariInputStream;

        if (Bot.runsInProdMode) {
            logger.info("Starting in PRODUCTION mode");
            jdaFilename = "jda_prod.properties";
            hikariFilename = "hikari_prod.properties";
        } else {
            logger.info("Starting in DEVELOPMENT mode");
            jdaFilename = "jda_dev.properties";
            hikariFilename = "hikari_dev.properties";
        }
        jdaInputStream = this.getClass().getClassLoader().getResourceAsStream(jdaFilename);
        hikariInputStream = this.getClass().getClassLoader().getResourceAsStream(hikariFilename);
        try {
            jdaProperties.load(jdaInputStream);
            hikariProperties.load(hikariInputStream);
        } catch (IOException err) {
            throw new RuntimeException("Cannot load Properties properties with InputStream");
        }
    }

    public String getToken() {
        return getJDAProperties().getProperty("token");
    }

    public Properties getJDAProperties() {
        return jdaProperties;
    }

    public Properties getHikariProperties() {
        return hikariProperties;
    }
}
