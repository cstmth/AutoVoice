package de.carldressler.autovoice.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.carldressler.autovoice.Bot;
import de.carldressler.autovoice.utilities.Logging;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class DB {
    static DataSource dataSource;
    static Logger logger = Logging.getLogger("DB");

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
            logger.error("Could not execute regular statement", err);
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
        }
    }

    static public void executeInBatch(String... SQLs) {
        Statement stmt = getStatement();
        try {
            for (String sql : SQLs) {
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
            stmt.getConnection().commit();
        } catch (SQLException err) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException rollbackError) {
                logger.error("Could not roll back failed batch query", rollbackError);
            }
            logger.error("Could not execute batch query", err);
        } finally {
            closeConnection(stmt);
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

    public static void closeConnection(ResultSet rs) {
        logger.debug("Closing ResultSet");
        try {
            rs.close();
        } catch (SQLException err) {
            throw new RuntimeException("Could not close ResultSet", err);
        }
    }

    /**
     * This method is reserved for result set connections that have been processed.
     */
    static public void closeConnection(Statement statement) {
        logger.debug("Closing Statement (connection)");
        try {
            closeConnection(statement.getConnection());
        } catch (SQLException err) {
            throw new RuntimeException("Could not close ResultSet", err);
        }
    }

    static public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException err) {
            throw new RuntimeException("Could not close ResultSet", err);
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
