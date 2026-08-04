package helpers;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/app?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "app_user";
    private static final String DB_PASS = "app_pass";
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void cleanDatabase() {
        try (Connection conn = getConnection()) {
            runner.execute(conn, "SET FOREIGN_KEY_CHECKS = 0");
            runner.execute(conn, "TRUNCATE TABLE auth_codes");
            runner.execute(conn, "TRUNCATE TABLE card_transactions");
            runner.execute(conn, "TRUNCATE TABLE cards");
            runner.execute(conn, "TRUNCATE TABLE users");
            runner.execute(conn, "SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("Database cleaned");
            addTestUsers(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error cleaning database", e);
        }
    }

    private static void addTestUsers(Connection conn) throws SQLException {
        String sql = "INSERT INTO users(id, login, password, status) VALUES " +
                "(UUID(), 'vasya', '$2a$10$ALBItrOhQqm3M8y5POxRc.NC7zYpEJ9sfEoqNzXJ5XBchaIdv6rZK', 'active'), " +
                "(UUID(), 'petya', '$2a$10$ALBItrOhQqm3M8y5POxRc.NC7zYpEJ9sfEoqNzXJ5XBchaIdv6rZK', 'active')";
        runner.execute(conn, sql);
        System.out.println("Test users added: vasya, petya");
    }

    public static String getLatestAuthCode(String login) {
        String sql = "SELECT a.code FROM auth_codes a " +
                "JOIN users u ON a.user_id = u.id " +
                "WHERE u.login = ? " +
                "ORDER BY a.created DESC LIMIT 1";
        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting auth code", e);
        }
    }

    public static String getUserId(String login) {
        String sql = "SELECT id FROM users WHERE login = ?";
        try (Connection conn = getConnection()) {
            return runner.query(conn, sql, new ScalarHandler<>(), login);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting user id", e);
        }
    }
}