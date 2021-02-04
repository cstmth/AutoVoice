package de.carldressler.autovoice.utilities.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.carldressler.autovoice.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class DB {
    static DataSource dataSource;
    static Logger logger = LoggerFactory.getLogger("DB");

    static {
        Properties properties = Bot.configAccessor.getHikariProperties();
        HikariConfig config = new HikariConfig(properties);
        dataSource = new HikariDataSource(config);
    }

    static public void testConnectivity() {
        executeStatement("SELECT * FROM auto_channels LIMIT 10");
    }

    static public void executeStatement(String sql) {
        Statement statement = getStatement();
        try {
            statement.execute(sql);
            statement.getConnection().commit();
            closeConnection(statement);
        } catch (SQLException err) {
            logger.error("Could not execute Statement", err);
        } finally {
            closeConnection(statement);
        }
    }

    static public void executePreparedStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
            preparedStatement.getConnection().commit();
        } catch (SQLException err) {
            logger.error("Could not execute PreparedStatement", err);
        } finally {
            closeConnection(preparedStatement);
        }
    }

    /**
     * Please note that the associated connection is not automatically closed.
     */
    static public ResultSet queryPreparedStatement(PreparedStatement preparedStatement) {
        try {
            return preparedStatement.executeQuery();
        } catch (SQLException err) {
            throw new RuntimeException("Could not query PreparedStatement", err);
        } finally {
            closeConnection(preparedStatement);
        }
    }

    static public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException err) {
            throw new RuntimeException("Could not get regular Statement object", err);
        }
    }

    static public PreparedStatement getPreparedStatement(String sql) {
        try {
            return getConnection().prepareStatement(sql);
        } catch (SQLException err) {
            throw new RuntimeException("Could not get PreparedStatement object", err);
        }
    }

    static public void closeConnection(Statement statement) {
        logger.debug("Closing Statement (connection)");
        try {
            statement.getConnection().close();
        } catch (SQLException err) {
            throw new RuntimeException("Could not close Statement or PreparedStatement", err);
        }
    }

    static Connection getConnection() {
        logger.debug("Getting connection");
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException err) {
            throw new RuntimeException("Could not get valid database connection", err);
        }
    }


}
